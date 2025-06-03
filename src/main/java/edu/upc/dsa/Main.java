package edu.upc.dsa;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.io.IOException;
import java.net.URI;

public class Main {

    public static final String BASE_URI = "http://192.168.10.92:8080/dsaApp/";

    public static HttpServer startServer() {
        // Configura Jersey con la clase AppConfig
        final AppConfig rc = new AppConfig();

        // Crea y arranca el servidor Grizzly en la URI BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();

        // Sirve archivos estáticos (Swagger UI, frontend, etc) en / (raíz)
        StaticHttpHandler staticHandler = new StaticHttpHandler("./public/");
        server.getServerConfiguration().addHttpHandler(staticHandler, "/");

        System.out.println(String.format(
                "Servidor arrancado en %s\nDocumentación OpenAPI en %sopenapi.json\nPulsa Enter para parar...",
                BASE_URI, BASE_URI));

        System.in.read();
        server.shutdownNow();
    }
}

