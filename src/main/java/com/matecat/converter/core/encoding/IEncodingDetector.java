package com.matecat.converter.core.encoding;

import java.io.File;

/**
 * Encoding interface
 * An encoding detector must process a file and return it's encoding (for example, UTF-8)
 */
public interface IEncodingDetector {

    /**
     * Detect the encoding used in a file
     * @param file File to process
     * @return Encoding used
     */
    Encoding detect(File file);

}
