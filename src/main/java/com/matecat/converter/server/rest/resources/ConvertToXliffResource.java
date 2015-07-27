package com.matecat.converter.server.rest.resources;

import com.matecat.converter.core.XliffGenerator;
import com.matecat.converter.core.project.Project;
import com.matecat.converter.core.project.ProjectFactory;
import com.matecat.converter.server.exceptions.ServerException;
import com.matecat.converter.server.rest.JSONResponseFactory;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.logging.Logger;


/**
 * Resource taking care of the conversion task into .XLF
 *
 * The call is:
 * [POST]: /convert/{source-language}/{target-language}
 *
 * Sending the file to convert as POST body with multipart/form-data encoding
 *
 * The result is returned as JSON, to obey the old Matecat library (TODO replace for file content)
 */
@Path("/convert")
public class ConvertToXliffResource {

    // Logger
    private static Logger LOGGER = Logger.getLogger("ConvertResource");

    /**
     * Convert a file into XLF
     * @param sourceLanguageCode Source language
     * @param targetLanguageCode target language
     * @param fileInputStream Input file
     * @param contentDispositionHeader Inputfile
     * @return Converted file
     */
    @POST
    @Path("/{source-lang}/{target-lang}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response convert(
            @PathParam("source-lang") String sourceLanguageCode,
            @PathParam("target-lang") String targetLanguageCode,
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {

        // Filename
        String filename = FilenameUtils.getName(contentDispositionHeader.getFileName());

        // Logging
        LOGGER.info(String.format("# [CONVERSION REQUEST] %s: %s to %s", filename, sourceLanguageCode, targetLanguageCode));

        try {

            // Parse the codes
            Locale sourceLanguage = parseLanguage(sourceLanguageCode);
            Locale targetLanguage = parseLanguage(targetLanguageCode);

            // Create the project
            Project project = ProjectFactory.createProject(filename, fileInputStream);
            LOGGER.info(String.format("# PROJECT [%s]: %s", filename, project.getFolder().getPath()));

            // Retrieve the xlf
            File xlf = new XliffGenerator(sourceLanguage, targetLanguage, project.getFile()).generate();

            // Create response
            Response response = Response
                    .status(Response.Status.OK)
                    .entity(JSONResponseFactory.getSuccess(xlf))
                    .build();

            // Remove and return the response
            project.delete();

            // Return the response
            return response;

        }

        // If there is any error, return it
        catch (Exception e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(JSONResponseFactory.getError(e.getMessage()))
                    .build();
        }

    }


    /**
     * Parse the language code into Locales
     * @param languageCode Language code
     * @return Locale which corresponds to the code
     * @throws ServerException If the code is not valid
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
            throw new ServerException("The source language '" + languageCode + "' is not valid");
        }
    }

}
