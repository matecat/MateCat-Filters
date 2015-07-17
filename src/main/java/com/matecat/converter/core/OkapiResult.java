package com.matecat.converter.core;

import java.io.File;

/**
 * Created by Alvaro on 16/07/2015.
 */
public class OkapiResult {

    private File xlf, manifest;

    public OkapiResult(File xlf, File manifest) {
        this.xlf = xlf;
        this.manifest = manifest;
    }

    public File getXlf() {
        return xlf;
    }

    public File getManifest() {
        return manifest;
    }
}
