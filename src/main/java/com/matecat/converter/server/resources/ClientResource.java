package com.matecat.converter.server.resources;

import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;


/**
 * Client resource
 */
@Path("/")
public class ClientResource {

    private static final String CLIENT_HTML_PATH = "/client/client.html";
    private static final String CLIENT_HTML;
    static {
        try {
            CLIENT_HTML = IOUtils.toString(System.class.getResourceAsStream(CLIENT_HTML_PATH), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("The client.html was not found");
        }
    }

    /**
     * Output the client
     * @return Client
     */
    @GET
    public Response client(@Context HttpServletRequest request) throws IOException {
        return Response
                .status(Response.Status.OK)
                .entity(CLIENT_HTML)
                .build();
    }

}