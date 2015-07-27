package com.matecat.converter.core.okapiclient;

import com.matecat.converter.core.encoding.Encoding;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Locale;

import static org.junit.Assert.*;


public class OkapiPackTest {

    File incompleteSamplePack;
    File completeSamplePack;

    @Before
    public void setUp() throws Exception {
        incompleteSamplePack = new File(getClass().getResource(File.separator + "samplepack" + File.separator + "incomplete").getPath());
        completeSamplePack = new File(getClass().getResource(File.separator + "samplepack" + File.separator + "complete").getPath());
    }

    @Test
    public void testOkapiPackInit() throws Exception {

        // Build the pack
        OkapiPack pack = new OkapiPack(incompleteSamplePack);


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
        File emptyFolder = new File(incompleteSamplePack.getParentFile().getPath() + File.separator + "empty");
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

    @Test
    public void testOkapiPackDerived() throws Exception {
        File derived = new OkapiPack(completeSamplePack).getDerivedFile();
        assertNotNull(derived);
        assertTrue(derived.exists());
        assertFalse(derived.isDirectory());
    }

    @Test(expected = RuntimeException.class)
    public void testOkapiPackDerivedWithoutInit() throws Exception {
        new OkapiPack(incompleteSamplePack).getDerivedFile();
    }

    @Test
    public void testDelete() throws Exception {
        File newPack = new File(completeSamplePack.getPath() + "_NEW");
        FileUtils.copyDirectory(completeSamplePack, newPack);
        assertTrue(newPack.exists());
        OkapiPack pack = new OkapiPack(newPack);
        pack.delete();
        assertFalse(newPack.exists());
    }

    @Test(expected = RuntimeException.class)
    public void testGetOriginalAfterDelete() throws Exception {
        File newPack = new File(completeSamplePack.getPath() + "_NEW");
        FileUtils.copyDirectory(completeSamplePack, newPack);
        OkapiPack pack = new OkapiPack(newPack);
        pack.delete();
        pack.getOriginalFile();
    }

    @Test(expected = RuntimeException.class)
    public void testGetXLFAfterDelete() throws Exception {
        File newPack = new File(completeSamplePack.getPath() + "_NEW");
        FileUtils.copyDirectory(completeSamplePack, newPack);
        OkapiPack pack = new OkapiPack(newPack);
        pack.delete();
        pack.getXlf();
    }

    @Test(expected = RuntimeException.class)
    public void testGetPackAfterDelete() throws Exception {
        File newPack = new File(completeSamplePack.getPath() + "_NEW");
        FileUtils.copyDirectory(completeSamplePack, newPack);
        OkapiPack pack = new OkapiPack(newPack);
        pack.delete();
        pack.getPackFolder();
    }

    @Test(expected = RuntimeException.class)
    public void testGetDerivedAfterDelete() throws Exception {
        File newPack = new File(completeSamplePack.getPath() + "_NEW");
        FileUtils.copyDirectory(completeSamplePack, newPack);
        OkapiPack pack = new OkapiPack(newPack);
        pack.delete();
        pack.getDerivedFile();
    }


}