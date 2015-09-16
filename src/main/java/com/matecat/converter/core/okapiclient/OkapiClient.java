package com.matecat.converter.core.okapiclient;

import com.matecat.converter.core.XliffProcessor;
import com.matecat.converter.core.encoding.Encoding;
import com.matecat.converter.core.format.Format;
import com.matecat.converter.okapi.steps.segmentation.AddIcuHintsStep;
import com.matecat.converter.okapi.steps.segmentation.RemoveIcuHintsStep;
import net.sf.okapi.common.LocaleId;
import net.sf.okapi.common.Util;
import net.sf.okapi.common.filters.FilterConfigurationMapper;
import net.sf.okapi.common.filters.IFilter;
import net.sf.okapi.common.filters.IFilterConfigurationMapper;
import net.sf.okapi.common.pipelinedriver.BatchItemContext;
import net.sf.okapi.common.pipelinedriver.IPipelineDriver;
import net.sf.okapi.common.pipelinedriver.PipelineDriver;
import net.sf.okapi.common.resource.RawDocument;
import net.sf.okapi.filters.html.HtmlFilter;
import net.sf.okapi.filters.rainbowkit.RainbowKitFilter;
import net.sf.okapi.filters.table.TableFilter;
import net.sf.okapi.steps.common.RawDocumentToFilterEventsStep;
import net.sf.okapi.steps.rainbowkit.creation.ExtractionStep;
import net.sf.okapi.steps.rainbowkit.postprocess.MergingStep;
import net.sf.okapi.steps.rainbowkit.postprocess.Parameters;
import net.sf.okapi.steps.segmentation.SegmentationStep;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.matecat.converter.core.format.Format.*;


/**
 * Okapi Client
 *
 * This class interacts with the Okapi framework in order to generate its result pack, or to merge it into
 * the derived (translated) file.
 */
public class OkapiClient {

    // Logger
    private static Logger LOGGER = LoggerFactory.getLogger(OkapiClient.class);

    /**
     * Private constructor (static class)
     */
    private OkapiClient() {}

    /* SEGMENTATION RULES */

    /**
     * Initialization of the default segmentation rules and its folder
     */
    public static final String SRX_RESOURCE_PATH = "/okapi/segmentation/default.srx";
    public static final File SRX_FILE;
    static {
        // The SRX file is a resource of the application. When it is
        // packed inside a jar Okapi fails reading its content.
        // In order to make it work in every situation, I copy the SRX
        // in a regular file on the filesystem, and then I pass this
        // new file to Okapi.
        try {
            SRX_FILE = File.createTempFile("matecat-converter-srx-rules", null);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create a temp file for SRX rules.", e);
        }
        try {
            FileUtils.copyInputStreamToFile(System.class.getResourceAsStream(SRX_RESOURCE_PATH), SRX_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Cannot copy SRX rules to temp file.", e);
        }
    }


    /* IS SUPPORTED */

    /**
     * Check if a format is supported
     * @param format Format
     * @return True if the format is supported, false otherwise
     */
    public static boolean isSupported(Format format) {
        return OkapiFilterFactory.isSupported(format);
    }


    /* OKAPI PIPELINE / STEPS CREATION */

    /**
     * Create pipeline driver
     * @param root File's root
     * @return Pipeline driver
     */
    private static IPipelineDriver createOkapiPipelineDriver(String root) {

        // Create the pipeline driver
        IPipelineDriver driver = new PipelineDriver();

        // Set output
        driver.setRootDirectories(root, root);
        driver.setOutputDirectory(root);

        // Return the driver
        return driver;
    }

    /**
     * Create the segmentation step
     * @param sourceLanguage Source language
     * @return Segmentation step
     */
    private static SegmentationStep createSegmentationStep(Locale sourceLanguage) {
        SegmentationStep segmentationStep = new SegmentationStep();
        net.sf.okapi.steps.segmentation.Parameters params = (net.sf.okapi.steps.segmentation.Parameters) segmentationStep.getParameters();
        params.setSourceSrxPath(SRX_FILE.getPath());
        return segmentationStep;
    }

    /**
     * Create the extraction step
     * @return Extraction step
     */
    private static ExtractionStep createExtractionStep() {
        ExtractionStep extStep = new ExtractionStep();
        net.sf.okapi.steps.rainbowkit.creation.Parameters extParams = (net.sf.okapi.steps.rainbowkit.creation.Parameters) extStep.getParameters();
        extParams.setPackageName(OkapiPack.PACK_FILENAME);
        return extStep;
    }

    /**
     * Create the merging steps
     * @return Merging step
     */
    private static MergingStep createMergingStep() {
        MergingStep mergingStep = new MergingStep();
        net.sf.okapi.steps.rainbowkit.postprocess.Parameters params = (net.sf.okapi.steps.rainbowkit.postprocess.Parameters) mergingStep.getParameters();
        // Sometimes the xliff's target language is different from
        // the target specified in the Okapi manifest file.
        // This happens for example when in MateCAT you create a
        // project with multiple target languages. MateCAT calls
        // the converter just one time and then uses the obtained
        // xliff with each translation job in each target language,
        // just changing the target attributes in the xliff.
        // When the xliff's target segments language is different
        // from the one specified in the manifest file, Okapi
        // ignores them and uses source segments instead. So after
        // the merge you obtain a document without translations.
        // Using the following options you tell Okapi to ignore the
        // target language specified in the manifest, and to consider
        // always the one specified in the pipeline configuration,
        // that we create using the xliff's target language.
        params.setForceTargetLocale(true);
        return mergingStep;
    }

    /**
     * Create a filter configuration mapper
     * @param filter Filter being used
     * @return Configuration mapper, including common configurations and the corresponding to the current filter
     */
    private static IFilterConfigurationMapper createFilterConfigurationMapper(IFilter filter) {

        // Create a filter configuration map and add mandatory configurations
        IFilterConfigurationMapper fcMapper = new FilterConfigurationMapper();
        fcMapper.addConfigurations(RainbowKitFilter.class.getName());
        fcMapper.addConfigurations(HtmlFilter.class.getName());
        fcMapper.addConfigurations(TableFilter.class.getName());

        // Add custom configurations
        fcMapper.addConfigurations(filter.getClass().getName());

        return fcMapper;
    }


    /* OKAPI INTERACTION */

    /**
     * Generate pack
     *
     * From the inputs, generate and return an Okapi's result pack
     *
     * @param sourceLanguage Source language
     * @param targetLanguage Target language
     * @param encoding Encoding used
     * @param file File
     * @return Okapi's result pack
     */
    public static OkapiPack generatePack(Locale sourceLanguage, Locale targetLanguage, Encoding encoding, File file) {

        // Check inputs
        if (sourceLanguage == null)
            throw new IllegalArgumentException("The source language cannot be null");
        if (targetLanguage == null)
            throw new IllegalArgumentException("The target language cannot be null");
        if (encoding == null)
            throw new IllegalArgumentException("The input encoding cannot be null");
        if (file == null  ||  !file.exists()  ||  file.isDirectory())
            throw new IllegalArgumentException("The input file is not valid");

        // Output pack folder
        File packFolder = new File(file.getParentFile().getPath() + File.separator + OkapiPack.PACK_FILENAME);

        // Create the pipeline driver
        IPipelineDriver driver = createOkapiPipelineDriver(file.getParent());

        // Filtering step
        RawDocumentToFilterEventsStep filteringStep = new RawDocumentToFilterEventsStep();
        IFilter filter = OkapiFilterFactory.getFilter(getFormat(file));
        filteringStep.setFilter(filter);
        driver.addStep(filteringStep);

        // Set the filter configuration map to use with the driver
        driver.setFilterConfigurationMapper(createFilterConfigurationMapper(filter));

        // Add ICU sentences boundaries hint, to help the SRX segmentation step
        driver.addStep(new AddIcuHintsStep(sourceLanguage));

        // Segmentation step
        driver.addStep(createSegmentationStep(sourceLanguage));

        // Remove ICU hints restoring original segments
        driver.addStep(new RemoveIcuHintsStep());

        // Kit creation step
        driver.addStep(createExtractionStep());

        // Add the input file to the driver
        // WARNING:
        // new LocaleId(sourceLanguage) DOESN'T WORK for languages like "sr-Latn-RS" (Serbian Latin)
        // new LocaleId(sourceLanguage.toLanguageTag()) instead works properly
        RawDocument rawDoc = new RawDocument(file.toURI(), encoding.getCode(), new LocaleId(sourceLanguage.toLanguageTag()), new LocaleId(targetLanguage.toLanguageTag()), filter.getName());

        // Output file (useless but needed)
        String basename = Util.getFilename(file.getPath(), false);
        String outputPath = packFolder.getParentFile().getPath() + File.separator + basename + ".out" + Util.getExtension(file.getPath());
        File outputFile = new File(outputPath);

        // Create batch and run it
        BatchItemContext item = new BatchItemContext(rawDoc, outputFile.toURI(), encoding.getCode());
        driver.addBatchItem(item);
        driver.processBatch();

        // Check that it has been created
        if (!packFolder.exists())
            throw new RuntimeException("The pack could not be created");

        // Return pack
        return new OkapiPack(packFolder);
    }

    /**
     * Generate the derived file from a Okapi's result pack
     * @param pack Okapi Results pack
     * @return Derived file
     */
    public static File generateDerivedFile(OkapiPack pack) {

        try {

            // Create pipeline
            String root = pack.getPackFolder().getParent();
            IPipelineDriver driver = createOkapiPipelineDriver(root);
            driver.setFilterConfigurationMapper(
                    createFilterConfigurationMapper(
                            OkapiFilterFactory.getFilter(Format.getFormat(pack.getOriginalFile()))));

            // Add the extraction step
            driver.addStep(new RawDocumentToFilterEventsStep());

            // Add the t-kit merging step
            driver.addStep(createMergingStep());

            // Add the input file (manifest file)
            XliffProcessor processor = new XliffProcessor(pack.getXlf());
            Locale sourceLanguage = processor.getSourceLanguage();
            Locale targetLanguage = processor.getTargetLanguage();
            RawDocument rawDoc = new RawDocument(pack.getManifest().toURI(),
                    "UTF-8",
                    new LocaleId(sourceLanguage),
                    new LocaleId(targetLanguage),
                    "okf_rainbowkit-noprompt");
            driver.addBatchItem(rawDoc);

            // Run the pipeline
            driver.processBatch();

            // Return the derived file
            return pack.getDerivedFile();
        }
        catch ( Throwable e ) {
            LOGGER.error("It was not possible to obtain the derived file from " + pack.getOriginalFile().getName(), e);
            throw new RuntimeException("It was not possible to obtain the derived file from " + pack.getOriginalFile().getName());
        }
    }

}