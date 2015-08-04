package com.matecat.converter.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Configuration static class, which provides access to the properties specified in the configuration file
 */
public class Configuration {

    // Initialize properties
    private static final Properties properties;
    static {
        try (InputStream inputStream = Configuration.class.getResourceAsStream("/config.properties")) {
            properties = new Properties();
            properties.load(inputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("The configuration file is missing");
        }
    }

    /**
     * Private constructor (static class)
     */
    private Configuration() {}


    /**
     * Get a property
     * @param propertyName Name of the property
     * @return Value of the property, of null if it is not specified
     */
    public static String getProperty(String propertyName) {
        return properties.getProperty(propertyName, null);
    }


}
