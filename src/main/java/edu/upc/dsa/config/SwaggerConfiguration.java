package edu.upc.dsa.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Tu API",
                version = "1.0.0",
                description = "Documentación generada con Swagger"
        ),
        servers = {
                @Server(url = "http://localhost:8080/dsaApp"),
                @Server(url = "http://dsa1.upc.edu/dsaApp")
        }
)
public class SwaggerConfiguration {
    // No hace falta nada más aquí
}
