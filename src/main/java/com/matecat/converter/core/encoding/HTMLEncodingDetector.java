package com.matecat.converter.core.encoding;

import com.matecat.converter.core.format.Format;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Encoding detector for HTML files based on charset markup
 *
 * If the HTML file contains specific charset information, it overwrites other charset
 * information found before.
 *
 * The detector matches the following type of declarations:
 *
 *      <meta charset="UTF-8"> // HTML5
 *      <meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1"> // HTML4
 *      <?xml version="1.0" encoding="UTF-8"?> // XHTML
 *
 * Some useful links:
 *
 *      http://www.w3schools.com/html/html_charset.asp
 *      http://www.w3.org/TR/2011/WD-html5-20110113/parsing.html //
 *      http://www.w3.org/blog/2008/03/html-charset/
 *      http://www.w3schools.com/html/html_charset.asp
 */
public class HTMLEncodingDetector implements IEncodingDetector {

    // Default HTML encoding
    public static final String DEFAULT_HTML_ENCODING = "UTF-8";

    /**
     * {@inheritDoc}
     */
    @Override
    public Encoding detect(File file) {
        return detect(file, null);
    }

    /**
     * Detect the encoding of a file
     * @param file File
     * @param suspectedEncoding Encoding we think the file can had, used to read it and parse it
     * @return Encoding
     */
    public Encoding detect(File file, Encoding suspectedEncoding) {

        // Check that the file is valid
        if (file == null  ||  !file.exists())
            throw new IllegalArgumentException("The file does not exist");

        // Check that the format is valid
        Format format = Format.getFormat(file);
        if (format != Format.HTML  &&  format != Format.HTM  &&  format != Format.XHTML)
            throw new IllegalArgumentException("The file is not an HTML/HTM/XHTML file");

        // Read the contents and return it
        try {
            String content = suspectedEncoding != null ?
                    FileUtils.readFileToString(file, suspectedEncoding.getCode())
                    : FileUtils.readFileToString(file);
            Encoding encoding = detect(content);
            if (encoding != null)
                return encoding;
        }
        catch (IOException ignore) {}

        // If there is any problem or it's not defined , return the suspected one, or the html default
        return (suspectedEncoding != null)? suspectedEncoding : new Encoding(DEFAULT_HTML_ENCODING);
    }

    /**
     * Detect the encoding matching HTML tags
     * @param content File content
     * @return Encoding found, or null if there is no tag present
     */
    private Encoding detect(String content) {

        // First, parse for HTML5
        Pattern html5Pattern = Pattern.compile("<meta.*charset=\"?([\\w\\-]*)\"?");
        Matcher html5Matcher = html5Pattern.matcher(content);
        if (html5Matcher.find())
            return new Encoding(html5Matcher.group(1));

        // If not, parse HTML4
        Pattern html4Pattern = Pattern.compile("<meta.*content=\".*charset=([\\w-]*)\"");
        Matcher html4Matcher = html4Pattern.matcher(content);
        if (html4Matcher.find())
            return new Encoding(html4Matcher.group(1));

        // If not, parse XHTML
        Pattern xhtmlPattern = Pattern.compile("<\\?xml.*encoding=\"([\\w-]*)\"");
        Matcher xhtmlMatcher = xhtmlPattern.matcher(content);
        if (xhtmlMatcher.find())
            return new Encoding(xhtmlMatcher.group(1));

        return null;
    }

}
