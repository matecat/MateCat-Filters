package com.matecat.converter.core.format.converters;

import com.matecat.converter.core.format.Format;
import com.matecat.converter.core.format.converters.WinConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.*;

public class WinConverterTest {

    private WinConverter converter;
    private File convertedFile;

    @Before
    public void setUp() throws Exception {
        converter = new WinConverter();
    }

    @After
    public void tearDown() throws Exception {
        if (convertedFile != null)
            convertedFile.delete();
    }

    @Test
    public void testIsConvertible() throws Exception {
        Arrays.stream(Format.values()).forEach(
                format -> {
                    if (converter.isConvertible(format)) {
                        Format defaultConversion = converter.getPreferredConversion(format);
                        assertNotNull(defaultConversion);
                        assertTrue(converter.isConvertible(format, defaultConversion));
                    }
                }
        );
    }

    @Test
    public void testConvertDoc() throws Exception {

        // Convert the file
        File oviedo = new File(getClass().getResource("/converter/Oviedo.doc").toURI());
        WinConverter converter = new WinConverter();
        convertedFile = converter.convert(oviedo, Format.DOCX);

        // Check it was saved in the same directoy
        assertEquals(oviedo.getParentFile(), convertedFile.getParentFile());

        // Check it has DOCX extension
        assertEquals(Format.DOCX, Format.getFormat(convertedFile));

        // Check it has the same name
        int dotIndex = oviedo.getName().lastIndexOf('.');
        String oldFilename = oviedo.getName().substring(0, dotIndex);
        String newFilename = convertedFile.getName().substring(0, dotIndex);
        assertEquals(oldFilename, newFilename);

    }
}