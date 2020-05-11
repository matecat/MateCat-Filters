package com.matecat.converter.server;

import org.json.JSONStringer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

/**
 * Factory which creates JSON messages to use as http responses.
 * <p>
 * The use of {@link JSONStringer} allows to decide the order the JSON object fields.
 * <p>
 * TODO: Replace the JSON response by the contents of the file. This class will be deleted.
 */
public class JSONResponseFactory {

    public static final String IS_SUCCESS = "isSuccess";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String XLIFF_CONTENT = "xliffContent";
    public static final String DOCUMENT_CONTENT = "documentContent";
    public static final String FILENAME = "filename";
    public static final String DEBUG_LOG = "log";

    public static String getError(String errorMessage, List<String> logLines) {
        JSONStringer stringer = new JSONStringer();
        stringer.object()
                .key(IS_SUCCESS).value(false)
                .key(ERROR_MESSAGE).value(errorMessage);
        insertDebugLog(stringer, logLines);
        stringer.endObject();
        return stringer.toString();
    }

    public static String getConvertSuccess(File file, List<String> logLines) {
        try {
            String xliffContent = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            JSONStringer stringer = new JSONStringer();
            stringer.object()
                    .key(IS_SUCCESS).value(true)
                    .key(FILENAME).value(file.getName());
            insertDebugLog(stringer, logLines);
            stringer.key(XLIFF_CONTENT).value(xliffContent);
            stringer.endObject();
            return stringer.toString();
        } catch (IOException e) {
            // wrap and rethrow
            throw new RuntimeException(e);
        }
    }

    public static String getDerivedSuccess(File file, List<String> logLines) {
        try {
            String encodedDocument = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
            JSONStringer stringer = new JSONStringer();
            stringer.object()
                    .key(IS_SUCCESS).value(true)
                    .key(FILENAME).value(file.getName());
            insertDebugLog(stringer, logLines);
            stringer.key(DOCUMENT_CONTENT).value(encodedDocument);
            stringer.endObject();
            return stringer.toString();
        } catch (IOException e) {
            // wrap and rethrow
            throw new RuntimeException(e);
        }
    }

    private static void insertDebugLog(JSONStringer stringer, List<String> logLines) {
        if (!logLines.isEmpty()) {
            stringer.key(DEBUG_LOG).array();
            logLines.forEach(stringer::value);
            stringer.endArray();
        }
    }
}
