package com.matecat.converter.core;

import com.matecat.converter.core.okapiclient.OkapiPack;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.Locale;

/**
 * Created by Reneses on 7/23/15.
 */
public class XliffProcessorTest {

    @Test
    public void testGetOriginalFile() throws Exception {
        File xlf = new File(getClass().getResource(File.separator + "extraction" + File.separator + "test.docx.xlf").getPath());
        new XliffProcessor(xlf).getOriginalFile();
    }

    @Test
    public void testGetDerivedFile() throws Exception {

        File file = new File(getClass().getResource(File.separator + "derivation" + File.separator + "test.docx").getPath());

        // Generate xlf
        File xlf = new XliffGenerator(Locale.ENGLISH, Locale.ENGLISH, file).generate();

        // Remove pack
        File pack = new File(xlf.getParentFile().getPath() + File.separator + OkapiPack.PACK_FILENAME);
        FileUtils.deleteDirectory(pack);

        // Alter the translation
        String xlfContent = FileUtils.readFileToString(xlf);
        String newXlfContent = xlfContent.replaceAll("Oviedo", "____TRANSLATION____");
        FileUtils.writeStringToFile(xlf, newXlfContent);

        // Generate derived
        new XliffProcessor(xlf).getDerivedFile();

    }

}