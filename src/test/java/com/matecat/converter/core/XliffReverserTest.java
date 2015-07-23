package com.matecat.converter.core;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by Reneses on 7/23/15.
 */
public class XliffReverserTest {

    @Test
    public void testExtract() throws Exception {

        File xlf = new File(getClass().getResource(File.separator + "extraction" + File.separator + "test.docx.xlf").getPath());
        new XliffReverser(xlf).extract();

    }
}