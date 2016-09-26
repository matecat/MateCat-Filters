package com.matecat.converter.core.okapiclient;

import static com.matecat.converter.core.Format.SDLXLIFF;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import net.sf.okapi.common.LocaleId;
import net.sf.okapi.common.MimeTypeMapper;
import net.sf.okapi.common.Util;
import net.sf.okapi.common.filters.FilterConfiguration;
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
import net.sf.okapi.steps.segmentation.SegmentationStep;

import net.sf.okapi.whitespacecorrection.AddWhitespaceAfterKutenStep;
import net.sf.okapi.whitespacecorrection.RemoveWhitespaceAfterKutenStep;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.matecat.converter.core.XliffProcessor;
import com.matecat.converter.core.encoding.Encoding;
import com.matecat.converter.core.Format;
import com.matecat.converter.core.util.Config;
import com.matecat.converter.okapi.steps.segmentation.AddIcuHintsStep;
import com.matecat.converter.okapi.steps.segmentation.RemoveIcuHintsStep;


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
     * @param segmentation the name of the custom segmentation file to use (if any), or <code>null</code> to fallback on default
     * @param driver a reference to the current driver to be populated with the segmentation step
     * 
     * @see Config.customSegmentationFolder
     */
    private static void createSegmentationStep(Locale sourceLanguage, String segmentation, IPipelineDriver driver) {
        SegmentationStep segmentationStep = new SegmentationStep();
        net.sf.okapi.steps.segmentation.Parameters params = (net.sf.okapi.steps.segmentation.Parameters) segmentationStep.getParameters();
        
        String customSegmentationFilePath = getCustomSegmentationFilePath(segmentation);

        if( customSegmentationFilePath != null ) {
			params.setSourceSrxPath(customSegmentationFilePath);
			driver.addStep(segmentationStep);
        } else {
	        driver.addStep(new AddIcuHintsStep(sourceLanguage));
	        params.setSourceSrxPath(SRX_FILE.getPath());
	        driver.addStep(segmentationStep);
	        driver.addStep(new RemoveIcuHintsStep());
        }
    }
    
    
    /**
     * given a segmentation name, check if exists a corresponding file with custom segmentation rules and return its path 
     * @param segmentation
     * @return the full path of the file containing the specified segmentation rules, or null if no custom segmentation has been defined
     * @throws RuntimeException if the file does not exist or there are no read permissions on it 
     */
    private static String getCustomSegmentationFilePath(String segmentation) {
    	// no custom segmentation required
        if (segmentation == null || segmentation.trim().isEmpty()) {
        	LOGGER.info("Using default segmentation");
        	return null;
        }
        
		// the custom segmentation folder is empty, skip all checks and use default rules
		if( Config.customSegmentationFolder.isEmpty() ) {
			LOGGER.info("No custom segmentation folder, falling back to default segmentation");
			throw new IllegalStateException("Custom segmentation file requested, but no segmentation folder configured. File: " + Config.customSegmentationFolder + segmentation + ".srx");
		}
		
		
		/* A custom segmentation has been requested, and there is a valid custom segmentation rules folder.
		 * Try to get the proper file, if it is not found or not accessible, raise an exception for the client.
		 * That is why no exception handling has been defined here
		 */
				
		// instantiate a file wrapper to make all the necessary checks
		File segmentationFile = new File(Config.customSegmentationFolder + segmentation + ".srx");
		
		/* Check if the corresponding file exists
		 *  
		 * IMPORTANT
		 * Read permission in folder Config.customSegmentationFolder are check only once at startup time from Config class.
		 * If these permissions change at runtime and the application becomes not allowed to read from it, the following check will fail
		 * because it will try to read the folder content, and an empty set of files will be returned, due to the permission issue.
		 * 
		 *  Sample Scenario:
		 *   - application loads and at startup time is able to read from Config.customSegmentationFolder
		 *   - permission on the folder change, and the application cannot read from it anymore
		 *   - user issues a conversion request and the application tries to look for the specified segmentation file from custom folder
		 *   - because of read limitation, OS will return the application an empty set of files
		 *   - the application will correctly fallback on default srx file, but with following "not found" error message
		 *  
		 * Even though the behaviuor is correct (and this case should not happen), the error message might be trivial 
		 */ 
		if(!segmentationFile.isFile()) {
			LOGGER.warn("Custom segmentation file not found. File: " + Config.customSegmentationFolder + segmentation + ".srx");
			throw new IllegalArgumentException("Custom segmentation file not found. File: " + Config.customSegmentationFolder + segmentation + ".srx");
		}
	
		// Check if the corresponding file can be read
		if(!segmentationFile.canRead()) {
			LOGGER.warn("Custom segmentation file cannot be read. File: " + Config.customSegmentationFolder + segmentation + ".srx");
			throw new IllegalArgumentException("Custom segmentation file cannot be read. File: " + Config.customSegmentationFolder + segmentation + ".srx");
		}
	
		// the file exists and can be read, return its path
		LOGGER.info("Using custom segmentation in file: " + segmentationFile.getPath());
		return segmentationFile.getPath();	
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

        // These lines were used to handle situations where
        // MateCAT changed target language in the XLIFF, but
        // not in the included Okapi's Manifest file.
        // This caused Okapi look for target segments in the
        // language specified in the manifest and finding
        // nothing, because in the XLIFF there are only
        // segments in the MateCAT's project target lang.
        // Setting setForceTargetLocale solved the situation
        // for mismatches like "zh-CN" and "it-IT", but not for
        // "en-US" and "en-GB".
        // So I implemented a more radical workaround, in
        // XLliffProcessor::reconstructManifest.

        // net.sf.okapi.steps.rainbowkit.postprocess.Parameters params = (net.sf.okapi.steps.rainbowkit.postprocess.Parameters) mergingStep.getParameters();
        // params.setForceTargetLocale(true);

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
        fcMapper.addConfigurations(filter.getClass().getName());
        fcMapper.addConfigurations(RainbowKitFilter.class.getName());
        // Here you must add all the used subfilters configurations; this should
        // be improved: subfilters configurations should be saved in the manifest
        // and retrieved from there, not from the FilterConfigurationMapper
        fcMapper.addConfigurations(TableFilter.class.getName());
        // This is used by the XML filter
        fcMapper.addConfiguration(new FilterConfiguration(
                "okf_html-custom",
                MimeTypeMapper.HTML_MIME_TYPE,
                HtmlFilter.class.getName(),
                "HTML",
                "HTML customized for MateCat Filters",
                OkapiFilterFactory.OKAPI_CUSTOM_CONFIGS_PATH + OkapiFilterFactory.HTML_CONFIG_FILENAME,
                ".htm;.html;"
        ));

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
    public static OkapiPack generatePack(Locale sourceLanguage, Locale targetLanguage, Encoding encoding, File file, String segmentation, IFilter filter) {

        // Check inputs
        if (sourceLanguage == null)
            throw new IllegalArgumentException("The source language cannot be null");
        if (targetLanguage == null)
            throw new IllegalArgumentException("The target language cannot be null");
        if (encoding == null)
            throw new IllegalArgumentException("The input encoding cannot be null");
        if (file == null  ||  !file.exists()  ||  file.isDirectory())
            throw new IllegalArgumentException("The input file is not valid");

        final Format format = Format.getFormat(file);

        // Output pack folder
        File packFolder = new File(file.getParentFile().getPath() + File.separator + OkapiPack.PACK_FILENAME);

        // Create the pipeline driver
        IPipelineDriver driver = createOkapiPipelineDriver(file.getParent());

        // Filtering step
        RawDocumentToFilterEventsStep filteringStep = new RawDocumentToFilterEventsStep();
        if (filter == null) {
            filter = OkapiFilterFactory.getFilter(file);
        }
        filteringStep.setFilter(filter);
        driver.addStep(filteringStep);

        // Set the filter configuration map to use with the driver
        driver.setFilterConfigurationMapper(createFilterConfigurationMapper(filter));

        // Very often, PO files carry already some translated segments inside.
        // If we segment the sources, how can we obtain the corresponding segments
        // in the translated contents? We can't. The structure of PO files makes
        // already segmented, so it's better to not segment further.
        // XLIFFs instead are already segmented, and segmenting them further causes
        // strange outputs.
        if (format != Format.PO && format != Format.XLF && format != Format.XLIFF && format != SDLXLIFF) {
        	createSegmentationStep(sourceLanguage, segmentation, driver);
        }

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
        // Warning: output encoding must be ALWAYS UTF-8
        BatchItemContext item = new BatchItemContext(rawDoc, outputFile.toURI(), StandardCharsets.UTF_8.toString());
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
                            OkapiFilterFactory.getFilter(pack.getOriginalFile())));

            // Add the extraction step
            driver.addStep(new RawDocumentToFilterEventsStep());

            XliffProcessor processor = new XliffProcessor(pack.getXlf());
            LocaleId sourceLanguage = new LocaleId(processor.getSourceLanguage());
            LocaleId targetLanguage = new LocaleId(processor.getTargetLanguage());

            // Kuten (that is the char "。") is the "period" for the asian languages.
            // This character has an embedded trailing space, and this causes
            // problems when it is converted to/from a regular period + space, i.e. ". "
            boolean sourceLanguageHasKuten = LocaleId.JAPANESE.sameLanguageAs(sourceLanguage) || LocaleId.CHINA_CHINESE.sameLanguageAs(sourceLanguage);
            boolean targetLanguageHasKuten = LocaleId.JAPANESE.sameLanguageAs(targetLanguage) || LocaleId.CHINA_CHINESE.sameLanguageAs(targetLanguage);

            if (sourceLanguageHasKuten && !targetLanguageHasKuten) {
                // If source language has kutens but target language has not,
                // all the target segments ending with just a period will have
                // no space after them, and will be merged all "glued" together.
                // To compensate, add an ending space to each target segment
                // which source ends with a kuten.
                driver.addStep(new AddWhitespaceAfterKutenStep());
            }

            if (targetLanguageHasKuten && !sourceLanguageHasKuten) {
                // If target language has kutens but source has not, the merged
                // output will contain many extra spaces. This because CATs hide
                // trailing spaces after ending periods, but put them back in
                // in place in the XLIFF.
                // To compensate, replace any kuten + space with just a kuten in
                // the target segments.
                driver.addStep(new RemoveWhitespaceAfterKutenStep());
            }

            // Add the t-kit merging step
            driver.addStep(createMergingStep());

            // Add the input file (manifest file)
            RawDocument rawDoc = new RawDocument(pack.getManifest().toURI(),
                    "UTF-8", sourceLanguage, targetLanguage,
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