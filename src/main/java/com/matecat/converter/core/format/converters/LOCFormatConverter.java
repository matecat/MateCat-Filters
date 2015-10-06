package com.matecat.converter.core.format.converters;

import com.matecat.converter.core.format.Format;
import com.matecat.converter.core.format.FormatNotSupportedException;
import com.matecat.converter.core.util.Config;

import java.io.IOException;
import java.util.*;

/**
 * LOC Converter
 *
 * Default and free format converter developed and hosted by the Matecat team.
 * It is implemented as a SocketFormatConverter, which connects to the (Windows) machine it is running in.
 */
public class LOCFormatConverter extends SocketFormatConverter {

    /*
     * Load the LOC Server configuration from the configuration file
     */
    private static final String HOST;
    private static final Integer PORT;
    static {
        try {
            HOST = Config.locHost;
            PORT = Config.locPort;
            if (HOST == null  ||  HOST.equals("")  ||  PORT < 0)
                throw new IOException();
        }
        catch (IOException | NumberFormatException e) {
            throw new RuntimeException("LOC configuration is missing or corrupted");
        }
    }


    // Supported extensions
    private static List<Format> supportedFormats = Arrays.asList(

            // Word formats

            Format.DOC,   // 0
            Format.DOT,   // 1

            Format.DOCX,
            Format.DOCM,

            Format.DOTX,
            Format.DOTM,

            Format.RTF,

            // Excel formats

            Format.XLS,
            Format.XLT,

            Format.XLSX,
            Format.XLSM,

            Format.XLTX,
            Format.XLTM,

            // Powerpoint formats

            Format.PPT,
            Format.PPS,
            Format.POT,

            Format.PPTX,
            Format.PPTM,

            Format.PPSX,
            Format.PPSM,

            Format.POTX,
            Format.POTM,

            // PDF & OCR formats

            Format.PDF,
            Format.BMP,
            Format.GIF,
            Format.PNG,
            Format.JPEG,
            Format.TIFF
        );


    // Conversions mapping
    private static Map<Format, Set<Format>> conversionsMapping;
    static {

        // Initialized temporal map
        Map<Format, Set<Format>> temp = new HashMap<>();
        supportedFormats.forEach(format -> temp.put(format, new LinkedHashSet<>()));

        // Add formats
        temp.get(Format.DOC).add(Format.DOCX);
        temp.get(Format.DOCX).add(Format.DOC);
        temp.get(Format.DOT).add(Format.DOCX);
        temp.get(Format.DOCX).add(Format.DOT);
        temp.get(Format.DOCM).add(Format.DOCX);
        temp.get(Format.DOCX).add(Format.DOCM);
        temp.get(Format.DOTX).add(Format.DOCX);
        temp.get(Format.DOCX).add(Format.DOTX);
        temp.get(Format.DOTM).add(Format.DOCX);
        temp.get(Format.DOCX).add(Format.DOTM);
        temp.get(Format.RTF).add(Format.DOCX);
        temp.get(Format.DOCX).add(Format.RTF);

        temp.get(Format.XLS).add(Format.XLSX);
        temp.get(Format.XLSX).add(Format.XLS);
        temp.get(Format.XLT).add(Format.XLSX);
        temp.get(Format.XLSX).add(Format.XLT);
        temp.get(Format.XLSM).add(Format.XLSX);
        temp.get(Format.XLSX).add(Format.XLSM);
        temp.get(Format.XLTX).add(Format.XLSX);
        temp.get(Format.XLSX).add(Format.XLTX);
        temp.get(Format.XLTM).add(Format.XLSX);
        temp.get(Format.XLSX).add(Format.XLTM);

        temp.get(Format.PPT).add(Format.PPTX);
        temp.get(Format.PPTX).add(Format.PPT);
        temp.get(Format.PPS).add(Format.PPTX);
        temp.get(Format.PPTX).add(Format.PPS);
        temp.get(Format.POT).add(Format.PPTX);
        temp.get(Format.PPTX).add(Format.POT);
        temp.get(Format.PPTM).add(Format.PPTX);
        temp.get(Format.PPTX).add(Format.PPTM);
        temp.get(Format.PPSX).add(Format.PPTX);
        temp.get(Format.PPTX).add(Format.PPSX);
        temp.get(Format.PPSM).add(Format.PPTX);
        temp.get(Format.PPTX).add(Format.PPSM);
        temp.get(Format.POTX).add(Format.PPTX);
        temp.get(Format.PPTX).add(Format.POTX);
        temp.get(Format.POTM).add(Format.PPTX);
        temp.get(Format.PPTX).add(Format.POTM);

        temp.get(Format.PDF).add(Format.DOCX);
        temp.get(Format.BMP).add(Format.DOCX);
        temp.get(Format.GIF).add(Format.DOCX);
        temp.get(Format.PNG).add(Format.DOCX);
        temp.get(Format.JPEG).add(Format.DOCX);
        temp.get(Format.TIFF).add(Format.DOCX);

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