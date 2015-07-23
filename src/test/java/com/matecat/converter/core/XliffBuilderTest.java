package com.matecat.converter.core;

import com.matecat.converter.core.okapiclient.OkapiPack;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

import static org.junit.Assert.assertEquals;

/**
 * Created by Reneses on 7/17/15.
 */
public class XliffBuilderTest {

    OkapiPack pack;

    @Before
    public void setUp() throws Exception {

        // Load files
        pack = new OkapiPack(new File(getClass().getResource(File.separator + "samplepack").getPath()));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithNullPack() throws Exception {
        XliffBuilder.build(null);
    }

    @Test
    public void testBuild() throws Exception {

        // Build xliff
        File xliff = XliffBuilder.build(pack);

        // Check the generated xliff
        try {

            // Parse the XML document
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xliff);
            Element root = document.getDocumentElement();

            // Check that the first element is the original file
            Element firstElement = (Element) root.getFirstChild();
            String originalFilename = pack.getOriginalFile().getName();
            String retrievedFirstElementFilename = firstElement.getAttribute("original");
            assertEquals(originalFilename, retrievedFirstElementFilename);

            // Check that the encoding is stored properly
            byte[] originalFileBytes = Files.readAllBytes(pack.getOriginalFile().toPath());
            String originalFileEncoded = Base64.getEncoder().encodeToString(originalFileBytes);
            String encodedFile = firstElement.getFirstChild().getFirstChild().getFirstChild().getTextContent();
            assertEquals("The encoding of the original file does not match", originalFileEncoded, encodedFile);

            // Check that the second element is the manifest
            Element secondElement = (Element) firstElement.getNextSibling();
            String retrievedSecondElementFilename = secondElement.getAttribute("original");
            assertEquals("manifest.rkm", retrievedSecondElementFilename);

            // Check that the manifest is stored properly
            byte[] manifestBytes = Files.readAllBytes(pack.getManifest().toPath());
            String originalManifestEncoded = Base64.getEncoder().encodeToString(manifestBytes);
            String encodedManifest = secondElement.getFirstChild().getFirstChild().getFirstChild().getTextContent();
            assertEquals("The encoding of the manifest does not match", originalManifestEncoded, encodedManifest);

            // Removed the generated file
            xliff.delete();

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }

    }
}