package com.matecat.converter.core;

import com.matecat.converter.core.okapiclient.OkapiPack;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.Locale;

import static org.junit.Assert.assertEquals;


/**
 * Xliff processor Test
 */
public class XliffProcessorTest {


    /**
     * Test the extraction of the original file without problems
     * @throws Exception
     */
    @Test
    public void testGetOriginalFile() throws Exception {
        File xlf = new File(getClass().getResource("/extraction/test.docx.xlf").getPath());
        new XliffProcessor(xlf).getOriginalFile();
    }


    /**
     * Test that the file is derived and, in special, that it is translated
     * @throws Exception
     */
    @Test
    public void testGetDerivedFile() throws Exception {

        // Constants to work with
        final String ORIGINAL = "TEXT _BEFORE_ TRANSLATION";
        final String DERIVED = "TEXT _AFTER_ TRANSLATION";

        // Create text file
        File folder = new File(getClass().getResource("/derivation").getPath());
        File input = new File(folder.getPath() + File.separator + "test.txt");
        FileUtils.writeStringToFile(input, ORIGINAL);

        // Generate xlf
        File xlf = new XliffGenerator(Locale.ENGLISH, Locale.FRENCH, input, null).generate();

        // Remove pack
        File pack = new File(folder.getPath() + File.separator + OkapiPack.PACK_FILENAME);
        FileUtils.deleteDirectory(pack);

        // Alter the translation
        String xlfContent = FileUtils.readFileToString(xlf, "UTF-8");
        String newXlfContent = xlfContent.replaceAll(ORIGINAL + "</mrk></target>", DERIVED + "</mrk></target>");
        FileUtils.writeStringToFile(xlf, newXlfContent);

        // Generate derived and check
        File output = new XliffProcessor(xlf).getDerivedFile();
        String content = FileUtils.readFileToString(output, "UTF-8");
        assertEquals(DERIVED, content);

    }

}