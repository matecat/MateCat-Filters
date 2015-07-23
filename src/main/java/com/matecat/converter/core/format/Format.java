package com.matecat.converter.core.format;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


/**
 * Format class, which represents the supported formats by the application.
 * It also offers some util functions regarding format handling.
 */
public enum Format {

    // Suported formats
    DOCX, DOC, DOTX, DOT, DOCM, DOTM, RTF, ODT, SXW, TXT, PDF,
    XLSX, XLS, XLTX, XLT, XLSM, ODS, SXC, CSV,
    PPTX, PPT, PPSX, PPS, PPSM, PPTM, POTX, POT, POTM, ODP, SXI, XML,
    PNG, JPG, TIFF,
    HTML, HTM, XHTML, PHP, JSON, TXML, YML, RKM,
    XLIFF, SDLXLIFF, TMX, TTX, ITD, XLF,
    MIF, INX, IDML, ICML, XTG, TAG, DITA,
    PROPERTIES, RC, RESX, SGM, SGML, STRINGS, PO, XLW, XLSB;

    // Generate a dictionary mapping the extension to its enum constant
    private static Map<String, Format> supportedFormats;
    static {
        supportedFormats = new HashMap<>();
        Stream.of(Format.values())
                .forEach(format -> {
                    String ext = format.toString();
                    supportedFormats.put(ext, format);
                });
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
        Format format = supportedFormats.getOrDefault(extension, null);

        // If it is not supported, throw an exception
        if (format == null)
            throw new FormatNotSupportedException(extension);

        // Else, return it
        return format;

    }


    /**
     * Obtain the extension (as a string) of a given file
     * @param file File we want to extract the extension from
     * @return File's extension
     */
    private static String getExtension(File file) {
        String filename = file.getName();
        int index = filename.lastIndexOf('.');
        if (index <= 0)
            throw new IllegalArgumentException("The given file " + filename + " does not have any extension");
        return filename.substring(index + 1);
    }


    /**
     * Obtain the format of a given file
     * @param file File we want to know the format
     * @return File's format
     * @throws FormatNotSupportedException If the file's format is not supported
     */
    public static Format getFormat(File file) {
        return parse(getExtension(file));
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
