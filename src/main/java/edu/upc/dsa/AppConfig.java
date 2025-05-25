package edu.upc.dsa;

import edu.upc.dsa.config.JWTAuthFilter;
import edu.upc.dsa.config.SwaggerConfiguration;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class AppConfig extends ResourceConfig {
    public AppConfig() {
        // Paquete con tus servicios REST
        packages("edu.upc.dsa.services");

        // Soporte JSON con Jackson
        register(JacksonFeature.class);

        // Filtro JWT para autenticación
        register(JWTAuthFilter.class);

        // Configuración personalizada de Swagger (OpenAPI)
        register(SwaggerConfiguration.class);

        // Endpoint para openapi.json
        register(OpenApiResource.class);
    }
}


