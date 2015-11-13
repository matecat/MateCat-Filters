package com.matecat.converter.server.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;


/**
 * Simple resource used to test if the server is running
 */
@Path("/test")
public class TestConnectionResource {

    // Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(TestConnectionResource.class);

    /**
     * Return a simple ok message
     */
    @GET
    public Response test(@Context HttpServletRequest request) {
        LOGGER.info("[TEST] Connection tested from {}", request.getRemoteAddr());
        return Response
                .status(Response.Status.OK)
                .entity("Server on")
                .build();
    }

}