package com.matecat.converter;

import com.matecat.converter.server.rest.MatecatConverterServer;

public class Main {

	/**
	 * Entry point for the convert, which executes the server
	 */
	public static void main(String[] args) throws Exception {
		new MatecatConverterServer();
	}

}
