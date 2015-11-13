package com.matecat.converter.core.util;

import com.matecat.converter.core.okapiclient.customfilters.ICustomFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;


/**
 * Configuration static class, which provides access to the properties specified in the configuration file
 */
public class Config {

    // Logger
    private static Logger LOGGER = LoggerFactory.getLogger(Config.class);

    // Configuration params
    public static final int serverPort;
    public static final String cacheFolder;
    public static final String errorsFolder;
    public static final boolean deleteOnClose;
    public static final boolean winConvEnabled;
    public static final String winConvHost;
    public static final int winConvPort;
    public static final List<Class> customFilters;

    static {
        try (InputStream inputStream = System.class.getResourceAsStream("/config.properties")) {
            Properties props = new Properties();
            props.load(inputStream);

            serverPort = Integer.parseInt(props.getProperty("server-port"));

            String cacheFolderVal = props.getProperty("cache-folder", "").trim();
            if (!cacheFolderVal.isEmpty()) {
                // Try to extract a valid folder from the param value
                File folder = new File(cacheFolderVal);
                folder.mkdirs();
                if (folder.exists() && folder.isDirectory() && folder.canRead() && folder.canWrite()) {
                    cacheFolderVal = folder.getCanonicalPath();
                } else {
                    LOGGER.error("Bad value for config param cache-folder: " + cacheFolderVal + "; set at default OS' temp folder");
                    cacheFolderVal = "";
                }
            }
            if (cacheFolderVal.isEmpty()) {
                // Can be here because:
                // 1 - this param is missing
                // 2 - the param isn't a valid folder (see previous block)
                // In both case use the fallback: the OS's temp folder
                final File tmpFile = File.createTempFile("where-am-i", ".matecat");
                cacheFolderVal = tmpFile.getParentFile().getCanonicalPath();
            }
            cacheFolder = cacheFolderVal;


            String errorsFolderVal = props.getProperty("errors-folder", "").trim();
            if (!errorsFolderVal.isEmpty()) {
                try {
                    errorsFolderVal = new File(errorsFolderVal).getCanonicalPath();
                } catch (Exception e) {
                    errorsFolderVal = "";
                    LOGGER.error("Bad value for config param error-folder: " + errorsFolderVal + "; errors backup disabled");
                }
            }
            errorsFolder = errorsFolderVal;

            deleteOnClose = Boolean.parseBoolean(props.getProperty("delete-on-close"));
            winConvEnabled = Boolean.parseBoolean(props.getProperty("win-conv-enabled"));
            winConvHost = props.getProperty("win-conv-host");
            winConvPort = Integer.parseInt(props.getProperty("win-conv-port"));

            String customFiltersString = props.getProperty("custom-filters");
            if (customFiltersString == null) {
                customFilters = Collections.unmodifiableList(new ArrayList<>());
            } else {
                List<Class> customFiltersList = new ArrayList<>();
                String[] customFiltersNames = props.getProperty("custom-filters").split(",");
                for (String customFilterName : customFiltersNames) {
                    String cleanCustomFilterName = customFilterName.trim();
                    Class customFilter = Class.forName(cleanCustomFilterName);
                    if (!ICustomFilter.class.isAssignableFrom(customFilter)) {
                        throw new RuntimeException("Error loading custom filters classes: the class " + cleanCustomFilterName + " doesn't implement the ICustomFilter interface.");
                    }
                    customFiltersList.add(customFilter);
                }
                customFilters = Collections.unmodifiableList(customFiltersList);
            }
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
