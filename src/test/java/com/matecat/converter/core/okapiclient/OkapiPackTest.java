package com.matecat.converter.core.okapiclient;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;


public class OkapiPackTest {

    File samplePack;

    @Before
    public void setUp() throws Exception {
        samplePack = new File(getClass().getResource(File.separator + "samplepack").getPath());
    }

    @Test
    public void testOkapiPack() throws Exception {

        // Build the pack
        OkapiPack pack = new OkapiPack(samplePack);


        // Test original file
        File originalFile = pack.getOriginalFile();

        // It's a real file
        assertNotNull(originalFile);
        assertTrue(originalFile.exists());
        assertFalse(originalFile.isDirectory());

        // It's filename is 'Oviedo.docx'
        assertEquals("Oviedo.docx", originalFile.getName());


        // Test the manifest
        File manifest = pack.getManifest();

        // It's a real file
        assertNotNull(manifest);
        assertTrue(manifest.exists());
        assertFalse(manifest.isDirectory());

        // It's filename is 'manifest.rkm'
        assertEquals("manifest.rkm", manifest.getName());


        // Test the xlf
        File xlf = pack.getXlf();

        // It's a real file
        assertNotNull(xlf);
        assertTrue(xlf.exists());
        assertFalse(xlf.isDirectory());

        // It's filename is 'Oviedo.docx.xlf'
        assertEquals(originalFile.getName() + ".xlf", xlf.getName());

    }


    @Test(expected = IllegalArgumentException.class)
    public void testOkapiPackNullFolder() throws Exception {

        // Build the pack
        new OkapiPack(null);

    }

    @Test
    public void testOkapiPackEmptyFolder() throws Exception {

        // Empty folder
        File emptyFolder = new File(samplePack.getParentFile().getPath() + File.separator + "empty");
        emptyFolder.mkdir();

        // Build the pack
        try {
            new OkapiPack(emptyFolder);
            assertTrue(false);
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        finally {
            emptyFolder.delete();
        }

    }
}