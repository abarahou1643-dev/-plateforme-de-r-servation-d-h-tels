package com.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class AppConfig {
    public static void main(String[] args) throws Exception {
        ResourceConfig config = new ResourceConfig();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(mapper);
        config.register(provider);

        config.packages("com.example.rest");

        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(server, "/api");
        context.addServlet(servlet, "/*");

        server.start();
        System.out.println("🚀 Server started at http://localhost:8080/api/");
        server.join();
    }
}
