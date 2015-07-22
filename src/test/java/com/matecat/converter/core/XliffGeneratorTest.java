package com.matecat.converter.core;

import org.junit.Test;

import java.io.File;
import java.util.Locale;

/**
 * Created by Reneses on 7/22/15.
 */
public class XliffGeneratorTest {

    @Test
    public void testGenerate() throws Exception {

        File file = new File(getClass().getResource(File.separator + "okapi" + File.separator + "Oviedo.docx").getPath());
        XliffGenerator generator = new XliffGenerator(Locale.ENGLISH, Locale.ENGLISH, file);
        File xlf = generator.generate();

    }
}