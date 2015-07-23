package com.matecat.converter.core;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.matecat.converter.core.encoding.Encoding;
import com.matecat.converter.core.encoding.ICUEncodingDetector;
import com.matecat.converter.core.encoding.IEncodingDetector;
import com.matecat.converter.core.format.Format;
import com.matecat.converter.core.format.FormatNotSupportedException;
import com.matecat.converter.core.format.converters.AbstractFormatConverter;
import com.matecat.converter.core.format.converters.loc.LOCFormatConverter;
import com.matecat.converter.core.okapiclient.OkapiClient;
import com.matecat.converter.core.okapiclient.OkapiPack;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Xliff Generator
 * This is the main class of the converter's core. It is used to generate an Xliff.
 */
public class XliffReverser {

    // Required properties
    private File xlf;
    private Format originalFormat;

    // Other module's instances
    private List<AbstractFormatConverter> converters = new ArrayList<>();
    private IEncodingDetector encodingDetector = new ICUEncodingDetector();
    private OkapiClient okapiClient = new OkapiClient();


    public XliffReverser(File xlf) {

        // Check that the input file is not null
        if (xlf == null  ||  !xlf.exists()  ||  xlf.isDirectory())
            throw new IllegalArgumentException("The input file does not exist");

        // Check that the file is an .xlf
        if (!FilenameUtils.getExtension(xlf.getName()).equals("xlf"))
            throw new IllegalArgumentException("The input file is not a .xlf file");

        // Save the xlf
        this.xlf = xlf;

    }


    // First element: original file
    // Save to the same directory
    public void extract() {

        try {

            // Output folder
            File packFolder = new File(xlf.getParentFile().getPath() + File.separator + "pack");
            if (packFolder.exists())
                FileUtils.cleanDirectory(packFolder);
            else
                packFolder.mkdir();

            // Parse the XML document
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xlf);
            Element root = document.getDocumentElement();

            // Reconstruct the original file
            Element fileElement = (Element) root.getFirstChild();
            reconstructOriginalFile(packFolder, fileElement);

            // Extract the original format
            extractOriginalFormat(fileElement);

            // Reconstruct the manifest
            Element manifestElement = (Element) fileElement.getNextSibling();
            reconstructManifest(packFolder, manifestElement);

            // Reconstruct the original xlf
            reconstructOriginalXlf(packFolder, document, fileElement, manifestElement);

            // Generate the pack (which will check the extracted files)
            OkapiPack pack = new OkapiPack(packFolder);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }


    private void extractOriginalFormat(Element fileElement) {

        // Filename
        String filename = fileElement.getAttribute("original");

        // Original format
        String originalFormatString = ((Element) fileElement.getFirstChild().getFirstChild()).getAttribute("original-format");
        Format originalFormat;
        try {
            originalFormat = Format.parse(originalFormatString);
        }
        catch (FormatNotSupportedException e1) {
            try {
                originalFormat = Format.parse(FilenameUtils.getExtension(filename));
            }
            catch (FormatNotSupportedException e2) {
                throw new RuntimeException("The encoded file has no extension");
            }
        }
        this.originalFormat = originalFormat;
    }


    private void reconstructOriginalFile(File packFolder, Element fileElement) {

        try {

            // Filename
            String filename = fileElement.getAttribute("original");

            // Contents
            String encodedFile = ((Element) fileElement.getFirstChild().getFirstChild()).getTextContent();
            byte[] originalFileBytes = Base64.getDecoder().decode(encodedFile);

            // Create original folder
            File originalFolder = new File(packFolder.getPath() + File.separator + OkapiPack.ORIGINAL_DIRECTORY_NAME);
            if (originalFolder.exists())
                FileUtils.cleanDirectory(originalFolder);
            else
                originalFolder.mkdir();

            // Reconstruct the original file
            File originalFile = new File(originalFolder.getPath() + File.separator + filename);
            originalFile.createNewFile();
            FileUtils.writeByteArrayToFile(originalFile, originalFileBytes);

        }
        catch (Exception e) {
            throw new RuntimeException("It was not possible to reconstruct the original file");
        }
    }

    private void reconstructManifest(File packFolder, Element manifestElement) {

        try {

            // Check that it's the manifest
            if (!manifestElement.getAttribute("original").equals(OkapiPack.MANIFEST_FILENAME))
                throw new RuntimeException("The xlf is corrupted: it does not contain a manifest");

            // Manifest contents
            String encodedManifest = manifestElement.getFirstChild().getFirstChild().getTextContent();
            byte[] manifestBytes = Base64.getDecoder().decode(encodedManifest);

            // Reconstruct the manifest file
            File manifestFile = new File(packFolder.getPath() + File.separator + OkapiPack.MANIFEST_FILENAME);
            manifestFile.createNewFile();
            FileUtils.writeByteArrayToFile(manifestFile, manifestBytes);

        }
        catch (Exception e) {
            throw new RuntimeException("It was not possible to reconstruct the manifest file");
        }
    }

    private void reconstructOriginalXlf(File packFolder, Document document, Element fileElement, Element manifestElement) {

        try {

            // Get root
            Element root = document.getDocumentElement();

            // Filename
            String filename = fileElement.getAttribute("original");

            // Obtain the original xlf
            root.removeChild(fileElement);
            root.removeChild(manifestElement);

            // Normalize document
            document.normalizeDocument();

            // Create work folder
            File workFolder = new File(packFolder.getPath() + File.separator + OkapiPack.WORK_DIRECTORY_NAME);
            if (workFolder.exists())
                FileUtils.cleanDirectory(workFolder);
            else
                workFolder.mkdir();

            // Save the file
            String xlfOutputPath = workFolder.getPath() + File.separator + filename + ".xlf";
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(xlfOutputPath);
            transformer.transform(domSource, streamResult);

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}