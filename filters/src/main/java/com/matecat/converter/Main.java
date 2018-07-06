package com.matecat.converter;

import com.matecat.converter.server.MatecatConverterServer;
import com.matecat.filters.basefilters.FiltersRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

	private static Logger LOGGER = LoggerFactory.getLogger(Main.class);


	public static void main(String[] args) throws Exception {
		if (Charset.defaultCharset() != StandardCharsets.UTF_8) {
			throw new Exception("Java default charset is " + Charset.defaultCharset() + ", must be UTF-8. Fix your configuration.");
		}

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
