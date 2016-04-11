package com.matecat.converter.server.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.MissingResourceException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.okapi.common.exceptions.OkapiUnexpectedRevisionException;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.matecat.converter.core.XliffGenerator;
import com.matecat.converter.core.project.Project;
import com.matecat.converter.core.project.ProjectFactory;
import com.matecat.converter.server.JSONResponseFactory;
import com.matecat.converter.server.exceptions.ServerException;


/**
 * Resource taking care of the conversion task into .XLF
 */
@Path("/AutomationService/original2xliff")
public class ConvertToXliffResource {

    // Logger
    private static Logger LOGGER = LoggerFactory.getLogger(ConvertToXliffResource.class);

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
            @FormDataParam("segmentation") String segmentation) {

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

        LOGGER.info("[CONVERSION REQUEST] {}: {} to {}", filename, sourceLanguageCode, targetLanguageCode);

        Project project = null;
        Response response = null;
        boolean everythingOk = false;
        try {

            // Check that the input file is not null
            if (fileInputStream == null)
                throw new IllegalArgumentException("The input file has not been sent");

            // Parse the codes
            Locale sourceLanguage = parseLanguage(sourceLanguageCode);
            Locale targetLanguage = parseLanguage(targetLanguageCode);

            // Create the project
            project = ProjectFactory.createProject(filename, fileInputStream);

            // Retrieve the xlf
            File xlf = new XliffGenerator(sourceLanguage, targetLanguage, project.getFile(), segmentation).generate();

            // Create response
            response = Response
                    .status(Response.Status.OK)
                    .entity(JSONResponseFactory.getConvertSuccess(xlf))
                    .build();

            everythingOk = true;
            LOGGER.info("[CONVERSION FINISHED] {}: {} to {}", filename, sourceLanguageCode, targetLanguageCode);
        }

        // If there is any error, return it
        catch (Exception e) {
            String errorMessage;
            if (e instanceof OkapiUnexpectedRevisionException) {
                errorMessage = "Document contains revisions or comments, please review and remove them.";
            } else {
                errorMessage = e.getMessage();
            }
            response =  Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(JSONResponseFactory.getError(errorMessage))
                    .build();
            LOGGER.error("[CONVERSION REQUEST FAILED] {}", errorMessage, e);
        }

        // Close the project and streams
        finally {
            if (fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (IOException ignored) {}
            if (project != null)
                // Delete folder only if everything went well
                project.close(everythingOk);
        }

        return response;
    }


    /**
     * Parse the language code into Locales
     */
    private Locale parseLanguage(String languageCode) throws ServerException {

        // Parse the code
        Locale language = Locale.forLanguageTag(
                languageCode);

        // Validate language
        try {
            language.getISO3Language();
            return language;
        }

        // If there is any error, throw a ServerException
        catch (MissingResourceException e) {
            throw new ServerException("The language '" + languageCode + "' is not valid");
        }
    }

}
