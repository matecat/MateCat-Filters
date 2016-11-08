package com.matecat.converter.core.winconverter;

import com.matecat.converter.core.Format;

import java.util.HashMap;
import java.util.Map;

/**
 * This class stores the default output format for each supported source format
 */
public class OutputFormatsMappings {

    public final static Map<Format, Format> map;

    static {
        map = new HashMap<>();

        map.put(Format.DOC, Format.DOCM);
        map.put(Format.DOT, Format.DOTM);
        map.put(Format.RTF, Format.DOCX);

        map.put(Format.XLS, Format.XLSM);
        map.put(Format.XLT, Format.XLTM);

        map.put(Format.PPT, Format.PPTM);
        map.put(Format.PPS, Format.PPSM);
        map.put(Format.POT, Format.POTM);

        map.put(Format.PDF, Format.DOCX);
        map.put(Format.BMP, Format.DOCX);
        map.put(Format.GIF, Format.DOCX);
        map.put(Format.PNG, Format.DOCX);
        map.put(Format.JPEG, Format.DOCX);
        map.put(Format.TIFF, Format.DOCX);
    }
}
