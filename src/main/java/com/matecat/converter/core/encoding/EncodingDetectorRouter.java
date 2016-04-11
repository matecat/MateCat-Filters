package com.matecat.converter.core.encoding;


import com.matecat.converter.core.Format;

import java.io.File;


/**
 * Encoding detector router
 *
 * This class is the entry point for every file we want to detect the charset in. It send them to the
 * corresponding detectors, returning the result.
 */
public class EncodingDetectorRouter implements IEncodingDetector {

    /**
     * {@inheritDoc}
     */
    @Override
    public Encoding detect(File file) {

        Format format = Format.getFormat(file);

        // Plain text
        if (Format.isPlainTextFormat(format)) {

            Encoding encoding = new ICUEncodingDetector().detect(file);

            // HTML, HTM or XHTML
            if (format == Format.HTML || format == Format.HTM || format == Format.XHTML)
                encoding = new HTMLEncodingDetector().detect(file, encoding);

            return encoding;

        }

        // Other, use the default
        return Encoding.getDefault();

    }
}