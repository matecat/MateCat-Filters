package com.matecat.converter.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Configuration static class, which provides access to the properties specified in the configuration file
 */
public class Config {

    // Logger
    private static Logger LOGGER = LoggerFactory.getLogger(Config.class);

    // Configuration params
    public static final int serverPort;
    public static final String storageFolder;
    public static final boolean deleteOnClose;
    public static final boolean locEnabled;
    public static final String locHost;
    public static final int locPort;

    static {
        try (InputStream inputStream = System.class.getResourceAsStream("/config.properties")) {
            Properties props = new Properties();
            props.load(inputStream);

            serverPort = Integer.parseInt(props.getProperty("server-port"));
            storageFolder = props.getProperty("storage-folder");
            deleteOnClose = Boolean.parseBoolean(props.getProperty("delete-on-close"));
            locEnabled = Boolean.parseBoolean(props.getProperty("loc-enabled"));
            locHost = props.getProperty("loc-host");
            locPort = Integer.parseInt(props.getProperty("loc-port"));
        }
        catch (Exception e) {
            throw new RuntimeException("Exception while loading config.properties.", e);
        }
    }

    /**
     * Private constructor (static class)
     */
    private Config() {}

}
