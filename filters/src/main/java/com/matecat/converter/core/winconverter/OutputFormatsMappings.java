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

        map.put(Format.DOC, Format.DOCX);
        map.put(Format.DOCM, Format.DOCX);
        map.put(Format.DOT, Format.DOCX);
        map.put(Format.DOTX, Format.DOCX);
        map.put(Format.DOTM, Format.DOCX);
        map.put(Format.RTF, Format.DOCX);

        map.put(Format.XLS, Format.XLSX);
        map.put(Format.XLSM, Format.XLSX);
        map.put(Format.XLT, Format.XLSX);
        map.put(Format.XLTX, Format.XLSX);
        map.put(Format.XLTM, Format.XLSX);

        map.put(Format.PPT, Format.PPTX);
        map.put(Format.PPTM, Format.PPTX);
        map.put(Format.PPS, Format.PPTX);
        map.put(Format.PPTX, Format.PPS);
        map.put(Format.POT, Format.PPTX);
        map.put(Format.PPTX, Format.POT);
        map.put(Format.PPTX, Format.PPTM);
        map.put(Format.PPSX, Format.PPTX);
        map.put(Format.PPTX, Format.PPSX);
        map.put(Format.PPSM, Format.PPTX);
        map.put(Format.PPTX, Format.PPSM);
        map.put(Format.POTX, Format.PPTX);
        map.put(Format.PPTX, Format.POTX);
        map.put(Format.POTM, Format.PPTX);
        map.put(Format.PPTX, Format.POTM);

        map.put(Format.PDF, Format.DOCX);
        map.put(Format.BMP, Format.DOCX);
        map.put(Format.GIF, Format.DOCX);
        map.put(Format.PNG, Format.DOCX);
        map.put(Format.JPEG, Format.DOCX);
        map.put(Format.TIFF, Format.DOCX);
    }
}
