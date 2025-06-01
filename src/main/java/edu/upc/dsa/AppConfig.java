package edu.upc.dsa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import edu.upc.dsa.config.JWTAuthFilter;
import edu.upc.dsa.config.SwaggerConfiguration;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class AppConfig extends ResourceConfig {
    public AppConfig() {
        // Paquete con tus servicios REST
        packages("edu.upc.dsa.services");

        // Configuración personalizada de ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(mapper);

        register(provider);

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


