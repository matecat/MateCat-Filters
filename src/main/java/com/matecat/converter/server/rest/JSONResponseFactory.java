package com.matecat.converter.server.rest;

import net.sf.okapi.filters.json.JSONFilter;
import org.json.simple.JSONObject;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

/**
 * TODO: Replace the JSON response by the contents of the file. This class will be deleted
 */
public class JSONResponseFactory {

    public static String getError(String errorMessage) {
        JSONObject output = new JSONObject();
        output.put("isSuccess", false);
        output.put("errorMessage", errorMessage);
        return output.toJSONString();
    }

    public static String getSuccess(File file) {
        try {
            String encodedDocument = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
            JSONObject output = new JSONObject();
            output.put("isSuccess", true);
            output.put("documentContent", encodedDocument);
            return output.toJSONString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }




}
