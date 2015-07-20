package com.matecat.converter.core.format.converters;

import com.matecat.converter.core.format.Format;

import java.io.File;

/**
 * This class represents a format converter.
 *
 * Okapi does not offer direct support for all the formats. However, some formats can be easily converted into
 * others that the framework supports. Format converters perform this task. They can also offer the reverse process.
 */
public abstract class AbstractFormatConverter {

    /**
     * Convert a file to a given format
     * @param file Input file
     * @param outputFormat Format we want to convert the file to
     * @return Converted file
     */
    public abstract File convert(final File file, Format outputFormat);

    /**
     * Check if the converted admits a format
     * @param inputFormat Format we want to convert
     * @return True if it's convertible, false otherwise
     */
    public abstract boolean isConvertible(Format inputFormat);

    /**
     * Check if the specified conversion is available
     * @param inputFormat Format we want to convert
     * @param outputFormat Format we want to convert to
     * @return True if it's convertible, false otherwise
     */
    public abstract boolean isConvertible(Format inputFormat, Format outputFormat);


    /**
     * Get the default format that the input format will be converted to
     * @param inputFormat Format we are going to convert
     * @return Format it will be converted to by default
     */
    public abstract Format getDefaultConversion(Format inputFormat);

}