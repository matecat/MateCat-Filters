package com.matecat.converter.core.okapiclient;

import com.matecat.converter.core.encoding.Encoding;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class OkapiClientTest {

    private void testFile(String filename) throws IOException {
        File file = new File(getClass().getResource(File.separator + "okapi" + File.separator + filename).getPath());
        OkapiClient client = new OkapiClient();
        OkapiPack pack = client.execute(Locale.ENGLISH, Locale.ENGLISH, Encoding.getDefault(), file);
        FileUtils.deleteDirectory(pack.getPackFolder());
    }

    @Test
    public void testDOCX() throws Exception {
        testFile("test.docx");
    }

    @Test
    public void testXLSX() throws Exception {
        testFile("test.xlsx");
    }

    @Test
    public void testPPTX() throws Exception {
        testFile("test.pptx");
    }

    @Test
    public void testTXT() throws Exception {
        testFile("test.txt");
    }

    @Test
    public void testHTML() throws Exception {
        testFile("test.html");
    }

}