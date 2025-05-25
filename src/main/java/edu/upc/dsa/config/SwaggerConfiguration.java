package edu.upc.dsa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.log4j.Logger;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.util.Collections;

@Provider
public class SwaggerConfiguration implements ContextResolver<OpenAPI> {
    final static Logger logger = Logger.getLogger(SwaggerConfiguration.class);

    @Override
    public OpenAPI getContext(Class<?> type) {
        logger.info("Funcionas");
        return new OpenAPI()
                .info(new Info()
                        .title("Tu API")
                        .version("1.0.0")
                        .description("Documentación generada con Swagger"))
                .servers(Collections.singletonList(
                        new Server().url("http://localhost:8080/dsaApp")

                ));
    }
}
