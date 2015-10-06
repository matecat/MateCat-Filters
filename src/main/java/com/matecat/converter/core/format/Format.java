package com.matecat.converter.core.format;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Stream;


/**
 * Format class, which represents the supported formats by the application.
 * It also offers some util functions regarding format handling.
 */
public enum Format {

    // Suported formats
    DOC, DOT, DOCX, DOCM, DOTX, DOTM, RTF,
    XLS, XLT, XLSX, XLSM, XLTX, XLTM, ODS, SXC, CSV,
    PPT, PPS, POT, PPTX, PPTM, PPSM, PPSX, POTX, POTM,
    ODP, OTP, SXI, XML,
    ODT, OTT, OTS, SXW, TXT, PDF,
    BMP, GIF, PNG, JPEG, TIFF,
    HTML, HTM, XHTML, PHP, JSON, TXML, YAML, RKM,
    XLIFF, SDLXLIFF, TMX, TTX, ITD, XLF,
    MIF, INX, IDML, ICML, XTG, TAG, DITA,
    PROPERTIES, RC, RESX, SGM, SGML, STRINGS, PO, XLW, XLSB,
    DTD, SRT, TSV, WIX,
    ARCHIVE, XINI, TS;

    // Generate a dictionary mapping the extension to its enum constant
    private static final Map<String, Format> supportedFormats;
    static {
        supportedFormats = new HashMap<>();
        Stream.of(Format.values())
                .forEach(format -> {
                    String ext = format.toString();
                    supportedFormats.put(ext, format);
                });
    }

    // Plain text formats
    private static final Set<Format> textFormats;
    static {
        textFormats = new HashSet<>(Arrays.asList(
                TXT, CSV, XML, HTML, HTM, XHTML, PHP, JSON, TXML, YAML, XLIFF,
                SDLXLIFF, DITA, IDML, RESX, STRINGS, PO, ARCHIVE, PROPERTIES, DTD, SRT, TSV, WIX
        ));
    }


    /**
     * Check whether a format is plain text or not
     * @param format Format
     * @return True if plain text, false otherwise
     */
    public static boolean isPlainTextFormat(Format format) {
        return textFormats.contains(format);
    }


    /**
     * Parse the extension and return the format
     * @param extension Extension (with or without dot)
     * @return Format
     */
    public static Format parse(String extension) {

        // Remove the dot, if it exists
        extension = extension.replace('.', '\0').toLowerCase();

        // Return corresponding format
        // TODO: extension abbreviations should be handled better
        Format format = null;
        switch (extension) {
            case "jpg":
                format = Format.JPEG;
                break;
            case "tif":
                format = Format.TIFF;
                break;
            case "yml":
                format = Format.YAML;
                break;
            default:
                format = supportedFormats.getOrDefault(extension, null);
        }

        // If it is not supported, throw an exception
        if (format == null)
            throw new FormatNotSupportedException(extension);

        // Else, return it
        return format;

    }

    /**
     * Obtain the format of a given filename
     * @param filename Filename we want to know the format
     * @return File's format
     * @throws FormatNotSupportedException If the file's format is not supported
     */
    public static Format getFormat(String filename) {
        return parse(FilenameUtils.getExtension(filename));
    }


    /**
     * Obtain the format of a given file
     * @param file File we want to know the format
     * @return File's format
     * @throws FormatNotSupportedException If the file's format is not supported
     */
    public static Format getFormat(File file) {
        return parse(FilenameUtils.getExtension(file.getName()));
    }


    /**
     * To string returning the extension of the format (without dot)
     * @return Format's extension
     */
    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
