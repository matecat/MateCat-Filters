package com.matecat.converter.core.format.converters;

/**
 * Exception thrown when a file is too big for the conversion
 */
public class FileTooBigException extends IllegalArgumentException {

    /**
     * Default constructor which includes the filename into the exception message
     * @param filename Filename of the file thathas produced the exception
     */
    public FileTooBigException(String filename) {
        super("The given file \"" + filename + "\" is bigger than the maximum size allowed");
    }

}
