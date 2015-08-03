package com.matecat.converter.core.encoding;


import com.matecat.converter.core.format.Format;

import java.io.File;

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