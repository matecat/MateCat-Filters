package com.matecat.converter.server;

import com.matecat.converter.server.rest.MatecatConverterServer;

public class Main {

	/**
	 * Entry point for the convert, which executes the server
	 *
	 * @param args Input port, or nothing to use the default
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// If we are supplying a port
		if(args.length >= 1) {
			try {
				int serverPort = Integer.parseInt(args[0]);
				new MatecatConverterServer(serverPort);
			} catch (NumberFormatException e) {
				throw new RuntimeException("The supplied port " + args[0] + " is not valid");
			}
		}
		else {
			new MatecatConverterServer();
		}

	}

}
