package com.matecat.converter.server;

import com.matecat.converter.server.rest.MatecatConverterServer;

public class Main {

	public static void main(String[] args) throws Exception {
		
		if(args.length >= 1) {
			try {
				int serverPort = Integer.parseInt(args[0]);
				new MatecatConverterServer(serverPort);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		else {
			new MatecatConverterServer();
		}

	}

}
