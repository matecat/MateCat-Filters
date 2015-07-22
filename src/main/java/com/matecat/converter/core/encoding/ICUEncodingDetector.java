package com.matecat.converter.core.encoding;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Encoding detector which uses the <a href="http://site.icu-project.org">ICU library</a>
 */
public class ICUEncodingDetector implements IEncodingDetector {

    // Minimum confidence needed to accept the match
    public static final int CONFIDENCE_THRESHOLD = 50;

    /**
     * {@inheritDoc}
     */
    public Encoding detect(File file) {

        // Check that the file is valid
        if (file == null  ||  !file.exists())
            throw new IllegalArgumentException("The file does not exist");

        // Detect the encoding
        try {
            CharsetDetector detector = new CharsetDetector();
            byte[] bytes = Files.readAllBytes(file.toPath());
            detector.setText(bytes);
            CharsetMatch match = detector.detect();
            if (match.getConfidence() < CONFIDENCE_THRESHOLD)   // If we are not sure about it
                return Encoding.getDefault();                   // return the default encoding
            return new Encoding(match.getName());
        }

        // If some exception has been raised, return the default encoding
        catch (IOException e) {
            return Encoding.getDefault();
        }

    }
}
