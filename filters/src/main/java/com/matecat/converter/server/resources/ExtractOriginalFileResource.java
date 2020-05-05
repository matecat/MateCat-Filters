package com.matecat.converter.server.resources;

import com.matecat.converter.core.XliffProcessor;
import com.matecat.converter.core.project.Project;
import com.matecat.converter.core.project.ProjectFactory;
import com.matecat.converter.server.JSONResponseFactory;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

    // Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ExtractOriginalFileResource.class);


    /**
     * Extract the original file from the xlf
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response convert(@FormDataParam("file") InputStream fileInputStream) {

        // Logging
        LOGGER.info("XLIFF > SOURCE request");

        Project project = null;
        Response response;
        boolean everythingOk = false;
        try {

            // Check that the input file is not null
            if (fileInputStream == null)
                throw new IllegalArgumentException("The input file has not been sent");

            // Create the project
            project = ProjectFactory.createProject("to-original.xlf", fileInputStream);

            // Retrieve the xlf
            File originalFile = new XliffProcessor(project.getFile()).getOriginalFile();

            // Create response
            response = Response
                    .status(Response.Status.OK)
                    .entity(JSONResponseFactory.getDerivedSuccess(originalFile))
                    .build();

            everythingOk = true;
            LOGGER.info("Successfully returned source file");
        }

        // If there is any error, return it
        catch (Exception e) {
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(JSONResponseFactory.getError(e.getMessage()))
                    .build();
            LOGGER.error("Exception extracting source file from XLIFF", e);
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

}
