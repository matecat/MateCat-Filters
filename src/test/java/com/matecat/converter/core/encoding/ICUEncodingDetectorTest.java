package com.matecat.converter.core.encoding;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class ICUEncodingDetectorTest {

    IEncodingDetector detector;

    @Before
    public void setUp() throws Exception {
        detector = new ICUEncodingDetector();
    }

    private File getTestFile(String name) throws URISyntaxException {
        return new File(getClass().getResource("/encoding/" + name).toURI());
    }

    @Test
    public void testDetectHTMLUTF8() throws Exception {
        File testFile = getTestFile("UTF-8.html");
        Encoding match = detector.detect(testFile);
        assertEquals("UTF-8", match.getCode());
    }

    @Test
    public void testDetectHTMLUTF16LE() throws Exception {
        File testFile = getTestFile("UTF-16LE.html");
        Encoding match = detector.detect(testFile);
        assertEquals("UTF-16LE", match.getCode());
    }

    @Test
    public void testDetectTXTUTF8() throws Exception {
        File testFile = getTestFile("UTF-8.txt");
        Encoding match = detector.detect(testFile);
        assertEquals("UTF-8", match.getCode());
    }

    @Test
    public void testDetectTXTUTF16LE() throws Exception {
        File testFile = getTestFile("UTF-16LE.txt");
        Encoding match = detector.detect(testFile);
        assertEquals("UTF-16LE", match.getCode());
    }

    @Test
    public void testDetectTXTWindows1252() throws Exception {
        File testFile = getTestFile("windows-1252.txt");
        Encoding match = detector.detect(testFile);
        assertEquals("ISO-8859-1", match.getCode());
    }
}