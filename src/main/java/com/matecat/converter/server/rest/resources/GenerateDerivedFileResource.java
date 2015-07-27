package com.matecat.converter.server.rest.resources;

import com.matecat.converter.core.XliffProcessor;
import com.matecat.converter.core.project.Project;
import com.matecat.converter.core.project.ProjectFactory;
import com.matecat.converter.server.rest.JSONResponseFactory;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;


/**
 * Resource taking care of the generation of the new file from the .XLF
 *
 * The call is:
 * [POST]: /derived/
 *
 * Sending the xlf file to convert as POST body with multipart/form-data encoding
 *
 * The result is returned as JSON, to obey the old Matecat library (TODO replace for file content)
 */
@Path("/derived")
public class GenerateDerivedFileResource {

    // Logger
    private static Logger LOGGER = Logger.getLogger(GenerateDerivedFileResource.class.getName());

    /**
     * Generate the derived file from the xlf
     * @param fileInputStream Xlf
     * @param contentDispositionHeader Xlf
     * @return Derived file
     */
    @POST
    @Path("/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response convert(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {

        // Filename
        String filename = FilenameUtils.getName(contentDispositionHeader.getFileName());

        // Logging
        LOGGER.info(String.format("# [DERIVATION REQUEST] %s", filename));

        try {

            // Create the project
            Project project = ProjectFactory.createProject(filename, fileInputStream);
            LOGGER.info(String.format("# PROJECT [%s]: %s", filename, project.getFolder().getPath()));

            // Retrieve the xlf
            File derivedFile = new XliffProcessor(project.getFile()).getDerivedFile();

            // Create response
            Response response = Response
                    .status(Response.Status.OK)
                    .entity(JSONResponseFactory.getSuccess(derivedFile))
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

}
