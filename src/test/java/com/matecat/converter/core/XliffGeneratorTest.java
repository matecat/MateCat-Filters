package com.matecat.converter.core;

import org.junit.Test;

import java.io.File;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Created by Reneses on 7/22/15.
 */
public class XliffGeneratorTest {


    /**
     * As every sub component is tested, the only purpose of this test is to check that
     * no exception is thrown
     * @throws Exception
     */
    @Test
    public void testGenerate() throws Exception {

        File file = new File(getClass().getResource(File.separator + "generation" + File.separator + "test.docx").getPath());
        XliffGenerator generator = new XliffGenerator(Locale.ENGLISH, Locale.ENGLISH, file);
        File xlf = generator.generate();
        assertNotNull(xlf);
        assertTrue(xlf.exists());
        assertFalse(xlf.isDirectory());

    }
}