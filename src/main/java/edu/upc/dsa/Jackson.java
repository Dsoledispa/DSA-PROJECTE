package edu.upc.dsa;

import edu.upc.dsa.config.SwaggerConfiguration;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class Jackson extends ResourceConfig {
    public Jackson() {
        // Paquete donde están tus servicios REST
        packages("edu.upc.dsa.services");

        // Registro de Jackson para JSON
        register(JacksonFeature.class);

        // Registro del filtro JWT
        register(edu.upc.dsa.config.JWTAuthFilter.class);

        // Registro de la configuración personalizada de Swagger
        register(SwaggerConfiguration.class);

        // Registro del endpoint OpenAPI /openapi.json
        register(OpenApiResource.class);


    }
}

