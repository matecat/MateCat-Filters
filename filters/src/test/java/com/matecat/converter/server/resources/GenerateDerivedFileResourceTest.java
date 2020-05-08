package com.matecat.converter.server.resources;

import com.matecat.converter.server.JSONResponseFactory;
import com.matecat.converter.server.MatecatConverterServer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.Path;
import java.io.File;
import java.io.InputStreamReader;

import static org.junit.Assert.*;


public class GenerateDerivedFileResourceTest {

    private static MatecatConverterServer server;
    private static final int PORT = 8090;
    private static final String url = "http://localhost:" + PORT + GenerateDerivedFileResource.class.getAnnotation(Path.class).value();
    private static final File fileToUpload = new File(GenerateDerivedFileResourceTest.class.getResource("/server/test.docx.xlf").getPath());

    @Before
    public void setUp() throws Exception {
        server = new MatecatConverterServer(PORT);
        while (!server.isStarted())
            Thread.sleep(100);
    }

    private MultipartEntityBuilder prepareMultipartBuilder() {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        FileBody uploadFilePart = new FileBody(fileToUpload);
        builder.addPart("xliffContent", uploadFilePart);
        return builder;
    }

    @Test
    public void testDeriveSuccess() throws Exception {
        // Send request
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(prepareMultipartBuilder().build());
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpResponse response = httpclient.execute(httpPost);
        // Check OK status code
        assertEquals(200, response.getStatusLine().getStatusCode());
        // Parse body as JSON
        final InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
        JSONObject json = (JSONObject) new JSONParser().parse(reader);
        // Is success
        boolean isSuccess = (boolean) json.get(JSONResponseFactory.IS_SUCCESS);
        assertTrue(isSuccess);
        // No error message
        String error = (String) json.getOrDefault(JSONResponseFactory.ERROR_MESSAGE, "");
        assertEquals("", error);
        // filename
        final String name = (String) json.get(JSONResponseFactory.FILENAME);
        assertNotNull(name);
        assertEquals(fileToUpload.getName().replaceAll(".docx.xlf", ".out.docx"), name);
        // Encoded document
        String encodedDoc = (String) json.get(JSONResponseFactory.DOCUMENT_CONTENT);
        assertNotSame("", encodedDoc);
    }

    @Test
    public void testDeriveDebug() throws Exception {
        // Send request
        HttpPost httpPost = new HttpPost(url);
        final MultipartEntityBuilder builder = prepareMultipartBuilder();
        builder.addPart("debugMode", new StringBody("true", ContentType.TEXT_PLAIN));
        httpPost.setEntity(builder.build());
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpResponse response = httpclient.execute(httpPost);
        // Check OK status code
        assertEquals(200, response.getStatusLine().getStatusCode());
        // Parse body as JSON
        final InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
        JSONObject json = (JSONObject) new JSONParser().parse(reader);
        // Is success
        boolean isSuccess = (boolean) json.get(JSONResponseFactory.IS_SUCCESS);
        assertTrue(isSuccess);
        // No error message
        String error = (String) json.getOrDefault(JSONResponseFactory.ERROR_MESSAGE, "");
        assertEquals("", error);
        // log
        final JSONArray log = (JSONArray) json.get(JSONResponseFactory.DEBUG_LOG);
        assertNotNull(log);
        assertFalse(log.isEmpty());
        // filename
        final String name = (String) json.get(JSONResponseFactory.FILENAME);
        assertNotNull(name);
        assertNotEquals("", name);
        // Encoded document
        String doc = (String) json.get(JSONResponseFactory.DOCUMENT_CONTENT);
        assertNotSame("", doc);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
        while (!server.isStopped())
            Thread.sleep(100);
    }
}