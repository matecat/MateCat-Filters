package com.matecat.converter.core.okapiclient;

import com.matecat.converter.core.encoding.Encoding;
import org.junit.Test;

import java.io.File;
import java.util.Locale;

public class OkapiClientTest {

    private void testFile(String filename) {
        File file = new File(getClass().getResource("/okapi/" + filename).getPath());
        OkapiPack pack = OkapiClient.generatePack(Locale.ENGLISH, Locale.ENGLISH, Encoding.getDefault(), file, null, null, false);
        OkapiClient.generateDerivedFile(pack);
        pack.delete();
    }

    @Test
    public void testDOCX() {
        testFile("test.docx");
    }

    @Test
    public void testXLSX() {
        testFile("test.xlsx");
    }

    @Test
    public void testPPTX() {
        testFile("test.pptx");
    }

    @Test
    public void testTXT() {
        testFile("test.txt");
    }

    @Test
    public void testHTML() {
        testFile("test.html");
    }

    @Test
    public void testHTM() {
        testFile("test.htm");
    }

    @Test
    public void testXHTML() {
        testFile("test.xhtml");
    }

    @Test
    public void testODT() {
        testFile("test.odt");
    }

    @Test
    public void testODP() {
        testFile("test.odp");
    }

    @Test
    public void testODS() {
        testFile("test.ods");
    }

    @Test
    public void testPHP() {
        testFile("test.php");
    }

    @Test
    public void testPO() {
        testFile("test.po");
    }

    @Test
    public void testJSON() {
        testFile("test.json");
    }

    @Test
    public void testPROPERTIES() {
        testFile("test.properties");
    }

    @Test
    public void testIDML() {
        testFile("test.idml");
    }

    @Test
    public void testTXML() {
        testFile("test.txml");
    }

    @Test
    public void testYML() {
        testFile("test.yml");
    }

    @Test
    public void testXML() {
        testFile("test.xml");
    }

    @Test
    public void testDITA() {
        testFile("test.dita");
    }

    @Test
    public void testSTRINGS() {
        testFile("test.strings");
    }

    @Test
    public void testCSV() {
        testFile("test.csv");
    }

    @Test
    public void testMIF() {
        testFile("test.mif");
    }


}