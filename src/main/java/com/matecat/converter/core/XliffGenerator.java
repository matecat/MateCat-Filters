package com.matecat.converter.core;

import com.matecat.converter.core.encoding.Encoding;
import com.matecat.converter.core.encoding.ICUEncodingDetector;
import com.matecat.converter.core.encoding.IEncodingDetector;
import com.matecat.converter.core.format.Format;
import com.matecat.converter.core.format.FormatNotSupportedException;
import com.matecat.converter.core.format.converters.Converters;
import com.matecat.converter.core.okapiclient.OkapiClient;
import com.matecat.converter.core.okapiclient.OkapiPack;

import java.io.File;
import java.util.Locale;

/**
 * Xliff Generator
 * This is the main class of the converter's core. It is used to generate an Xliff.
 */
public class XliffGenerator {

    // Required properties
    private Locale sourceLanguage;
    private Locale targetLanguage;
    private File file;

    // Other module's instances
    private IEncodingDetector encodingDetector = new ICUEncodingDetector();
    private Converters converters = new Converters();


    /**
     * Class constructor
     * @param sourceLanguage Source language
     * @param targetLanguage Target language
     * @param file File we want to convert into a Xliff
     */
    public XliffGenerator(Locale sourceLanguage, Locale targetLanguage, File file) {

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
        this.file = file;

    }


    /**
     * Generate the Xliff file from the stored inputs
     * @throws FormatNotSupportedException if the file is not supported and cannot be converted to supported file
     * @return Xliff file
     */
    public File generate() {

        // 0. Load the file
        Format originalFormat = Format.getFormat(this.file);
        File file = this.file;

        // 1. If the file it's not supported, convert it
        if (!OkapiClient.isSupported(originalFormat))
            file = converters.convert(this.file, converters.getPreferredConversion(originalFormat));

        // 2. Detect the encoding
        Encoding encoding = encodingDetector.detect(file);

        // 3. Send to Okapi
        OkapiPack okapiPack = OkapiClient.generatePack(sourceLanguage, targetLanguage, encoding, file);

        // 4. Generate and return the xliff
        return XliffBuilder.build(okapiPack, originalFormat);
    }

}