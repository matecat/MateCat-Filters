package com.matecat.converter.core.project;

import com.matecat.converter.server.exceptions.ProjectCreationException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;


/**
 * Factory in charge of creating projects, in temporal folders
 */
public class ProjectFactory {

    /**
     * Private constructor to make the class static
     */
    private ProjectFactory() {}

    /**
     * Create a project given the filename and content of a file
     * @param filename Filename
     * @param uploadedInputStream Input stream containing the contents of the file
     * @return Project containing the file
     */
    public static Project createProject(String filename, InputStream uploadedInputStream) {
        try {
            // Create temporal directory
            File folder = Files.createTempDirectory("matecat-converter-").toFile();

            // Save the file inside the temporal
            File file = file = new File(folder.getPath() + File.separator + filename);
            FileUtils.copyInputStreamToFile(uploadedInputStream, file);

            // Return the project
            return new Project(file);
        }
        catch (IOException e) {
           throw new ProjectCreationException(String.format("It was not possible to create a project for the file '%s'", filename));
        }
    }

}