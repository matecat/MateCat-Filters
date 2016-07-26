package com.matecat.converter.server.exceptions;

import java.io.IOException;

/**
 * Internal server exception
 */
public class ServerException extends IOException {

    /**
     * Call the super constructor, passing a message
     * @param msg Message
     */
    public ServerException(String msg) {
        super(msg);
    }
}
