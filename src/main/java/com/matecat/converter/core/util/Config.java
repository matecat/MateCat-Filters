package com.matecat.converter.core.util;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.matecat.converter.core.okapiclient.customfilters.ICustomFilter;


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
    public static final String customSegmentationFolder;
    
    
    static {
        try (InputStream inputStream = System.class.getResourceAsStream("/config.properties")) {
            Properties props = new Properties();
            props.load(inputStream);

            serverPort = Integer.parseInt(props.getProperty("server-port"));

            LOGGER.info("Reading cache folder property: cache-folder");
            String cacheFolderVal = checkFolderValidity(props.getProperty("cache-folder"), true, true);
            if (cacheFolderVal.isEmpty()) {
                // Can be here because:
                // 1 - this param is missing
                // 2 - the param isn't a valid folder (see previous block)
                // In both case use the fallback: the OS's temp folder
                final File tmpFile = File.createTempFile("where-am-i", ".matecat");
                cacheFolderVal = tmpFile.getParentFile().getCanonicalPath();
                LOGGER.error("Bad value for config param cache-folder: " + cacheFolderVal + "; set at default OS' temp folder");
            }
            cacheFolder = cacheFolderVal;


            LOGGER.info("Reading error folder property: errors-folder");
            errorsFolder = checkFolderValidity(props.getProperty("errors-folder"), true, true);
            if (errorsFolder.isEmpty()) {
            	LOGGER.error("Bad value for config param error-folder; errors backup disabled");
            }

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
            
            
            // load the custom segmentation directory value
            LOGGER.info("Reading custom segmentation folder property: custom-segmentation-folder");
            customSegmentationFolder = checkFolderValidity(props.getProperty("custom-segmentation-folder"), false, false);
            if( customSegmentationFolder.isEmpty() ) {
            	LOGGER.warn("Custom segmentation folder provided in config file is not valid. Default segmentation file will be used");
            }

        }
        catch (Exception e) {
            throw new RuntimeException("Exception while loading config.properties.", e);
        }
    }

    
    /**
     * Check the validity of an user provided folder.
     * 
     * @param folderPath
     * @param createIfNotExists
     * @param checkWritePermission
     * @return the path to the folder if it exists is valid and access permission are satisfied, an empty string otherwise. In case a path is returned, it will always end with a slash ('/')
     */
    static String checkFolderValidity(String folderPath, boolean createIfNotExists, boolean checkWritePermission) {
        // no path provided
        if(folderPath == null || folderPath.trim().isEmpty()) {
        	LOGGER.info("Empty path");
        	return "";
        }

		File folder = new File(folderPath);
	
		// path provided, but the directory does not exist
		if(!folder.isDirectory()) {
			LOGGER.warn("Folder " + folderPath + " does not exist.");
			   			
			// not asked to create it
			if(!createIfNotExists) {
            	throw new RuntimeException("Folder " + folderPath + " provided in config file does not exist");
			}
			
			// failure when attempting to create it
			if(!folder.mkdirs()) {
				throw new RuntimeException("Failed to create path: " + folderPath + ".");
			}
		}
		
		// path provided, directory exists but cannot read because of permission issues
		if(!folder.canRead()) {
			throw new RuntimeException("No read permission for folder: " + folderPath + ".");
		}

		// need write permission, but is not available
		if(checkWritePermission && !folder.canWrite()) {
			throw new RuntimeException("No write permission for folder: " + folderPath + ".");
		}

		// everything's alright, folder does exist and apparently no permission issues occurred. Make sure the path ends  with a '/'
		LOGGER.info("Successfully loaded folder: " + folderPath);
		return folderPath.endsWith("/") ? folderPath : folderPath + "/";
    }
    
    
    
    /**
     * Private constructor (static class)
     */
    private Config() {}

}
