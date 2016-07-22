package com.matecat.converter.core;

import com.matecat.converter.core.encoding.Encoding;
import com.matecat.converter.core.encoding.EncodingDetectorRouter;
import com.matecat.converter.core.encoding.IEncodingDetector;
import com.matecat.converter.core.util.Config;
import com.matecat.converter.core.winconverter.WinConverterRouter;
import com.matecat.converter.core.okapiclient.OkapiClient;
import com.matecat.converter.core.okapiclient.OkapiPack;
import com.matecat.filters.basefilters.DefaultFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Locale;

/**
 * Xliff Generator
 * This is the main class of the converter's core. It is used to generate an Xliff.
 */
public class XliffGenerator {

    // Logger
    private static Logger LOGGER = LoggerFactory.getLogger(XliffGenerator.class);

    // Required properties
    private Locale sourceLanguage;
    private Locale targetLanguage;
    private String segmentation;
    private File file;

    // Other module's instances
    private IEncodingDetector encodingDetector = new EncodingDetectorRouter();


    /**
     * Class constructor
     * @param sourceLanguage Source language
     * @param targetLanguage Target language
     * @param file File we want to convert into a Xliff
     */
    public XliffGenerator(Locale sourceLanguage, Locale targetLanguage, File file, String segmentation) {

        // Check that any of the inputs is null
        if (sourceLanguage == null)
            throw new IllegalArgumentException("The source language cannot be null");
        if (targetLanguage == null)
            throw new IllegalArgumentException("The target language cannot be null");
        if (file == null  ||  !file.exists()  ||  file.isDirectory())
            throw new IllegalArgumentException("The input file is not valid");

        // Store the properties
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.segmentation = segmentation;
        this.file = file;
    }

    
	/**
     * Generate the Xliff file from the stored inputs
     * @return Xliff file
     */
    public File generate() throws Exception {

        // 0. Load the file
        Format originalFormat = Format.getFormat(this.file);
        File file = this.file;

        // 1. If the file it's not supported, convert it
        if (Config.winConvEnabled && !OkapiClient.isSupported(originalFormat)) {
            file = WinConverterRouter.convert(this.file);
        }

        // 2. Detect the encoding
        Encoding encoding = encodingDetector.detect(file);

        // 3. Send to Okapi
        OkapiPack okapiPack = OkapiClient.generatePack(sourceLanguage, targetLanguage, encoding, file, segmentation, null);

        // 4. Generate and return the xliff
        return XliffBuilder.build(okapiPack, originalFormat, DefaultFilter.class);
    }

}