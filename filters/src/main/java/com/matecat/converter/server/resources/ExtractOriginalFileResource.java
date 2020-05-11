package com.matecat.converter.server.resources;

import com.matecat.converter.core.XliffProcessor;
import com.matecat.converter.core.project.Project;
import com.matecat.converter.core.project.ProjectFactory;
import com.matecat.converter.server.JSONResponseFactory;
import com.matecat.logging.StoringAppender;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Resource taking care of the extraction of the original file from the .XLF
 */
@Path("/AutomationService/xliff2source")
public class ExtractOriginalFileResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertToXliffResource.class);
    private static final StoringAppender LOG_CAPTURER = new StoringAppender();

    /**
     * Extract the original file from the xlf
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response convert(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("debugMode") @DefaultValue("false") boolean debugMode) {
        // If debug mode requested install the log capturer
        if (debugMode) {
            LOG_CAPTURER.install();
        }

        LOGGER.info("XLIFF > SOURCE request");

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

            // Retrieve the xlf
            originalFile = new XliffProcessor(project.getFile()).getOriginalFile();

            // Set OK flag
            everythingOk = true;
            LOGGER.info("Successfully returned source file");
        } catch (Exception e) {
            // Save error message
            errorMessage = e.toString();
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
            if (fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (IOException ignored) {
                }
            if (project != null)
                // Delete folder only if everything went well
                project.close(everythingOk);
            // If debug mode requested de-install the log capturer
            if (debugMode) {
                LOG_CAPTURER.clear();
                LOG_CAPTURER.deinstall();
            }
        }
        return response;
    }

}
