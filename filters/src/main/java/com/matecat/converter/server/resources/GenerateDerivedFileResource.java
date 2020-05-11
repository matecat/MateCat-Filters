package com.matecat.converter.server.resources;

import com.matecat.converter.core.project.Project;
import com.matecat.converter.core.project.ProjectFactory;
import com.matecat.converter.server.JSONResponseFactory;
import com.matecat.filters.basefilters.FiltersRouter;
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
 * Resource taking care of the generation of the new file from the .XLF
 */
@Path("/AutomationService/xliff2original")
public class GenerateDerivedFileResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertToXliffResource.class);
    private static final StoringAppender LOG_CAPTURER = new StoringAppender();

    /**
     * Generate the derived file from the xlf
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response convert(
            @FormDataParam("xliffContent") InputStream fileInputStream,
            @FormDataParam("debugMode") @DefaultValue("false") boolean debugMode) {
        // If debug mode requested install the log capturer
        if (debugMode) {
            LOG_CAPTURER.install();
        }

        LOGGER.info("XLIFF > TARGET request");

        Project project = null;
        File derivedFile = new File("");
        String errorMessage = "Unknown error";
        Response response;
        boolean everythingOk = false;
        try {
            // Check that the input file is not null
            if (fileInputStream == null)
                throw new IllegalArgumentException("The input file has not been sent");

            // Create the project
            project = ProjectFactory.createProject("to-derived.xlf", fileInputStream);

            // Retrieve the xlf
            derivedFile = new FiltersRouter().merge(project.getFile());

            everythingOk = true;
            LOGGER.info("Successfully returned target file");
        } catch (Exception e) {
            // save error message
            errorMessage = e.toString();
            LOGGER.error("Exception converting XLIFF to target", e);
        } finally {
            // Create response
            if (everythingOk) {
                response = Response
                        .status(Response.Status.OK)
                        .entity(JSONResponseFactory.getDerivedSuccess(derivedFile, LOG_CAPTURER.getStoredLog()))
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
