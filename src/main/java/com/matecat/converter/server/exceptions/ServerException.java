package com.matecat.converter.server.exceptions;

import java.io.IOException;

/**
 * Created by Reneses on 7/24/15.
 */
public class ServerException extends IOException {

    public ServerException(String msg) {
        super(msg);
    }
}
