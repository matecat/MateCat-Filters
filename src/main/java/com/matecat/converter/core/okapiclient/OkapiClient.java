package com.matecat.converter.core.okapiclient;

import com.matecat.converter.core.XliffProcessor;
import com.matecat.converter.core.encoding.Encoding;
import com.matecat.converter.core.format.Format;
import net.sf.okapi.common.LocaleId;
import net.sf.okapi.common.Util;
import net.sf.okapi.common.filters.FilterConfigurationMapper;
import net.sf.okapi.common.filters.IFilter;
import net.sf.okapi.common.filters.IFilterConfigurationMapper;
import net.sf.okapi.common.pipelinedriver.BatchItemContext;
import net.sf.okapi.common.pipelinedriver.IPipelineDriver;
import net.sf.okapi.common.pipelinedriver.PipelineDriver;
import net.sf.okapi.common.resource.RawDocument;
import net.sf.okapi.filters.rainbowkit.RainbowKitFilter;
import net.sf.okapi.steps.common.RawDocumentToFilterEventsStep;
import net.sf.okapi.steps.rainbowkit.creation.ExtractionStep;
import net.sf.okapi.steps.rainbowkit.postprocess.MergingStep;
import net.sf.okapi.steps.segmentation.SegmentationStep;

import java.io.File;
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

    /**
     * Private constructor (static class)
     */
    private OkapiClient() {}

    /* SEGMENTATION RULES */

    /**
     * Initialization of the default segmentation rules and its folder
     */
    public static final String SEGMENTATION_FOLDER_NAME = "okapi" + File.separator + "segmentation";
    public static final String DEFAULT_SEGMENTATION_FILENAME = "default.srx";
    private static final File segmentationFolder, defaultSegmentation;
    static {
        // Load segmentation folder
        URL segmentationFolderURL =  OkapiClient.class.getResource(File.separator + SEGMENTATION_FOLDER_NAME);
        if (segmentationFolderURL == null)
            throw new RuntimeException("The folder containing the segmentation rules was not found (is it installed?)");
        segmentationFolder = new File(segmentationFolderURL.getPath());

        // Load default segmentation rules
        defaultSegmentation =  new File(segmentationFolder.getPath() + File.separator + DEFAULT_SEGMENTATION_FILENAME);
        if (!defaultSegmentation.exists())
            throw new RuntimeException(String.format("The default segmentation rules '%s' are not installed", DEFAULT_SEGMENTATION_FILENAME));
    }

    /**
     * Segmentation rules cache
     */
    private static ConcurrentMap<Locale, File> segmentationRules = new ConcurrentHashMap<>();


    /**
     * Get a segmentation rules file given the language
     *
     * If it exists one file whose filename matches the language, it will return it.
     * Otherwise, it will return the default segmentation file.
     *
     * @param language Source language
     * @return Segmentation file
     */
    private static File getSegmentationFile(Locale language) {

        // Obtain from buffer
        if (segmentationRules.containsKey(language))
            return segmentationRules.get(language);

        // If not, try to load a custom segmentation. If it does not exist, use the default one.
        File rules = getSegmentationFile(language.getLanguage() + ".srx");
        if (rules == null)
            rules = defaultSegmentation;
        segmentationRules.put(language, rules);
        return rules;

    }

    /**
     * Get the segmentation rules, given it's filename
     * @param filename Filename
     * @return Segmentation rules file, null if not found
     */
    private static File getSegmentationFile(String filename) {
        File out = new File(segmentationFolder.getPath() + File.separator + filename.toLowerCase());
        if (!out.exists())
            return null;
        return out;
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

        // Create a filter configuration map
        IFilterConfigurationMapper fcMapper = new FilterConfigurationMapper();
        fcMapper.addConfigurations(RainbowKitFilter.class.getName());
        //fcMapper.addConfigurations(OpenXMLFilter.class.getName());
        //fcMapper.addConfigurations(HtmlFilter.class.getName());

        // Set the filter configuration map to use with the driver
        driver.setFilterConfigurationMapper(fcMapper);

        // Configure root
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
        File segmentationRules = getSegmentationFile(sourceLanguage);
        SegmentationStep segmentationStep = new SegmentationStep();
        net.sf.okapi.steps.segmentation.Parameters params = (net.sf.okapi.steps.segmentation.Parameters) segmentationStep.getParameters();
        params.setSourceSrxPath(segmentationRules.getPath());
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

        // Segmentation step
        driver.addStep(createSegmentationStep(sourceLanguage));

        // Kit creation step
        driver.addStep(createExtractionStep());

        // Add the input file to the driver
        RawDocument rawDoc = new RawDocument(file.toURI(), encoding.getCode(), new LocaleId(sourceLanguage), new LocaleId(targetLanguage));

        // Output file (useless but needed)
        String basename = Util.getFilename(file.getPath(), false);
        String outputPath = packFolder.getPath() + File.separator + basename + ".out" + Util.getExtension(file.getPath());
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

            String root = pack.getPackFolder().getParent();
            IPipelineDriver driver = createOkapiPipelineDriver(root);

            // Add the extraction step
            driver.addStep(new RawDocumentToFilterEventsStep());

            // Add the t-kit merging step
            driver.addStep(new MergingStep());

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

            // Ensure that the derived file has been created (if not, an exception will be thrown by the pack
            File derivedFile = pack.getDerivedFile();

            // Return it
            return derivedFile;
        }
        catch ( Throwable e ) {
            throw new RuntimeException("It was not possible to obtaint he derived file from " + pack.getOriginalFile().getName());
        }
    }

}