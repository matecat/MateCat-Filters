package com.matecat.filters.basefilters;

import com.matecat.converter.core.Format;
import com.matecat.converter.core.XliffBuilder;
import com.matecat.converter.core.XliffProcessor;
import com.matecat.converter.core.encoding.Encoding;
import com.matecat.converter.core.encoding.EncodingDetectorRouter;
import com.matecat.converter.core.okapiclient.OkapiClient;
import com.matecat.converter.core.okapiclient.OkapiPack;
import com.matecat.converter.core.util.Config;
import com.matecat.converter.core.winconverter.WinConverterClient;
import com.matecat.converter.core.winconverter.WinConverterRouter;

import java.io.File;
import java.util.Locale;

public class DefaultFilter implements IFilter {
    @Override
    public boolean isSupported(File sourceFile) {
        Format originalFormat = Format.getFormat(sourceFile);
        boolean supportedByOkapi = OkapiClient.isSupported(originalFormat);
        if (supportedByOkapi) {
            return true;
        }
        else if (Config.winConvEnabled) {
            return WinConverterClient.supportedFormats.contains(originalFormat);
        } else {
            return false;
        }
    }

    @Override
    public File extract(File sourceFile, Locale sourceLanguage, Locale targetLanguage, String segmentation) {
        Format originalFormat = Format.getFormat(sourceFile);
        OkapiPack okapiPack = extractOkapiPack(sourceFile, sourceLanguage, targetLanguage, segmentation, null, false);

        return XliffBuilder.build(okapiPack, originalFormat, this.getClass());
    }

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

    @Override
    public File merge(XliffProcessor xliff) {
        return xliff.getDerivedFile();
    }
}
