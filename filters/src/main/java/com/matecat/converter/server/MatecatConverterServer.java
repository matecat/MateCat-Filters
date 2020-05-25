package com.matecat.converter.server;

import com.matecat.converter.core.util.Config;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;


/**
 * Matecat converter server
 */
public class MatecatConverterServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatecatConverterServer.class);
    private Server server;
    private final int serverPort;
    private static String localIP, externalIP;


    /**
     * Constructor which will use the default port
     */
    public MatecatConverterServer() {
        this.serverPort = Config.serverPort;
        init();
    }


    /**
     * Constructor admitting a configured port
     *
     * @param serverPort Port to use
     */
    public MatecatConverterServer(int serverPort) {
        if (serverPort < 0)
            throw new IllegalArgumentException("The port specified in the configuration is not valid");
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
     *
     * @return True if started, false otherwise
     */
    public boolean isStarted() {
        return server.isStarted();
    }


    /**
     * Check if the server is stopped
     *
     * @return True if stopped, false otherwise
     */
    public boolean isStopped() {
        return server.isStopped();
    }


    /**
     * Get external IP
     *
     * @return External IP
     */
    public static String getExternalIP() {
        if (externalIP == null) {
            try {
                URL whatismyip = new URL("http://checkip.amazonaws.com");
                BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
                externalIP = in.readLine();
                in.close();
            } catch (IOException ignored) {
            }
        }
        return externalIP;
    }


    /**
     * Get local IP
     *
     * @return Local IP
     */
    public static String getLocalIP() {
        if (localIP == null) {
            // this method is deemed to be more reliable and cover more networking scenarios
            // it has also the advantage that does not perform any lookup
            // from https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
            try (final DatagramSocket socket = new DatagramSocket()) {
                // the address below does not need to exist or be reachable
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                localIP = socket.getLocalAddress().getHostAddress();
            } catch (UnknownHostException | SocketException e) {
                // fallback using InetAddress which performs a DNS lookup
                try {
                    localIP = InetAddress.getLocalHost().getHostAddress();
                } catch (IOException ignored) {
                    throw new RuntimeException("Unable to determine local IP address");
                }
            }
        }
        return localIP;
    }

    /**
     * Init the server
     */
    private void init() {
        try {
            initServer();
            server.start();
            LOGGER.info("Server started at {}:{} / {}:{}", getExternalIP(), serverPort, getLocalIP(), serverPort);
        } catch (BindException e) {
            LOGGER.error("Port " + serverPort + " already in use");
            System.exit(-1);
        } catch (InterruptedException e) {
            LOGGER.error("Server has been interrupted", e);
            throw new RuntimeException("The server has been interrupted", e);
        } catch (Exception e) {
            LOGGER.error("Internal server problem", e);
            throw new RuntimeException("Internal server problem", e);
        }
    }


    /**
     * Initialize the resources and other aspects of the server
     */
    private void initServer() {
        // Configure the server
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages("com.matecat.converter.server.resources");
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