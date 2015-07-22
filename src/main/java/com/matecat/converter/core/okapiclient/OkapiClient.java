package com.matecat.converter.core.okapiclient;

import com.matecat.converter.core.encoding.Encoding;
import net.sf.okapi.common.LocaleId;
import net.sf.okapi.common.Util;
import net.sf.okapi.common.filters.FilterConfigurationMapper;
import net.sf.okapi.common.filters.IFilter;
import net.sf.okapi.common.filters.IFilterConfigurationMapper;
import net.sf.okapi.common.pipeline.IPipelineStep;
import net.sf.okapi.common.pipelinedriver.BatchItemContext;
import net.sf.okapi.common.pipelinedriver.IPipelineDriver;
import net.sf.okapi.common.pipelinedriver.PipelineDriver;
import net.sf.okapi.common.resource.RawDocument;
import net.sf.okapi.filters.openxml.OpenXMLFilter;
import net.sf.okapi.filters.plaintext.PlainTextFilter;
import net.sf.okapi.filters.rainbowkit.RainbowKitFilter;
import net.sf.okapi.steps.common.RawDocumentToFilterEventsStep;
import net.sf.okapi.steps.rainbowkit.creation.ExtractionStep;
import net.sf.okapi.steps.segmentation.SegmentationStep;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.matecat.converter.core.format.Format.*;

/**
 * Created by Reneses on 7/20/15.
 */
public class OkapiClient {

    private static Map<Locale, File> segmentationRules = new HashMap<>();
    private static final File defaultSegmentation;
    static {
        URL path = OkapiClient.class.getResource(File.separator + "segmentation" + File.separator + "default.srx");
        if (path == null)
            throw new RuntimeException("It was not possible to load the SRX rules");
        defaultSegmentation = new File(path.getPath());
    }


    private File getSegmentationFile(String filename) {
        URL path = getClass().getResource(File.separator + "segmentation" + File.separator + filename.toLowerCase() + ".srx");
        if (path == null)
            return null;
        return new File(path.getPath());
    }


    private File getSegmentationFile(Locale language) {

        // Obtain from buffer
        if (segmentationRules.containsKey(language))
            return segmentationRules.get(language);

        // If not, try to load a custom segmentation. If it does not exist, use the default one.
        File rules = getSegmentationFile(language.getLanguage());
        if (rules == null)
            rules = defaultSegmentation;
        segmentationRules.put(language, rules);

        // Return it
        return rules;

    }


    static IPipelineDriver createOkapiPipelineDriver() {

        // Create the pipeline driver
        IPipelineDriver driver = new PipelineDriver();

        // Create a filter configuration map
        IFilterConfigurationMapper fcMapper = new FilterConfigurationMapper();
        fcMapper.addConfigurations(RainbowKitFilter.class.getName());
        fcMapper.addConfigurations(OpenXMLFilter.class.getName());
        fcMapper.addConfigurations(PlainTextFilter.class.getName());

        // Set the filter configuration map to use with the driver
        driver.setFilterConfigurationMapper(fcMapper);

        // Return the driver
        return driver;
    }


    public OkapiPack execute(Locale sourceLanguage, Locale targetLanguage, Encoding encoding, File file) {

        // Check inputs
        if (sourceLanguage == null)
            throw new IllegalArgumentException("The source language cannot be null");
        if (targetLanguage == null)
            throw new IllegalArgumentException("The target language cannot be null");
        if (encoding == null)
            throw new IllegalArgumentException("The input encoding cannot be null");
        if (file == null  ||  !file.exists()  ||  file.isDirectory())
            throw new IllegalArgumentException("The input file is not valid");

        // Do



        String basename = Util.getFilename(file.getPath(), false);

        File packFolder = new File(file.getParentFile().getPath() + File.separator + "pack");

        // Create the pipeline driver
        // Create the pipeline driver
        IPipelineDriver driver = createOkapiPipelineDriver();

        // Choose filter
        IFilter filter = OkapiFilterFactory.getFilter(getFormat(file));

        // Filtering step
        RawDocumentToFilterEventsStep filteringStep = new RawDocumentToFilterEventsStep();
        filteringStep.setFilter(filter);
        driver.addStep(filteringStep);

        // Segmentation
        File segmentationRules = getSegmentationFile(sourceLanguage);
        SegmentationStep segmentationStep = new SegmentationStep();
        net.sf.okapi.steps.segmentation.Parameters params = (net.sf.okapi.steps.segmentation.Parameters) segmentationStep.getParameters();
        params.setSourceSrxPath(segmentationRules.getPath());
        segmentationStep.setParameters(params);
        driver.addStep(segmentationStep);

        // Create and set up the t-kit creation step
        IPipelineStep extStep = new ExtractionStep();
        net.sf.okapi.steps.rainbowkit.creation.Parameters extParams = (net.sf.okapi.steps.rainbowkit.creation.Parameters) extStep.getParameters();
        extParams.setPackageName(packFolder.getName());

        // params.setWriterClass("net.sf.okapi.steps.rainbowkit.xliff.XLIFF2PackageWriter"); // Xliff 2.0
        extStep.setParameters(extParams);
        driver.addStep(extStep);

        // Set the root folder for the driver's context
        String root = file.getParent();
        driver.setRootDirectories(root, root);
        driver.setOutputDirectory(root);

        // Add the input file to the driver
        //RawDocument rawDoc = new RawDocument(file.toURI(), encoding.getCode(), new LocaleId(sourceLanguage), new LocaleId(targetLanguage), "okf_openxml");
        RawDocument rawDoc = new RawDocument(file.toURI(), encoding.getCode(), new LocaleId(sourceLanguage), new LocaleId(targetLanguage));



        String outputPath = file.getParentFile().getPath() + File.separator + basename + ".out" + Util.getExtension(file.getPath());
        //        Util.getDirectoryName(path) + File.separator + Util.getFilename(path, false) + ".out" + ;
        File outputFile = new File(outputPath);

        // Create the batch item to process and add it to the driver
        BatchItemContext item = new BatchItemContext(rawDoc, outputFile.toURI(), encoding.getCode());
        driver.addBatchItem(item);

        // Run the pipeline
        driver.processBatch();

        // Check that it has been created
        if (!packFolder.exists())
            throw new RuntimeException("The pack could not be created");

        // Return pack
        return new OkapiPack(packFolder);
    }

}
