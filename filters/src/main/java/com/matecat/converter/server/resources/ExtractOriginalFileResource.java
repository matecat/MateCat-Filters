package com.matecat.converter.server.resources;

import com.matecat.converter.core.XliffProcessor;
import com.matecat.converter.core.project.Project;
import com.matecat.converter.core.project.ProjectFactory;
import com.matecat.converter.server.JSONResponseFactory;
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

/**
 * Resource taking care of the extraction of the original file from the .XLF
 */
@Path("/AutomationService/xliff2source")
public class ExtractOriginalFileResource extends BaseResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtractOriginalFileResource.class);

    /**
     * Extract the original file from the xlf
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response convert(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader,
            @FormDataParam("debugMode") @DefaultValue("false") boolean debugMode) {
        traceStart(Operations.XLIFF2SOURCE);
        try {
            // If debug mode requested install the log capturer
            setupDebugMode(debugMode);

            LOGGER.info("XLIFF > SOURCE request");
            MDC.put(FILENAME, contentDispositionHeader.getFileName());

            Project project = null;
            File originalFile = new File("");
            String errorMessage = "Unknown error";
            Response response;
            boolean everythingOk = false;
            try {
                // Check that the input file is not null
                if (fileInputStream == null) {
                    throw new IllegalArgumentException("The input file has not been sent");
                }

                // Create the project
                project = ProjectFactory.createProject("to-original.xlf", fileInputStream);
                MDC.put(CACHE_ID, project.getFolder().getName());
                MDC.put(FILESIZE, String.valueOf(project.getFile().length()));

                // Retrieve the xlf
                originalFile = new XliffProcessor(project.getFile()).getOriginalFile();
                MDC.put(SOURCE_FILENAME, originalFile.getName());
                MDC.put(SOURCE_FILESIZE, String.valueOf(originalFile.length()));

                // Set OK flag
                everythingOk = true;
                LOGGER.info("Successfully returned source file");
            } catch (Exception e) {
                // save the error message
                errorMessage = prepareErrorMessage(e);
                LOGGER.error("Exception converting XLIFF to source", e);
            } finally {
                // Create response
                if (everythingOk) {
                    response = Response
                            .status(Response.Status.OK)
                            .entity(JSONResponseFactory.getDerivedSuccess(originalFile, LOG_CAPTURER.getStoredLog()))
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

}
