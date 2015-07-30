package com.matecat.converter.server;

import com.matecat.converter.core.util.Configuration;
import com.matecat.converter.server.resources.ConvertToXliffResource;
import com.matecat.converter.server.resources.ExtractOriginalFileResource;
import com.matecat.converter.server.resources.GenerateDerivedFileResource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.BindException;
import java.net.InetAddress;
import java.util.Properties;


/**
 * Matecat converter server
 */
public class MatecatConverterServer {

    // Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(MatecatConverterServer.class);

    // Port property name in the configuration file
    public static final String PORT_PROPERTY = "server-port";

    // Used port
    private int serverPort;

    // Server
    private Server server;


    /**
     * Constructor which will use the default port
     */
    public MatecatConverterServer() {
        try {
            int port = Integer.parseInt(Configuration.getProperty(PORT_PROPERTY));
            if (port <= 0)
                throw new Exception();
            this.serverPort = port;
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
     * Stop the server
     */
    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            LOGGER.error("It was not possible to stop the server", e);
        }
    }


    /**
     * Check if the server has been started (this is, ready to receive requests)
     * @return True if started, false otherwise
     */
    public boolean isStarted() {
        return server.isStarted();
    }


    /**
     * Check if the server is stopped
     * @return True if stopped, false otherwise
     */
    public boolean isStopped() {
        return server.isStopped();
    }


    /**
     * Init the server
     */
    private void init() {
        try {
            initServer();
            server.start();
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("\n" +
                    "############################################\n" +
                    "###   MATECAT CONVERTER SERVER STARTED\n" +
                    "###   > IP: " + ip + "\n" +
                    "###   > PORT: " + serverPort + "\n" +
                    "############################################\n");
            LOGGER.info("Server started at {}:{}", ip, serverPort);
            //server.join();
        }
        catch (BindException e) {
            LOGGER.error("The port " + serverPort + " is already in use", e);
            System.exit(-1);
        }
        catch (InterruptedException e) {
            LOGGER.error("The server has been interrupted", e);
            throw new RuntimeException("The server has been interrupted");
        } catch (Exception e) {
            LOGGER.error("Unknown internal server problem", e);
            throw new RuntimeException("Unknown internal server problem");
        }
    }


    /**
     * Initialize the resources and other aspects of the server
     */
    private void initServer() {

        // Configure the server
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages(ConvertToXliffResource.class.getPackage().getName());
        resourceConfig.packages(GenerateDerivedFileResource.class.getPackage().getName());
        resourceConfig.packages(ExtractOriginalFileResource.class.getPackage().getName());
        resourceConfig.register(JacksonFeature.class);
        resourceConfig.register(MultiPartFeature.class);
        ServletContainer servletContainer = new ServletContainer(resourceConfig);
        ServletHolder sh = new ServletHolder(servletContainer);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(sh, "/*");

        // Initiate it
        this.server = new Server(serverPort);
        server.setHandler(context);
    }

}