package com.matecat.converter.core;

import java.io.File;
import java.util.Locale;

/**
 * Created by Alvaro on 16/07/2015.
 */
public class XliffGenerator {

    private Locale sourceLanguage;
    private Locale targetLanguage;
    private File file;

    public XliffGenerator(Locale sourceLanguage, Locale targetLanguage, File file) {
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.file = file;
    }

    public File generate() {
        // TODO
        return null;
    }
}
