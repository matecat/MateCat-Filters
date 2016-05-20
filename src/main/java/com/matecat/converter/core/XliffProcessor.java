package com.matecat.converter.core;

import com.matecat.converter.core.format.Format;
import com.matecat.converter.core.format.converters.Converters;
import com.matecat.converter.core.okapiclient.OkapiClient;
import com.matecat.converter.core.okapiclient.OkapiPack;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
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
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Xliff Processor
 *
 * Processor of Xliff files, allowing:
 *  1. Extraction of the embedded pack (original file and manifest)
 *  2. Extraction of source and target languages
 */
public class XliffProcessor {

    // Logger
    private static Logger LOGGER = LoggerFactory.getLogger(XliffProcessor.class);

    private static final String CONVERTER_VERSION = XliffBuilder.class.getPackage().getImplementationVersion();
    private static final Pattern PRODUCER_CONVERTER_VERSION_PATTERN = Pattern.compile("matecat-converter(\\s+([^\"]+))?");

    // File we are processing
    private File xlf;

    // Embedded pack
    private OkapiPack pack;

    // Inner properties
    private String originalFilename = null;
    private Format originalFormat;
    private Locale sourceLanguage, targetLanguage;


    /**
     * Construct the processor given the XLF
     * @param xlf Xliff file we are going to process
     */
    public XliffProcessor(final File xlf) {

        // Check that the input file is not null
        if (xlf == null  ||  !xlf.exists()  ||  xlf.isDirectory())
            throw new IllegalArgumentException("The input file does not exist");

        // Check that the file is an .xlf
        if (!FilenameUtils.getExtension(xlf.getName()).equals("xlf"))
            throw new IllegalArgumentException("The input file is not a .xlf file");

        // Save the xlf
        this.xlf = xlf;

    }


    /**
     * Get source language
     * @return Source language
     */
    public Locale getSourceLanguage() {
        if (sourceLanguage == null)
            extractLanguages();
        return sourceLanguage;
    }


    /**
     * Get target language
     * @return Target language
     */
    public Locale getTargetLanguage() {
        if (targetLanguage == null)
            extractLanguages();
        return targetLanguage;
    }


    /**
     * Extract language from the XLF
     */
    private void extractLanguages() {
        try {
            // Parse the XML document
            String xlfContent = FileUtils.readFileToString(xlf, "UTF-8");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new StringReader(xlfContent)));
            Element firstFile = (Element) document.getDocumentElement().getElementsByTagName("file").item(0);

            // Extract the languages
            this.sourceLanguage = new Locale(firstFile.getAttribute("source-language"));
            this.targetLanguage = new Locale(firstFile.getAttribute("target-language"));
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException("It was not possible to extract the source and target languages. Corrupted xlf?");
        }
    }


    /**
     * Get the original file embedded into the XLF
     * @return Original file
     */
    public File getOriginalFile() {

        // Reconstruct the pack
        if (pack == null)
            reconstructPack();

        // Get the original file
        File originalFile = pack.getOriginalFile();

        // If it does not have its original format, try to convert it
        originalFile = convertToOriginalFormat(originalFile, originalFormat);

        // Return it
        return originalFile;

    }


    /**
     * Get the derived file
     * This is produced using the original file, the manifest and the XLF
     * @return Derived file
     */
    public File getDerivedFile() {

        // Reconstruct the pack
        if (pack == null)
            reconstructPack();

        // Generate the derived file
        File derivedFile = OkapiClient.generateDerivedFile(pack);

        // If it does not have its original format, try to convert it
        derivedFile = convertToOriginalFormat(derivedFile, originalFormat);

        // Return it
        return derivedFile;

    }


    /**
     * Try to convert a file to its original format
     * @param file File
     * @param originalFormat Original format
     * @return Converted file if possible, input file otherwise
     */
    private static File convertToOriginalFormat(File file, Format originalFormat) {
        Format currentFormat = Format.getFormat(file);
        if (currentFormat != originalFormat) {
            Converters converters = new Converters();
            if (converters.isConvertible(currentFormat, originalFormat)) {
                LOGGER.info("Converting file from {} to {}", currentFormat, originalFormat);
                return new Converters().convert(file, originalFormat);
            }
        }
        return file;
    }


    /**
     * Reconstruct the original Okapi result pack from the embedded files
     */
    private void reconstructPack() {

        try {

            // Output folder
            File packFolder = new File(xlf.getParentFile().getPath() + File.separator + "pack");
            if (packFolder.exists())
                FileUtils.cleanDirectory(packFolder);
            else
                packFolder.mkdir();

            // Parse the XML document
            String xlfContent = FileUtils.readFileToString(xlf, "UTF-8");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new StringReader(xlfContent)));
            Element root = document.getDocumentElement();

            Element fileElement = (Element) root.getFirstChild();
            Element manifestElement = (Element) fileElement.getNextSibling();

            // Reconstruct the manifest
            String originalFilename = reconstructManifest(packFolder, manifestElement);

            checkProducerVersion(fileElement);

            reconstructOriginalFile(packFolder, fileElement, originalFilename);

            extractOriginalFormat(fileElement);

            // Extract the languages
            this.sourceLanguage = new Locale(fileElement.getAttribute("source-language"));
            this.targetLanguage = new Locale(fileElement.getAttribute("target-language"));

            // Reconstruct the original xlf
            reconstructOriginalXlf(packFolder, document, fileElement, manifestElement, originalFilename);

            // Generate the pack (which will check the extracted files)
            this.pack = new OkapiPack(packFolder);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException("It was not possible to reconstruct the pack from the Xliff");
        }

    }

    /**
     * Checks the tool-id attribute of the provided <file> element, extracts
     * the XLIFF producer converter version and logs some warnings if the
     * producer version does not match the version of this server.
     */
    private static void checkProducerVersion(Element file) {
        final String toolId = file.getAttribute("tool-id");
        if (toolId != null) {
            final Matcher matcher = PRODUCER_CONVERTER_VERSION_PATTERN.matcher(toolId);
            if (matcher.find()) {
                final String xliffVersion = matcher.group(2);
                if (xliffVersion == null) {
                    LOGGER.warn("Missing producer version in input XLIFF");
                } else {
                    if (CONVERTER_VERSION == null) {
                        LOGGER.warn("XLIFF producer version is " + xliffVersion + ", but converter server version is unknown (version available only when running from a jar)");
                    } else if (!xliffVersion.equals(CONVERTER_VERSION)) {
                        LOGGER.warn("Converters versions mismatch: " + xliffVersion + " (XLIFF) vs " + CONVERTER_VERSION + " (server)");
                    } else {
                        // In this last condition converters versions match,
                        // so everything is perfect!
                    }
                }
            } else {
                LOGGER.warn("Bad tool-id attribute");
            }
        }
    }


    /**
     * Extract the original format from the embedded information
     * @param fileElement XML element from the file
     */
    private void extractOriginalFormat(Element fileElement) {
        try {
            String filename = fileElement.getAttribute("original");
            this.originalFormat = Format.getFormat(filename);
        }
        catch (Exception e1) {
            throw new RuntimeException("The encoded file has no extension");
        }
    }


    /**
     * Get the original filename from the embedded information
     * @param fileElement XML element from the file
     * @return Filename
     */
    private String getFilename(Element fileElement) {

        // Filename
        String filename = fileElement.getAttribute("original");

        // Replace the extension of the file for the one it was converted to
        // Datatype structure is:  datatype="x-{FORMAT (after conversions)}"
        try {
            String datatype = fileElement.getAttribute("datatype");
            String convertedExtension = datatype.substring(2);
            filename = FilenameUtils.getBaseName(filename) + "." + convertedExtension;
        }
        catch (Exception ignore) {}

        // Return it
        return filename;

    }


    /**
     * Extract the original file and save it in the pack
     * @param packFolder Pack's folder
     * @param fileElement XML element containing the file
     */
    private void reconstructOriginalFile(File packFolder, Element fileElement, String originalFilename) {

        try {

            // Filename
            if (originalFilename == null) {
                originalFilename = getFilename(fileElement);
            }

            // Contents
            Element internalFileElement = (Element) fileElement.getFirstChild().getFirstChild().getFirstChild();
            String encodedFile = internalFileElement.getTextContent();
            byte[] originalFileBytes = Base64.getDecoder().decode(encodedFile);

            // Create original folder
            File originalFolder = new File(packFolder.getPath() + File.separator + OkapiPack.ORIGINAL_DIRECTORY_NAME);
            if (originalFolder.exists())
                FileUtils.cleanDirectory(originalFolder);
            else
                originalFolder.mkdir();

            // Reconstruct the original file
            File originalFile = new File(originalFolder.getPath() + File.separator + originalFilename);
            originalFile.createNewFile();
            FileUtils.writeByteArrayToFile(originalFile, originalFileBytes);

        }
        catch (Exception e) {
            throw new RuntimeException("It was not possible to reconstruct the original file");
        }
    }


    /**
     * Reconstruct the manifest and save it in the pack
     * @param packFolder Pack's folder
     * @param manifestElement XML element containing the manifest
     */
    private String reconstructManifest(File packFolder, Element manifestElement) {

        try {

            // Check that it's the manifest
            if (!manifestElement.getAttribute("original").equals(OkapiPack.MANIFEST_FILENAME))
                throw new RuntimeException("The xlf is corrupted: it does not contain a manifest");

            // Extract language
            String targetLanguage = manifestElement.getAttribute("target-language");

            // Manifest contents
            String encodedManifest = manifestElement.getFirstChild().getFirstChild().getFirstChild().getTextContent();
            String manifest = new String(Base64.getDecoder().decode(encodedManifest), StandardCharsets.UTF_8);
            // MateCAT caches produced XLIFFs and reuses them to save
            // file conversions, updating just the source and target
            // languages when needed.
            // But this creates a problem: the Okapi's Manifest file
            // maintains the original couple of source - target
            // languages.
            // So sometimes this happens: Okapi runs looking for
            // segments in the language specified in the Manifest,
            // but in the XLIFF the segments are all in another
            // language, so Okapi finds nothing.
            // Missing target segments means obtaining a file
            // identical to the original, without translations.
            // To fix this I replace the target in the manifest with
            // the one defined in the XLIFF.
            manifest = manifest.replaceFirst("(<manifest [^>]* ?target=\")[^\"]+\"", "$1" + targetLanguage + "\"");

            // Extract source filename from manifest
            // Originally this class used to extract the original filename
            // from the "original" attribute of the first <file> element in
            // the XLIFF. Unfortunately some bugs in the encoding of the
            // filename in the HTTP communication caused many XLIFFs to be
            // created with corrupted text inside the "original" attribute.
            // So the pack was reconstructed using the "original" attribute,
            // but Okapi could not find the files because the filenames
            // in the manifest were different. To solve this bug I ignore
            // the "original" attribute and extract it directly from manifest.
            // TODO: remove the "original" attribute and rethink class design
            Matcher matcher = Pattern.compile(" relativeInputPath *= *\"(.+?)\"").matcher(manifest);
            String originalFilename = (matcher.find() ? StringEscapeUtils.unescapeXml(matcher.group(1)) : null);

            // Reconstruct the manifest file
            File manifestFile = new File(packFolder.getPath() + File.separator + OkapiPack.MANIFEST_FILENAME);
            manifestFile.createNewFile();
            FileUtils.writeStringToFile(manifestFile, manifest, StandardCharsets.UTF_8);

            return originalFilename;
        }
        catch (Exception e) {
            throw new RuntimeException("It was not possible to reconstruct the manifest file");
        }
    }


    /**
     * Reconstruct the original XLF used to derive this XLF; and save it into the work folder
     * inside the pack
     *
     * This is done by simply removing the file and manifest XML elements.
     * @param packFolder Pack's folder
     * @param document XML document
     * @param fileElement XML element containing the file
     * @param manifestElement XML element containing the manifest
     */
    private void reconstructOriginalXlf(File packFolder, Document document, Element fileElement, Element manifestElement, String originalFilename) {

        try {

            // Get root
            Element root = document.getDocumentElement();

            // Filename
            if (originalFilename == null) {
                originalFilename = getFilename(fileElement);
            }

            // Obtain the original xlf
            root.removeChild(fileElement);
            root.removeChild(manifestElement);

            // Create work folder
            File workFolder = new File(packFolder.getPath() + File.separator + OkapiPack.WORK_DIRECTORY_NAME);
            if (workFolder.exists())
                FileUtils.cleanDirectory(workFolder);
            else
                workFolder.mkdir();

            // Save the file
            String xlfOutputPath = workFolder.getPath() + File.separator + originalFilename + ".xlf";
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(xlfOutputPath);
            transformer.transform(domSource, streamResult);

        } catch (TransformerException | IOException e) {
            e.printStackTrace();
        }
    }

}