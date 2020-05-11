package com.matecat.filters.basefilters;

import com.matecat.converter.core.Format;
import com.matecat.converter.core.XliffBuilder;
import com.matecat.converter.core.XliffProcessor;
import com.matecat.converter.core.okapiclient.OkapiClient;
import com.matecat.converter.core.okapiclient.OkapiPack;
import com.matecat.converter.core.util.Config;
import com.matecat.converter.core.winconverter.WinConverterClient;

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
        OkapiPack okapiPack = FiltersUtils.extractOkapiPack(sourceFile, sourceLanguage, targetLanguage, segmentation, null, false);

        return XliffBuilder.build(okapiPack, originalFormat, this.getClass());
    }

    @Override
    public File merge(XliffProcessor xliff) {
        return xliff.getDerivedFile();
    }
}
