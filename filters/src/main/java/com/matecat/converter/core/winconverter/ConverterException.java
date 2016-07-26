package com.matecat.converter.core.winconverter;

/**
 * Exception thrown by an internal error of the converter
 */
public class ConverterException extends RuntimeException {

    /**
     * Constructor which admits a custom error message
     * @param msg Internal error message
     */
    public ConverterException(String msg) {
        super("The converter has thrown an internal exception: '" + msg + "'");
    }

}
