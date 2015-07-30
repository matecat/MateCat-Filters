package com.matecat.converter.core.format.converters;

import com.matecat.converter.core.format.Format;
import com.matecat.converter.core.format.FormatNotSupportedException;
import com.matecat.converter.core.util.Configuration;

import java.io.IOException;
import java.util.*;

/**
 * LOC Converter
 *
 * Default and free format converter developed and hosted by the Matecat team.
 * It is implemented as a SocketFormatConverter, which connects to the (Windows) machine it is running in.
 *
 * It supports the following conversions:
 *  - doc -> docx
 *  - dot -> docx
 *  - rtf -> docx
 *  - ppt -> pptx
 *  - pot -> pptx
 *  - pps -> pptx
 *  - xls -> xlsx
 *  - xlt -> xlsx
 *  - pdf -> docx (with and without OCR processing)
 *  - png -> docx (OCR processing)
 *  - jpg -> docx (OCR processing)
 *  - tiff -> docx (OCR processing)
 *  - docx -> pdf
 *  - docx -> rtf
 */
public class LOCFormatConverter extends SocketFormatConverter {

    /*
     * Load the LOC Server configuration from the configuration file
     */
    private static final String HOST_PROPERTY = "loc-host";
    private static final String PORT_PROPERTY = "loc-port";
    private static final String HOST;
    private static final Integer PORT;
    static {
        try {
            HOST = Configuration.getProperty(HOST_PROPERTY);
            PORT = Integer.parseInt(Configuration.getProperty(PORT_PROPERTY));
            if (HOST == null  ||  HOST.equals("")  ||  PORT < 0)
                throw new IOException();
        }
        catch (IOException | NumberFormatException e) {
            throw new RuntimeException("LOC configuration is missing or corrupted");
        }
    }


    // Supported extensions
    private static List<Format> supportedFormats = Arrays.asList(
                Format.DOCX,   // 0
                Format.DOC,    // 1
                Format.XLS,    // 2
                Format.PPT,    // 3
                Format.DOT,    // 4
                Format.XLT,    // 5
                Format.POT,    // 6
                Format.PPS,    // 7
                Format.RTF,    // 8
                Format.PDF,    // 9
                Format.PNG,    // 10
                Format.JPG,    // 11
                Format.TIFF,   // 12
                Format.XLSX,   // 13
                Format.PPTX    // 14
        );


    // Conversions mapping
    private static Map<Format, Set<Format>> conversionsMapping;
    static {

        // Initialized temporal map
        Map<Format, Set<Format>> temp = new HashMap<>();
        supportedFormats.forEach(format -> temp.put(format, new LinkedHashSet<>()));

        // Add formats
        temp.get(Format.DOC).add(Format.DOCX);
        temp.get(Format.DOT).add(Format.DOCX);
        temp.get(Format.XLS).add(Format.XLSX);
        temp.get(Format.XLT).add(Format.XLSX);
        temp.get(Format.PPT).add(Format.PPTX);
        temp.get(Format.POT).add(Format.PPTX);
        temp.get(Format.PPS).add(Format.PPTX);
        temp.get(Format.RTF).add(Format.DOCX);
        temp.get(Format.PDF).add(Format.DOCX);
        temp.get(Format.JPG).add(Format.DOCX);
        temp.get(Format.PNG).add(Format.DOCX);
        temp.get(Format.TIFF).add(Format.DOCX);
        temp.get(Format.DOCX).add(Format.RTF);

        // Store an unmodifiable map
        conversionsMapping = Collections.unmodifiableMap(temp);
    }


    /**
     * LOC Converter constructor using the properties loaded before
     */
    public LOCFormatConverter() {
        super(HOST, PORT, true);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected int getFormatCode(Format format) {
        int code = supportedFormats.indexOf(format);
        if (code == -1)
            throw new FormatNotSupportedException(format);
        return code;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConvertible(Format inputFormat) {
        return supportedFormats.contains(inputFormat)
                &&  conversionsMapping.containsKey(inputFormat)
                &&  !conversionsMapping.get(inputFormat).isEmpty();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConvertible(Format inputFormat, Format outputFormat) {
        return isConvertible(inputFormat)
                &&  conversionsMapping.get(inputFormat).contains(outputFormat);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Format getPreferredConversion(Format inputFormat) {
        if (!isConvertible(inputFormat))
            throw new FormatNotSupportedException(inputFormat);
        return conversionsMapping.get(inputFormat).iterator().next();
    }

}