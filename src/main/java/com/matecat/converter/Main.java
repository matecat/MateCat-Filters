package com.matecat.converter;

import com.matecat.converter.server.MatecatConverterServer;

import java.util.Scanner;

public class Main {

	/**
	 * Entry point for the convert, which executes the server
	 */
	public static void main(String[] args) throws Exception {

		// Init the server
		MatecatConverterServer server = new MatecatConverterServer();

		// Wait until the user finishes the execution
		Scanner sc = new Scanner(System.in);
		String command;
		do {
			command = sc.nextLine();
		}
		while (!command.toLowerCase().equals("quit")  &&  !command.toLowerCase().equals("exit"));

		// Stop the server
		server.stop();
		System.out.println("\nGood bye!");

	}

}
