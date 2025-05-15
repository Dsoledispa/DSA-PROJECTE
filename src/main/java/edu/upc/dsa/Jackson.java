package edu.upc.dsa;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class Jackson extends ResourceConfig {
    public Jackson() {
        // Aquí registramos los recursos y proveedores
        packages("edu.upc.dsa.services"); // Paquete donde están tus recursos REST
        register(JacksonFeature.class);  // Registra Jackson para JSON
        register(edu.upc.dsa.config.JWTAuthFilter.class); //Registro directo para JWT
        register(edu.upc.dsa.config.CORSFilter.class); // REGISTRO DEL FILTRO CORS
    }
}
