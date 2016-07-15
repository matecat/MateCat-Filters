package com.matecat.converter;

import net.sf.okapi.common.Event;
import net.sf.okapi.common.exceptions.OkapiIOException;
import net.sf.okapi.common.pipeline.BasePipelineStep;
import net.sf.okapi.common.resource.RawDocument;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EscapeTwigStep extends BasePipelineStep {
    @Override
    public String getName() {
        return "Escape Twig expressions";
    }

    @Override
    public String getDescription() {
        return "Escapes all Twig expressions to make the document HTML valid.";
    }

    @Override
    protected Event handleRawDocument (Event event) {
        try {
            RawDocument rawDoc = event.getRawDocument();

            String content = IOUtils.toString(rawDoc.getStream(), rawDoc.getEncoding());

            Pattern p = Pattern.compile("\\{\\{[^\\}]+\\}\\}");
            Matcher m = p.matcher(content);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(sb, StringEscapeUtils.escapeHtml(m.group()));
            }
            m.appendTail(sb);

            // Open the output
            File outFile;
            try {
                outFile = File.createTempFile("~okapi-48_okp-lbc_", ".tmp");
            }
            catch ( Throwable e ) {
                throw new OkapiIOException("Cannot create temporary output.", e);
            }
            FileUtils.writeStringToFile(outFile, sb.toString());

            rawDoc.finalizeOutput();

            // Creates the new RawDocument
            event.setResource(new RawDocument(outFile.toURI(), rawDoc.getEncoding(),
                    rawDoc.getSourceLocale(), rawDoc.getTargetLocale()));
        }
        catch ( IOException e ) {
            throw new OkapiIOException("IO error while converting.", e);
        }

        return event;
    }
}
