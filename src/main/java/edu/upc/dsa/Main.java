package edu.upc.dsa;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.io.IOException;
import java.net.URI;

public class Main {
    public static final String BASE_URI = "http://0.0.0.0:8080/dsaApp/";

    public static HttpServer startServer() {
        // Configura Jersey con la clase Jackson
        final Jackson rc = new Jackson();

        // Crea y arranca el servidor Grizzly
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();

        // Sirve los archivos estáticos en /public (para Swagger UI o frontend)
        StaticHttpHandler staticHandler = new StaticHttpHandler("./public/");
        server.getServerConfiguration().addHttpHandler(staticHandler, "/");

        System.out.println(String.format("Servidor arrancado en %s\n" +
                "Documentación OpenAPI disponible en %sopenapi.json\n" +
                "Pulsa Enter para parar...", BASE_URI, BASE_URI));

        System.in.read();
        server.shutdownNow();
    }
}
