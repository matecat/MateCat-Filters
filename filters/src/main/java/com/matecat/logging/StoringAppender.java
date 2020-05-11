package com.matecat.logging;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.Encoder;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * A Logback appender that intercepts and stores all the log events.
 * <p/>
 * The appender is meant to be temporarily installed to the root logger and reuses
 * the encoder of the first {@link ConsoleAppender} it finds or install its own.
 * The stored events can afterwards be retrieved as a String.
 * <p/>
 * <b>NOTE:</b> if not deinstalled it may exhaust all the memory by storing all
 * the log events indefinitely.
 */
public class StoringAppender extends AppenderBase<ILoggingEvent> {
    public static final String APPENDER_NAME = "interceptor";
    public static final String DEFAULT_PATTERN = "%d{HH:mm:ss.SSS} %-5level %logger{30} - %msg%n";
    private final Queue<ILoggingEvent> loggingEvents = new ConcurrentLinkedQueue<>();
    private Encoder<ILoggingEvent> encoder;

    public StoringAppender() {
        setName(APPENDER_NAME);
    }

    // this method is not called if the appender is not started
    // start happens in the install method
    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        loggingEvents.add(iLoggingEvent);
    }

    /**
     * Clears the stored log events.
     */
    public void clear() {
        loggingEvents.clear();
    }

    /**
     * Retrieves the stored log events.
     *
     * @return an array of {@code String}s with all the encoded log events
     */
    public synchronized List<String> getStoredLog() {
        return loggingEvents.stream()
                .map(iLoggingEvent -> new String(encoder.encode(iLoggingEvent), StandardCharsets.UTF_8))
                .collect(Collectors.toList());
    }

    /**
     * Installs this appender into the Logback root logger.
     */
    public synchronized void install() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        this.setContext(lc);
        final Logger rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        // look for a ConsoleAppender and "steal" its encoder
        boolean consoleFound = false;
        final Iterator<Appender<ILoggingEvent>> it = rootLogger.iteratorForAppenders();
        while (it.hasNext()) {
            Appender<ILoggingEvent> appender = it.next();
            if (appender instanceof ConsoleAppender) {
                final ConsoleAppender<ILoggingEvent> ca = (ConsoleAppender<ILoggingEvent>) appender;
                this.encoder = ca.getEncoder();
                consoleFound = true;
                break;
            }
        }
        // if no ConsoleAppender found define our own encoder
        if (!consoleFound) {
            final PatternLayoutEncoder ple = new PatternLayoutEncoder();
            ple.setPattern(DEFAULT_PATTERN);
            this.encoder = ple;
        }
        // start our self and add to root logger
        this.start();
        rootLogger.addAppender(this);
    }

    /**
     * De-installs the appender from the Logback root logger.
     */
    public synchronized void deinstall() {
        this.stop();
        LoggerContext lc = (LoggerContext) getContext();
        final Logger rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.detachAppender(APPENDER_NAME);
    }
}
