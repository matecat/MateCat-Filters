package com.matecat.filters.basefilters;

import com.matecat.converter.core.Format;
import com.matecat.converter.core.encoding.Encoding;
import com.matecat.converter.core.encoding.EncodingDetectorRouter;
import com.matecat.converter.core.okapiclient.OkapiClient;
import com.matecat.converter.core.okapiclient.OkapiPack;
import com.matecat.converter.core.util.Config;
import com.matecat.converter.core.winconverter.WinConverterRouter;

import java.io.File;
import java.util.Locale;

/**
 * Utility methods for {@link IFilter} implementations.
 */
public final class FiltersUtils {

    // prevent instantiation
    private FiltersUtils() {
    }

    /**
     * Extract the Okapi pack from the given XLIFF file.
     * <p>
     * This method is used by {@link DefaultFilter} and by custom filters.
     *
     * @param sourceFile       XLIFF file
     * @param sourceLanguage   source language {@code Locale}
     * @param targetLanguage   target  language {@code Locale}
     * @param segmentation     segmentation
     * @param okapiFilter      Okapi filter object
     * @param segmentBilingual bilingual flag
     * @return an {@code OkapiPack} object
     */
    public static OkapiPack extractOkapiPack(File sourceFile, Locale sourceLanguage, Locale targetLanguage, String segmentation, net.sf.okapi.common.filters.IFilter okapiFilter, Boolean segmentBilingual) {
        Format originalFormat = Format.getFormat(sourceFile);

        // 1. If the file it's not supported, convert it
        if (Config.winConvEnabled && !OkapiClient.isSupported(originalFormat)) {
            try {
                sourceFile = WinConverterRouter.convert(sourceFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // 2. Detect the encoding
        Encoding encoding = new EncodingDetectorRouter().detect(sourceFile);

        // 3. Send to Okapi
        return OkapiClient.generatePack(sourceLanguage, targetLanguage, encoding, sourceFile, segmentation, okapiFilter, segmentBilingual);
    }
}
