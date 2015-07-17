package com.matecat.converter.core;

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

    File originalFile;
    OkapiResult result;

    @Before
    public void setUp() throws Exception {

        // Load files
        originalFile = new File(getClass().getResource(File.separator + "Oviedo" + File.separator + "Oviedo.docx").toURI());
        File xlf = new File(getClass().getResource(File.separator + "Oviedo" + File.separator + "Oviedo.docx.xlf").toURI());
                File manifest = new File(getClass().getResource(File.separator + "Oviedo" + File.separator + "manifest.rkm").toURI());

        // Check that the files exist
        assert originalFile.exists();
        assert xlf.exists();
        assert manifest.exists();

        // Generate result
        result = new OkapiResult(xlf, manifest);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithNullFile() throws Exception {
        XliffBuilder.build(null, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithNullSolution() throws Exception {
        XliffBuilder.build(originalFile, null);
    }

    @Test
    public void testBuild() throws Exception {

        // Build xliff
        File xliff = XliffBuilder.build(originalFile, result);

        // Check the generated xliff
        try {

            // Parse the XML document
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xliff);
            Element root = document.getDocumentElement();

            // Check that the first element is the original file
            Element firstElement = (Element) root.getFirstChild();
            String originalFilename = originalFile.getName();
            String retrievedFirstElementFilename = firstElement.getAttribute("original");
            assertEquals(originalFilename, retrievedFirstElementFilename);

            // Check that the encoding is stored properly
            byte[] originalFileBytes = Files.readAllBytes(originalFile.toPath());
            String originalFileEncoded = Base64.getEncoder().encodeToString(originalFileBytes);
            String encodedFile = firstElement.getFirstChild().getFirstChild().getTextContent();
            assertEquals("The encoding of the original file does not match", originalFileEncoded, encodedFile);

            // Check that the second element is the manifest
            Element secondElement = (Element) firstElement.getNextSibling();
            String retrievedSecondElementFilename = secondElement.getAttribute("original");
            assertEquals("manifest.rkm", retrievedSecondElementFilename);

            // Check that the manifest is stored properly
            byte[] manifestBytes = Files.readAllBytes(result.getManifest().toPath());
            String originalManifestEncoded = Base64.getEncoder().encodeToString(manifestBytes);
            String encodedManifest = secondElement.getFirstChild().getFirstChild().getTextContent();
            assertEquals("The encoding of the manifest does not match", originalManifestEncoded, encodedManifest);

            // Removed the generated file
            xliff.delete();

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }

    }
}