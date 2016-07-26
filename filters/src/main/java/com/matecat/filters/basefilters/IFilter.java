package com.matecat.filters.basefilters;

import com.matecat.converter.core.XliffProcessor;

import java.io.File;
import java.util.Locale;

public interface IFilter {
    boolean isSupported(File sourceFile);
    File extract(File sourceFile, Locale sourceLanguage, Locale targetLanguage, String segmentation);
    File merge(XliffProcessor xliff);
}
