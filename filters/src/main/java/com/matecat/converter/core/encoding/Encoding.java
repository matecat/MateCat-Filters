package com.matecat.converter.core.encoding;


/**
 * Encoding class, which just wraps the encoding code (string)
 * http://www.iana.org/assignments/character-sets/character-sets.xhtml
 */
public class Encoding {

    // Default encoding
    public static final String DEFAULT = "UTF-8";

    // Encoding code
    private String code;


    /**
     * Create an encoding instance given its IANA code
     * @param code Encoding's code
     */
    public Encoding(String code) {
        this.code = code;
    }


    /**
     * Get the encoding's code
     * @return Encoding's code
     */
    public String getCode() {
        return code;
    }


    /**
     * Return encoding's code
     * @return Encoding's code
     */
    public String toString() {
        return code;
    }


    /**
     * Get default encoding
     * @return Default encoding, UTF-8
     */
    public static Encoding getDefault() {
        return new Encoding(DEFAULT);
    }

}