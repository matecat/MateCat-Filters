package com.matecat.converter.core.okapiclient;

import com.matecat.converter.core.encoding.Encoding;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class OkapiClientTest {

    private void testFile(String filename) throws IOException {
        File file = new File(getClass().getResource("/okapi/" + filename).getPath());
        OkapiPack pack = OkapiClient.generatePack(Locale.ENGLISH, Locale.ENGLISH, Encoding.getDefault(), file, null, null);
        OkapiClient.generateDerivedFile(pack);
        pack.delete();
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

    @Test
    public void testHTM() throws Exception {
        testFile("test.htm");
    }

    @Test
    public void testXHTML() throws Exception {
        testFile("test.xhtml");
    }

    @Test
    public void testODT() throws Exception {
        testFile("test.odt");
    }

    @Test
    public void testODP() throws Exception {
        testFile("test.odp");
    }

    @Test
    public void testODS() throws Exception {
        testFile("test.ods");
    }

    @Test
    public void testPHP() throws Exception {
        testFile("test.php");
    }

    @Test
    public void testPO() throws Exception {
        testFile("test.po");
    }

    @Test
    public void testJSON() throws Exception {
        testFile("test.json");
    }

    @Test
    public void testPROPERTIES() throws Exception {
        testFile("test.properties");
    }

    @Test
    public void testIDML() throws Exception {
        testFile("test.idml");
    }

    @Test
    public void testTXML() throws Exception {
        testFile("test.txml");
    }

    @Test
    public void testYML() throws Exception {
        testFile("test.yml");
    }

    @Test
    public void testXML() throws Exception {
        testFile("test.xml");
    }

    @Test
    public void testDITA() throws Exception {
        testFile("test.dita");
    }

    @Test
    public void testSTRINGS() throws Exception {
        testFile("test.strings");
    }

    @Test
    public void testCSV() throws Exception {
        testFile("test.csv");
    }

    @Test
    public void testMIF() throws Exception {
        testFile("test.mif");
    }


}