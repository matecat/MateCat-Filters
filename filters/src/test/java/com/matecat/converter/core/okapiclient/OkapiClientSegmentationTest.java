package com.matecat.converter.core.okapiclient;

import com.matecat.converter.core.encoding.Encoding;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import static java.nio.charset.Charset.forName;

public class OkapiClientSegmentationTest {

    private ArrayList<String> sourceSegs(File xliff) throws IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException();
        }
        Document doc = documentBuilder.parse(xliff);

        NodeList elements = null;
        try {
            XPathExpression xp = XPathFactory.newInstance().newXPath().compile("//seg-source/mrk");
            elements = (NodeList) xp.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }


        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < elements.getLength(); i++) {
            strings.add(elements.item(i).getTextContent());
        }
        return strings;
    }

    private ArrayList<String> segment(String text) throws IOException, SAXException {
        File tempFile = File.createTempFile("okapi-segmentation-", ".txt");
        tempFile.deleteOnExit();
        FileUtils.writeStringToFile(tempFile, text, forName("UTF-8"));

        OkapiPack pack = OkapiClient.generatePack(Locale.ENGLISH, Locale.ENGLISH, Encoding.getDefault(), tempFile, null, null, false);
        ArrayList<String> strings = sourceSegs(pack.getXlf());
        pack.delete();
        return strings;
    }

    @Test
    public void testPercWithoutLeadingZero() throws IOException, SAXException {
        String sentence = "Only .004% resulted in a damage.";
        ArrayList<String> segs = segment(sentence);
        Assert.assertEquals(sentence, segs.get(0));
        Assert.assertEquals(1, segs.size());
    }

    @Test
    public void testSentenceStartingWithNumber() throws IOException, SAXException {
        String sentence = "2 bananas. 3 tomatoes.";
        ArrayList<String> segs = segment(sentence);
        Assert.assertEquals("2 bananas. ", segs.get(0));
        Assert.assertEquals("3 tomatoes.", segs.get(1));
        Assert.assertEquals(2, segs.size());
    }

    @Test
    public void testMultipleNewLines() throws IOException, SAXException {
        String sentence = "The Title\n\nThe text.";
        ArrayList<String> segs = segment(sentence);
        Assert.assertEquals("The Title", segs.get(0));
        Assert.assertEquals("The text.", segs.get(1));
        Assert.assertEquals(2, segs.size());
    }

}