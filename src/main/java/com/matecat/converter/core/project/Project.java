package com.matecat.converter.core.project;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;


/**
 * Project class
 *
 * It represents a project created from one file. A project has it's own folder, and is guaranteed to not have
 * name collisions with other projects.
 *
 * Projects can be created by means of the ProjectFactory class.
 * @see ProjectFactory
 */
public class Project {

    // Inner properties
    private File folder;
    private File file;

    /**
     * Contructor invoked by the factory, receiving a file
     * @param file Project's file
     */
    protected Project(File file) {
        this.file = file;
        this.folder = file.getParentFile();
    }

    /**
     * Get folder
     * @return Project's folder
     */
    public File getFolder() {
        if (folder == null)
            throw new RuntimeException("The project has already been deleted");
        return folder;
    }

    /**
     * Get file
     * @return Project's file
     */
    public File getFile() {
        if (file == null)
            throw new RuntimeException("The project has already been deleted");
        return file;
    }

    /**
     * Delete the project
     *
     * will delete all the files contained in the project, and remove all the inner references to them.
     * One this method is executed, it's not possible to use the project again.
     */
    public void delete() {
        try {
            FileUtils.deleteDirectory(folder);
            this.folder = null;
            this.file = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
