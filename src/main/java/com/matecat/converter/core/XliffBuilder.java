package com.matecat.converter.core;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Xliff builder
 *
 * This class is used to build a new XLIFF based on the original XLIFF, containing both the original file and the
 * manifest. This is done in new 'file' elements, within their 'skeleton' child, following the
 * <a href="http://docs.oasis-open.org/xliff/xliff-core/v2.0/xliff-core-v2.0.html">Xliff v2.0 specification</a>
 *
 * Both files are inserted creating two new 'file' elements at the beggining of the XLF:
 * 1. Original file (id: original-file)
 * 2. Manifest (id: manifest)
 *
 * The filename and contents are stored as following:
 * <file id="{ID OF THE FILE}" original="{FILENAME}">
 *   <skeleton>
 *      <file-contents encoding="{ENCODING USED}">
 *          {ENCODED CONTENTS OF THE FILE}
 *      </file-contents>
 *   </skeleton>
 * </file>
 */
public class XliffBuilder {

    // Encoded which will be used to store the files
    private static Base64.Encoder encoder = Base64.getEncoder();

    /**
     * Build the XLIFF, manifest and original file into a new Xliff
     * @param originalFile Original file which was used to generate the result
     * @param result Wrapper containing the XLIFF and manifest
     * @return New XLIFF generated combining the inputs
     */
    public static File build(final File originalFile, final OkapiResult result) {

        // Check the inputs that are not empty
        if (result == null  ||  originalFile == null  ||  !originalFile.exists())
            throw new IllegalArgumentException("The result and the original file cannnot be null");

        // Retrieve the filename
        String filename = originalFile.getName();

        // Encode the files we are going to insert into the xlf
        String encodedManifest = encodeFile(result.getManifest());
        String encodedFile = encodeFile(originalFile);

        // Insert the filename, the encoded manifest and the encoded file into the xlf
        File xlf = result.getXlf();
        return createXliff(xlf, filename, encodedFile, encodedManifest);

    }


    /**
     * Encode the file
     * @param input File to be encoded
     * @return Encoded file
     */
    private static String encodeFile(File input) {
        String output;
        try {
            byte[] bytes = Files.readAllBytes(input.toPath());
            output = encoder.encodeToString(bytes);
        } catch (IOException e) {
            //e.printStackTrace();
            throw new RuntimeException("It was not possible to encode the file " + input.getName());
        }
        return output;
    }


    /**
     * Create a new Xliff
     * @param xlf Base xliff
     * @param filename Original file's filename
     * @param encodedFile Encoded original file's contents
     * @param encodedManifest Encoded manifest
     * @return Xliff generated
     */
    private static File createXliff(final File xlf, String filename, String encodedFile, String encodedManifest) {

        File output = null;

        try {

            // Parse the XML document
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xlf);
            Element root = document.getDocumentElement();

            // Retrieve the source and target language
            Element sampleFile = (Element) document.getElementsByTagName("file").item(0);
            String sourceLanguage = sampleFile.getAttribute("source-language");
            String targetLanguage = sampleFile.getAttribute("target-language");

            // Add the original file
            Element manifestNode = createFileElement(document, sourceLanguage, targetLanguage,
                    "manifest", "manifest.rkm", encodedManifest);
            root.insertBefore(manifestNode, root.getFirstChild());

            // Add the original file
            Element originalFileNode = createFileElement(document, sourceLanguage, targetLanguage,
                    "original-file", filename, encodedFile);
            root.insertBefore(originalFileNode, root.getFirstChild());

            // Normalize document
            document.normalizeDocument();

            // Save the file
            String outputPath = xlf.getParentFile().getPath() + File.separator + "output.xlf";
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(outputPath);
            transformer.transform(domSource, streamResult);

            // Instantiate the output file
            output = new File(outputPath);

            // Check that the file has been correctly created
            if (!output.exists()) {
                throw new RuntimeException("The output Xliff could not been created");
            }

        } catch (ParserConfigurationException | TransformerException | IOException | SAXException pce) {
            pce.printStackTrace();
        }

        // Return the outputted file
        return output;

    }


    /**
     * Create a file element which contains a encoded file
     * @param document XML's document
     * @param sourceLanguage Source language
     * @param targetLanguage Target language
     * @param elementID New element's ID
     * @param filename Filename of the file we are storing
     * @param encodedFile Encoded contents of the file we are storing
     * @return New file element
     */
    private static Element createFileElement(Document document, String sourceLanguage, String targetLanguage,
                                             String elementID, String filename, String encodedFile) {

        // Create the new file element which will contain the original file
        Element originalFileNode = document.createElement("file");
        originalFileNode.setAttribute("id", elementID);
        originalFileNode.setAttribute("datatype", "x-undefined");
        originalFileNode.setAttribute("original", filename);
        originalFileNode.setAttribute("source-language", sourceLanguage);
        originalFileNode.setAttribute("target-language", targetLanguage);

        // Original's file skeleton
        Element originalFileSkeleton = document.createElement("skeleton");
        Element originalFileContent = document.createElement("file-contents");
        originalFileContent.setAttribute("encoding", "base64");
        originalFileContent.appendChild(document.createTextNode(encodedFile));
        originalFileSkeleton.appendChild(originalFileContent);

        // Add the skeleton to the file, and the file to the document
        originalFileNode.appendChild(originalFileSkeleton);

        // Return the new node
        return originalFileNode;

    }

}
