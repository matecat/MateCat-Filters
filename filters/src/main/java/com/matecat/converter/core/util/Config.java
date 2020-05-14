package com.matecat.converter.core.util;

import com.matecat.filters.basefilters.DefaultFilter;
import com.matecat.filters.basefilters.IFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.*;


/**
 * Configuration static class, which provides access to the properties specified in the configuration file
 */
public class Config {

    // Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    // Configuration params
    public static final int serverPort;
    public static final String cacheFolder;
    public static final String errorsFolder;
    public static final boolean deleteOnClose;
    public static final boolean winConvEnabled;
    public static final List<String> winConvertersOcr;
    public static final List<String> winConvertersNoOcr;
    public static final List<Class> customFilters;
    public static final String customSegmentationFolder;


    static {
        try (InputStream inputStream = System.class.getResourceAsStream("/config.properties")) {
            Properties props = new Properties();
            props.load(inputStream);
            LOGGER.debug("Loaded configuration:");

            serverPort = Integer.parseInt(props.getProperty("server-port"));
            LOGGER.debug("  Server port: {}", serverPort);

            String cacheFolderVal = checkFolderValidity(props.getProperty("cache-folder"), true, true);
            if (cacheFolderVal.isEmpty()) {
                // Can be here because:
                // 1 - this param is missing
                // 2 - the param isn't a valid folder (see previous block)
                // In both case use the fallback: the OS's temp folder
                final File tmpFile = File.createTempFile("where-am-i", ".matecat");
                cacheFolderVal = tmpFile.getParentFile().getCanonicalPath();
                LOGGER.warn("cache-folder param empty or invalid: caching in OS temp folder");
            }
            cacheFolder = cacheFolderVal;
            LOGGER.debug("  Cache folder: {}", cacheFolder);

            errorsFolder = checkFolderValidity(props.getProperty("errors-folder"), true, true);
            if (errorsFolder.isEmpty()) {
                LOGGER.warn("error-folder param empty or invalid: errors backup disabled");
            }
            LOGGER.debug("  Error folder: {}", errorsFolder);

            deleteOnClose = Boolean.parseBoolean(props.getProperty("delete-on-close"));
            LOGGER.debug("  Delete on close: {}", deleteOnClose);

            winConvEnabled = Boolean.parseBoolean(props.getProperty("win-conv-enabled"));
            LOGGER.debug("  WinConverter enabled: {}", winConvEnabled);

            String winConvertersOcrValue = props.getProperty("win-converters-ocr");
            if (winConvertersOcrValue != null) {
                winConvertersOcr = Arrays.asList(winConvertersOcrValue.split(","));
            } else {
                winConvertersOcr = new ArrayList<>();
            }
            LOGGER.debug("  WinConverters with OCR: {}", winConvertersOcr);

            String winConvertersNoOcrValue = props.getProperty("win-converters-no-ocr");
            if (winConvertersNoOcrValue != null) {
                winConvertersNoOcr = Arrays.asList(winConvertersNoOcrValue.split(","));
            } else {
                winConvertersNoOcr = new ArrayList<>();
            }
            LOGGER.debug("  WinConverters without OCR: {}", winConvertersNoOcr);

            // if win converter enabled check lists of converter addresses is not empty
            if (winConvEnabled && winConvertersOcr.isEmpty() && winConvertersNoOcr.isEmpty()) {
                throw new RuntimeException("WinConverter enabled but no WinConverter address specified");
            }

            String filtersString = props.getProperty("custom-filters");
            List<Class> filtersList = new ArrayList<>();
            if (filtersString != null) {
                String[] filtersNames = filtersString.split(",");
                for (String filterName : filtersNames) {
                    filterName = filterName.trim();
                    Class filter = Class.forName(filterName);
                    if (!IFilter.class.isAssignableFrom(filter)) {
                        throw new RuntimeException("Exception loading custom filters classes: the class " + filterName + " doesn't implement the IFilter interface.");
                    }
                    filtersList.add(filter);
                }
            }
            filtersList.add(DefaultFilter.class);
            customFilters = Collections.unmodifiableList(filtersList);
            LOGGER.debug("  Custom filter classes: {}", customFilters);

            // load the custom segmentation directory value
            customSegmentationFolder = checkFolderValidity(props.getProperty("custom-segmentation-folder"), false, false);
            if (customSegmentationFolder.isEmpty()) {
                LOGGER.warn("custom-segmentation-folder param empty or invalid: custom segmentation disabled");
            }
            LOGGER.debug("  Custom segmentation folder: {}", customSegmentationFolder);

        } catch (Exception e) {
            throw new RuntimeException("Exception while loading config.properties.", e);
        }
    }


    /**
     * Check the validity of an user provided folder.
     *
     * @param folderPath           the path of the folder
     * @param createIfNotExists    if true tries to create the folder if not exists
     * @param checkWritePermission if true checks if can read from folder
     * @return the path to the folder if it exists is valid and access permission are satisfied, an empty string otherwise. In case a path is returned, it will always end with a slash ('/')
     */
    static String checkFolderValidity(String folderPath, boolean createIfNotExists, boolean checkWritePermission) {
        // no path provided
        if (folderPath == null || folderPath.trim().isEmpty()) {
            return "";
        }

        File folder = new File(folderPath);

        // path provided, but the directory does not exist
        if (!folder.isDirectory()) {

            // not asked to create it
            if (!createIfNotExists) {
                throw new RuntimeException("Folder " + folderPath + " provided in config file does not exist");
            }

            // failure when attempting to create it
            if (!folder.mkdirs()) {
                throw new RuntimeException("Failed to create path: " + folderPath + ".");
            }
        }

        // path provided, directory exists but cannot read because of permission issues
        if (!folder.canRead()) {
            throw new RuntimeException("No read permission for folder: " + folderPath + ".");
        }

        // need write permission, but is not available
        if (checkWritePermission && !folder.canWrite()) {
            throw new RuntimeException("No write permission for folder: " + folderPath + ".");
        }

        // everything's alright, folder does exist and apparently no permission issues occurred. Make sure the path ends  with a '/'
        return folderPath.endsWith("/") ? folderPath : folderPath + "/";
    }


    /**
     * Private constructor (static class)
     */
    private Config() {
    }

}
