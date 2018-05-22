package com.matecat.converter.core;

import com.matecat.filters.basefilters.DefaultFilter;
import org.junit.Test;

import java.io.File;
import java.util.Locale;

import static org.junit.Assert.*;

public class DefaultFilterTest {


    /**
     * As every sub component is tested, the only purpose of this test is to check that
     * no exception is thrown
     */
    @Test
    public void testExtract() {

        File file = new File(getClass().getResource("/generation/test.docx").getPath());
        DefaultFilter generator = new DefaultFilter();
        File xlf = generator.extract(file, Locale.ENGLISH, Locale.ENGLISH, null);
        assertNotNull(xlf);
        assertTrue(xlf.exists());
        assertFalse(xlf.isDirectory());

    }
}