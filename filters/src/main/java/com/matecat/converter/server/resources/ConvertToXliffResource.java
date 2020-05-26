package com.matecat.converter.server.resources;

import com.matecat.converter.core.project.Project;
import com.matecat.converter.core.project.ProjectFactory;
import com.matecat.converter.server.JSONResponseFactory;
import com.matecat.converter.server.exceptions.ServerException;
import com.matecat.filters.basefilters.FiltersRouter;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;
import java.util.Locale;
import java.util.MissingResourceException;


/**
 * Resource taking care of the conversion task into .XLF
 */
@Path("/AutomationService/original2xliff")
public class ConvertToXliffResource extends BaseResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertToXliffResource.class);

    /**
     * Convert a file into XLF
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response convert(
            @FormDataParam("documentContent") InputStream fileInputStream,
            @FormDataParam("documentContent") FormDataContentDisposition contentDispositionHeader,
            @FormDataParam("fileName") String filename,
            @FormDataParam("sourceLocale") String sourceLanguageCode,
            @FormDataParam("targetLocale") String targetLanguageCode,
            @FormDataParam("segmentation") String segmentation,
            @FormDataParam("debugMode") @DefaultValue("false") boolean debugMode) {
        traceStart(Operations.SOURCE2XLIFF);
        try {
            // If debug mode requested install the log capturer
            setupDebugMode(debugMode);

            // Due to a bug in the MIMEPull library (MIMEParser.java line 510),
            // contentDispositionHeader.getFileName() returns the filename in ISO-8859-1
            // even if it was sent in UTF-8. Unless this bug is there, here is a little
            // workaround: you can send the filename in UTF-8 in the 'fileName' POST
            // param. If the 'fileName' parameter is present, it overrides the name of
            // the file in 'documentContent'
            if (filename == null || filename.isEmpty())
                filename = FilenameUtils.getName(contentDispositionHeader.getFileName());

            // Make extension ALWAYS lower case.
            // The original extension of the file is written in the output XLIFF
            // always lowercase, for compliance with the XLIFF spec (see datatype
            // attribute of <file> element). This causes insidious bugs in the
            // back-conversion, very difficult to solve with the current class
            // structure (I tried). This fixes it easily.
            // TODO: refactor internal classes to be filename/extension agnostic
            filename = FilenameUtils.removeExtension(filename) + "." + FilenameUtils.getExtension(filename).toLowerCase();

            LOGGER.info("SOURCE > XLIFF request: file=<{}> source=<{}> target=<{}>", filename, sourceLanguageCode, targetLanguageCode);
            MDC.put(FILENAME, filename);
            MDC.put(SRC_LANG, sourceLanguageCode);
            MDC.put(TGT_LANG, targetLanguageCode);

            Project project = null;
            File xlf = new File("");
            String errorMessage = "Unknown error";
            Response response;
            boolean everythingOk = false;
            try {
                // Check that the input file is not null
                if (fileInputStream == null) {
                    throw new IllegalArgumentException("The input file has not been sent");
                }

                // Parse the codes
                Locale sourceLanguage = parseLanguage(sourceLanguageCode);
                Locale targetLanguage = parseLanguage(targetLanguageCode);

                // Create the project
                project = ProjectFactory.createProject(filename, fileInputStream);
                MDC.put(CACHE_ID, project.getFolder().getName());
                MDC.put(FILESIZE, String.valueOf(project.getFile().length()));

                // Retrieve the xlf
                xlf = new FiltersRouter().extract(project.getFile(), sourceLanguage, targetLanguage, segmentation);

                // Set OK flag
                everythingOk = true;
                LOGGER.info("Successfully returned XLIFF file");
            } catch (Exception e) {
                // save the error message
                errorMessage = prepareErrorMessage(e);
                LOGGER.error("Exception converting source to XLIFF", e);
            } finally {
                // Create response
                if (everythingOk) {
                    response = Response
                            .status(Response.Status.OK)
                            .entity(JSONResponseFactory.getConvertSuccess(xlf, LOG_CAPTURER.getStoredLog()))
                            .build();
                } else {
                    response = Response
                            .status(Response.Status.BAD_REQUEST)
                            .entity(JSONResponseFactory.getError(errorMessage, LOG_CAPTURER.getStoredLog()))
                            .build();
                }
                // Close the project and streams
                requestCleanup(fileInputStream, debugMode, project, everythingOk);
            }
            return response;
        } finally {
            traceEnd();
        }
    }

    /**
     * Parse the language code into Locales
     */
    private Locale parseLanguage(String languageCode) throws ServerException {
        if (languageCode.isEmpty()) {
            throw new ServerException("Empty language");
        }
        // Parse the code
        Locale locale = Locale.forLanguageTag(languageCode);
        // Validate language
        try {
            locale.getISO3Language();
            return locale;
        } catch (MissingResourceException e) {
            // If there is any error, throw a ServerException
            throw new ServerException("Invalid language: " + languageCode);
        }
    }

}
