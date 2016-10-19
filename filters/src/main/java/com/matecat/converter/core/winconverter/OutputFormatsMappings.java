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
        map.put(Format.DOT, Format.DOCM);
        map.put(Format.DOTX, Format.DOCM);
        map.put(Format.RTF, Format.DOCM);

        map.put(Format.XLS, Format.XLSM);
        map.put(Format.XLT, Format.XLSM);
        map.put(Format.XLTX, Format.XLSM);

        map.put(Format.PPT, Format.PPTM);
        map.put(Format.PPS, Format.PPTM);
        map.put(Format.PPTX, Format.PPTM);
        map.put(Format.POT, Format.PPTM);
        map.put(Format.PPTX, Format.PPTM);
        map.put(Format.PPSX, Format.PPTM);
        map.put(Format.POTX, Format.PPTM);

        map.put(Format.PDF, Format.DOCX);
        map.put(Format.BMP, Format.DOCX);
        map.put(Format.GIF, Format.DOCX);
        map.put(Format.PNG, Format.DOCX);
        map.put(Format.JPEG, Format.DOCX);
        map.put(Format.TIFF, Format.DOCX);
    }
}
