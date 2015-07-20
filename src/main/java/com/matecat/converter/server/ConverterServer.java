package com.matecat.converter.server;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ConverterServer {

    private static final int PORT = 7070;

    public static void main(String args[]) throws Exception {
        Server server = new Server(PORT);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/hello");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new HelloServlet()), "/*");
        server.start();
    }


}

