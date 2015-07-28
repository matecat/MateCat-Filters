package com.matecat.converter.server.rest;

import com.matecat.converter.core.util.Configuration;
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

import java.net.BindException;
import java.net.InetAddress;
import java.util.logging.Logger;


/**
 * Matecat converter server
 */
public class MatecatConverterServer {

    // Port property name in the configuration file
    public static final String PORT_PROPERTY = "server-port";

    // Used port
    private int serverPort;


    /**
     * Constructor which will use the default port
     */
    public MatecatConverterServer() {
        try {
            int port = Integer.parseInt(Configuration.getProperty(PORT_PROPERTY));
            if (port < 0)
                throw new Exception();
            init();
        }
        catch (Exception e) {
            throw new RuntimeException("There is no default port specified in the configuration");
        }
    }


    /**
     * Constructor admitting a configured port
     * @param serverPort Port to use
     */
    public MatecatConverterServer(int serverPort) {
        if (serverPort < 0)
            throw new IllegalArgumentException("There port specified in the configuration is not valid");
        this.serverPort = serverPort;
        init();
    }


    /**
     * Init the server
     */
    private void init() {
        try {
            Server server = initServer();
            server.start();
            String ip = InetAddress.getLocalHost().getHostAddress();
            Logger.getLogger("Server").info("\n" +
                    "############################################\n" +
                    "###   MATECAT CONVERTER SERVER STARTED\n" +
                    "###   > IP: " + ip + "\n" +
                    "###   > PORT: " + serverPort + "\n" +
                    "############################################\n");
            server.join();
        }
        catch (BindException e) {
            throw new RuntimeException("The port " + serverPort + " is already in use");
        }
        catch (InterruptedException e) {
            throw new RuntimeException("The server has been interrupted");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unknown internal server problem");
        }
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