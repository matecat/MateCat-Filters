package com.matecat.converter.core.project;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;

public class ProjectFactoryTest {

    private File file;
    private Project project, project2;

    @Before
    public void setUp() throws Exception {
        file = new File(getClass().getResource("/project/test.docx").getPath());
        project = ProjectFactory.createProject(file.getName(), new FileInputStream(file));
        project2 = ProjectFactory.createProject(file.getName(), new FileInputStream(file));
    }

    @After
    public void tearDown() throws Exception {
        project.close(true);
        project2.close(true);
    }

    @Test
    public void testCreation() throws Exception {
        assertTrue(project.getFolder().exists());
        assertTrue(project.getFolder().isDirectory());
        assertTrue(project.getFile().exists());
        assertFalse(project.getFile().isDirectory());
        assertEquals(file.getName(), project.getFile().getName());
        assertEquals(file.getTotalSpace(), project.getFile().getTotalSpace());
    }

    @Test
    public void testCreateMultipleProjects() throws Exception {
        assertNotSame(project.getFile().getPath(), project2.getFile().getPath());
    }

}