package com.matecat.converter.server.resources;

import com.matecat.converter.core.project.Project;
import com.matecat.converter.server.MatecatConverterServer;
import com.matecat.logging.StoringAppender;
import net.sf.okapi.common.exceptions.OkapiEncryptedDataException;
import net.sf.okapi.common.exceptions.OkapiUnexpectedRevisionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Base class for REST Resources.
 * <p>
 * Deals mainly with request tracing and common logic.
 */
public class BaseResource {
    protected static final StoringAppender LOG_CAPTURER = new StoringAppender();
    protected static final Logger TRACER = LoggerFactory.getLogger("req-tracing");
    protected static final String CACHE_ID = "cacheId";
    protected static final String ELAPSED = "elapsed";
    protected static final String FILENAME = "fileName";
    protected static final String FILESIZE = "fileSize";
    protected static final String OPERATION = "operation";
    protected static final String REQ_ID = "reqId";
    protected static final String SOURCE_FILENAME = "sourceFileName";
    protected static final String SOURCE_FILESIZE = "sourceFileSize";
    protected static final String SRC_LANG = "sourceLanguage";
    protected static final String SUCCESSFUL = "successful";
    protected static final String TARGET_FILENAME = "targetFileName";
    protected static final String TARGET_FILESIZE = "targetFileSize";
    protected static final String TGT_LANG = "targetLanguage";
    protected static final String TIMESTAMP = "timestamp";
    protected static final String CALLER_IP = "callerIp";
    protected static final String HEADER_REQ_ID = "x-matecat-filter-proxy-reqid";
    @Context
    protected HttpServletRequest httpRequest;

    /**
     * Enumeration for filters operation types.
     */
    protected enum Operations {
        SOURCE2XLIFF, XLIFF2SOURCE, XLIFF2TARGET;

    }
    /**
     * Builds a random request id with the pattern HHMM-################# where
     * HH = hour, MM = minutes and # = a random alphanumeric character.
     *
     * @return The generated id
     */
    protected String generateRequestId() {
        final ThreadLocalRandom rand = ThreadLocalRandom.current();
        return String.format("%s-%xd", MatecatConverterServer.getLocalIP(), rand.nextLong());
    }

    /**
     * Trace start of a request.
     *
     * @param operation the operation type of the request
     */
    protected void traceStart(Operations operation) {
        MDC.clear();
        // take the request id from an HTTP header (by the Matecat Filter Proxy)
        // or generate a random one
        final String headerReqId = httpRequest.getHeader(HEADER_REQ_ID);
        if (headerReqId != null) {
            MDC.put(REQ_ID, headerReqId);
        } else {
            MDC.put(REQ_ID, generateRequestId());
        }
        MDC.put(OPERATION, operation.toString());
        MDC.put(TIMESTAMP, Instant.now().toString());
        MDC.put(CALLER_IP, httpRequest.getRemoteAddr());
        TRACER.debug("Request start");
    }

    /**
     * Trace end of a request.
     */
    protected void traceEnd() {
        final Duration elapsed = Duration.between(Instant.parse(MDC.get(TIMESTAMP)), Instant.now());
        MDC.put(ELAPSED, elapsed.toString());
        if (Boolean.parseBoolean(MDC.get(SUCCESSFUL))) {
            TRACER.info("Request served");
        } else {
            TRACER.error("Request failed");
        }
    }

    /**
     * If debug mode requested install the log capturer.
     *
     * @param debugEnabled true to enable debug mode
     */
    protected void setupDebugMode(boolean debugEnabled) {
        if (debugEnabled) {
            LOG_CAPTURER.install();
        }
    }

    /**
     * Perform cleanup actions at the end of a request.
     *
     * @param fileInputStream file input stream to close
     * @param debugMode       true if debug mode cleanup requested
     * @param project         project to delete (depending on configuration and outcome)
     * @param reqOutcome      true if request successful
     */
    protected void requestCleanup(InputStream fileInputStream, boolean debugMode, Project project, boolean reqOutcome) {
        // Close the file stream
        if (fileInputStream != null)
            try {
                fileInputStream.close();
            } catch (IOException ignored) {
            }
        // Delete folder only if everything went well
        if (project != null)
            project.close(reqOutcome);
        // If debug mode requested de-install the log capturer
        if (debugMode) {
            LOG_CAPTURER.clear();
            LOG_CAPTURER.deinstall();
        }
    }

    /**
     * Build an error message chaining the throwable in the exception or using canned strings.
     * <p>
     * Also store the message in the MDC.
     *
     * @param ex the exception
     * @return the build error message
     */
    protected String prepareErrorMessage(Exception ex) {
        StringBuilder sb = new StringBuilder();
        if (ex instanceof OkapiUnexpectedRevisionException) {
            sb.append("Document contains revisions or comments, please review and remove them.");
        } else if (ex instanceof OkapiEncryptedDataException) {
            sb.append("Document is password protected: can't access contents.");
        } else {
            sb.append(ex.toString());
            Throwable cause = ex.getCause();
            while (cause != null) {
                sb.append(" caused by ").append(cause.toString());
                cause = cause.getCause();
            }
        }
        final String errorMessage = sb.toString();
        MDC.put("exception", errorMessage);
        return errorMessage;
    }
}
