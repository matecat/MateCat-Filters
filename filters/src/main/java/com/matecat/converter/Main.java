package com.matecat.converter;

import com.matecat.converter.server.MatecatConverterServer;
import com.matecat.filters.basefilters.FiltersRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Main {

	private static Logger LOGGER = LoggerFactory.getLogger(Main.class);


	public static void main(String[] args) throws Exception {
		// Init the server
		MatecatConverterServer server = new MatecatConverterServer();

		// Shutdown gracefully when receiving SIGTERM or similar
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			LOGGER.info("Shutdown signal received, stopping the server...");
            server.stop();
            LOGGER.info("Server stopped successfully. Good bye!");
        }));
	}

}
