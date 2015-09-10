package com.matecat.converter.core.project;

import com.matecat.converter.core.util.Config;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;


/**
 * Factory in charge of creating projects, in temporal folders
 */
public class ProjectFactory {

    // Logger
    private static Logger LOGGER = LoggerFactory.getLogger(ProjectFactory.class);

    // Storage folder
    private static final File STORAGE_FOLDER;
    static {
        // Try to get the defined folder
        String storagePath = Config.storageFolder;
        if (storagePath != null  &&  !storagePath.equals("")) {
            LOGGER.info("[GLOBAL] Storage path configuration found: {}", storagePath);
            File folder = new File(storagePath);
            if (folder.exists() && folder.isDirectory() && folder.canRead() && folder.canWrite()) {
                STORAGE_FOLDER = folder;
            }
            else {
                LOGGER.error("[GLOBAL] The storage path '{}' is not valid; OS' temp default folder will be used", storagePath);
                STORAGE_FOLDER = null;
            }
        }
        else {
            STORAGE_FOLDER = null;
        }
    }

    /**
     * Private constructor to make the class static
     */
    private ProjectFactory() {}

    /**
     * Create a project given the filename and content of a file
     * @param filename Filename
     * @param uploadedInputStream Input stream containing the contents of the file. It will be closed after processing.
     * @return Project containing the file
     */
    public static Project createProject(String filename, InputStream uploadedInputStream) {
        try {

            // Load the folder
            File folder = STORAGE_FOLDER != null ?
                    Files.createTempDirectory(STORAGE_FOLDER.toPath(), "").toFile()
                    : Files.createTempDirectory("matecat-converter-").toFile();

            // Save the file inside the temporal
            File file = new File(folder.getPath() + File.separator + filename);
            FileUtils.copyInputStreamToFile(uploadedInputStream, file);

            // Return the project
            Project project = new Project(file);
            LOGGER.info("[PROJECT CREATED] {} saved in {}", filename, project.getFolder().getPath());
            return project;
        }
        catch (IOException e) {
            LOGGER.error("[PROJECT CREATION ERROR]: {}", e.getMessage(), e);
            throw new ProjectCreationException(String.format("It was not possible to create a project for the file '%s'", filename));
        }
    }

}