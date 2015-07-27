package com.matecat.converter.core;

import org.junit.Test;

import java.io.File;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Created by Reneses on 7/23/15.
 */
public class XliffProcessorTest {

    @Test
    public void testGetOriginalFile() throws Exception {
        File xlf = new File(getClass().getResource(File.separator + "extraction" + File.separator + "test.docx.xlf").getPath());
        //new XliffProcessor(xlf).getOriginalFile();
    }

    @Test
    public void testGetDerivedFile() throws Exception {
        File file = new File(getClass().getResource(File.separator + "merging" + File.separator + "test.docx").getPath());

        // Generate pack
        File xlf = new XliffGenerator(Locale.ENGLISH, Locale.ENGLISH, file).generate();

        // Generate derived
        new XliffProcessor(xlf).getDerivedFile();
    }

}