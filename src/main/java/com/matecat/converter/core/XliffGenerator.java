package com.matecat.converter.core;

import com.matecat.converter.core.encoding.Encoding;
import com.matecat.converter.core.encoding.ICUEncodingDetector;
import com.matecat.converter.core.encoding.IEncodingDetector;
import com.matecat.converter.core.format.Format;
import com.matecat.converter.core.format.converters.AbstractFormatConverter;
import com.matecat.converter.core.format.converters.loc.LOCFormatConverter;
import com.matecat.converter.core.okapiclient.OkapiClient;
import com.matecat.converter.core.okapiclient.OkapiPack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
    private List<AbstractFormatConverter> converters = new ArrayList<>();
    private IEncodingDetector encodingDetector = new ICUEncodingDetector();
    private OkapiClient okapiClient = new OkapiClient();


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

        // Init the converters
        initConverters();
    }


    /**
     * Init the converters
     */
    private void initConverters() {
        converters.add(new LOCFormatConverter());
    }


    /**
     * Generate the Xliff file from the stored inputs
     * @return Xliff file
     */
    public File generate() {

        // 0. Load the file
        Format originalFormat = Format.getFormat(this.file);
        File file = this.file;

        // 1. If the file it's not supported, convert it
        if (!OkapiClient.isSupported(originalFormat))
            file = convertFile(this.file);

        // 2. Detect the encoding
        Encoding encoding = encodingDetector.detect(file);

        // 3. Send to Okapi
        OkapiPack okapiPack = okapiClient.execute(sourceLanguage, targetLanguage, encoding, file);

        // 4. Generate and return the xliff
        return XliffBuilder.build(okapiPack, originalFormat);
    }


    /**
     * // TODO throw an exception if not?
     * If any of the converters can convert the file, do it
     * @param file File to convert if possible
     * @return Converted file if possible, original file otherwise
     */
    private File convertFile(File file) {

        // Input format
        Format inputFormat = Format.getFormat(file);

        // Check every converter converter
        for (AbstractFormatConverter converter : converters) {

            // If the converter can convert the file
            if (converter.isConvertible(inputFormat)) {

                // Obtain the preferred conversion and convert it
                Format outputFormat = converter.getDefaultConversion(inputFormat);
                return converter.convert(file, outputFormat);
            }
        }

        // Return the original file if it was not converted
        return file;

    }

}