package com.matecat.converter.server.resources;

import com.matecat.converter.core.project.Project;
import com.matecat.converter.core.project.ProjectFactory;
import com.matecat.converter.server.JSONResponseFactory;
import com.matecat.filters.basefilters.FiltersRouter;
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
 * Resource taking care of the generation of the new file from the .XLF
 */
@Path("/AutomationService/xliff2original")
public class GenerateDerivedFileResource {

    // Logger
    private static Logger LOGGER = LoggerFactory.getLogger(ConvertToXliffResource.class);

    /**
     * Generate the derived file from the xlf
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response convert(
            @FormDataParam("xliffContent") InputStream fileInputStream) {

        // Logging
        LOGGER.info("XLIFF > TARGET request");

        Project project = null;
        Response response = null;
        boolean everythingOk = false;
        try {

            // Check that the input file is not null
            if (fileInputStream == null)
                throw new IllegalArgumentException("The input file has not been sent");

            // Create the project
            project = ProjectFactory.createProject("to-derived.xlf", fileInputStream);

            // Retrieve the xlf
            File derivedFile = new FiltersRouter().merge(project.getFile());

            // Create response
            response = Response
                    .status(Response.Status.OK)
                    .entity(JSONResponseFactory.getDerivedSuccess(derivedFile))
                    .build();

            everythingOk = true;
            LOGGER.info("Successfully returned target file");
        }

        // If there is any error, return it
        catch (Exception e) {
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(JSONResponseFactory.getError(e.getMessage()))
                    .build();
            LOGGER.error("Exception converting XLIFF to target", e);
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
