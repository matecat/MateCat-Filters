package com.matecat.converter.core.format;

/**
 * Exception thrown when a format is not supported
 */
public class FormatNotSupportedException extends IllegalArgumentException {

    /**
     * Message accepting the input format
     * @param inputFormat Input format
     */
    public FormatNotSupportedException(String inputFormat) {
        super("The format " + inputFormat + " is not supported");
    }

    /**
     * Message accepting the input format
     * @param inputFormat Input format
     */
    public FormatNotSupportedException(Format inputFormat) {
        this(inputFormat.toString());
    }

    /**
     * Message accepting both the input and output format
     * @param inputFormat Input format
     * @param outputFormat Output format
     */
    public FormatNotSupportedException(Format inputFormat, Format outputFormat) {
        super("The conversion " + inputFormat.toString() + " to " + outputFormat.toString() + " is not supported");
    }

}
