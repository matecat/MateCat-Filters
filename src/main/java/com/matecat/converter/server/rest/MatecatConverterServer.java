package com.matecat.converter.server.rest;

import com.matecat.converter.server.rest.resources.ConvertToXliffResource;
import com.matecat.converter.server.rest.resources.ExtractOriginalFileResource;
import com.matecat.converter.server.rest.resources.GenerateDerivedFileResource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.net.InetAddress;
import java.util.logging.Logger;


/**
 * Matecat converter server
 */
public class MatecatConverterServer {

    // Default and used port
    private static final int DEFAULT_PORT = 8082;
    private int serverPort;


    /**
     * Constructor which will use the default port
     * @throws Exception
     */
    public MatecatConverterServer() throws Exception {
        this(DEFAULT_PORT);
    }

    /**
     * Constructor admitting a configured port
     * @param serverPort Port to use
     * @throws Exception
     */
    public MatecatConverterServer(int serverPort) throws Exception {
        this.serverPort = serverPort;
        Server server = initServer();
        server.start();
        String ip = InetAddress.getLocalHost().getHostAddress();
        Logger.getLogger("Server").info( "\n" +
                        "############################################\n" +
                        "###   MATECAT CONVERTER SERVER STARTED\n" +
                        "###   > IP: " + ip + "\n" +
                        "###   > PORT: " + serverPort + "\n" +
                        "############################################\n");
        server.join();

    }


    /**
     * Initialize the resources and other aspects of the server
     * @return Server instance
     */
    private Server initServer() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages(ConvertToXliffResource.class.getPackage().getName());
        resourceConfig.packages(GenerateDerivedFileResource.class.getPackage().getName());
        resourceConfig.packages(ExtractOriginalFileResource.class.getPackage().getName());
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
