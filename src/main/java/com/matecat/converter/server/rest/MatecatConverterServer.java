package com.matecat.converter.server.rest;

import com.matecat.converter.server.rest.ConvertResource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.net.InetAddress;
import java.util.logging.Logger;


public class MatecatConverterServer {

    private static final int DEFAULT_PORT = 8082;
    private int serverPort;

    public MatecatConverterServer() throws Exception {
        this(DEFAULT_PORT);
    }

    public MatecatConverterServer(int serverPort) throws Exception {
        this.serverPort = serverPort;
        Server server = configureServer();
        server.start();
        String ip = InetAddress.getLocalHost().getHostAddress();
        Logger.getLogger("Server").info( "\n" +
                        "############################################\n" +
                        "###   MATECAT SERVER STARTED\n" +
                        "###   > IP: " + ip + "\n" +
                        "###   > PORT: " + serverPort + "\n" +
                        "############################################");
        server.join();

    }

    private Server configureServer() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages(ConvertResource.class.getPackage().getName());
        resourceConfig.register(JacksonFeature.class);
        resourceConfig.register(MultiPartFeature.class);
        ServletContainer servletContainer = new ServletContainer(resourceConfig);
        ServletHolder sh = new ServletHolder(servletContainer);
        Server server = new Server(serverPort);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(sh, "/*");
        server.setHandler(context);
        return server;
    }


}
