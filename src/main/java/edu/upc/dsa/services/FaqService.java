package edu.upc.dsa.services;

import edu.upc.dsa.models.Faq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/faqs")
@Tag(name = "faqs", description = "Servicio de FAQs")
public class FaqService {

    final static Logger logger = Logger.getLogger(FaqService.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Obtener lista de FAQs",
            description = "Devuelve una lista de FAQs predefinida (estática)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de FAQs",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Faq.class))),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response getFaqs() {
        try {
            List<Faq> faqs = new ArrayList<>();

            faqs.add(new Faq("2024-05-29", "¿Cómo puedo guardar mi partida?", "Pulsa en 'Guardar' en el menú principal.", "Admin"));
            faqs.add(new Faq("2024-05-28", "¿Cuantas vidas tengo?", "En la sección principal de tu perfil salen la vidas en forma de corazón.", "Admin"));
            faqs.add(new Faq("2024-05-27", "¿Cómo conseguir más monedas?", "Juega y intenta recolectar todas las monedas posibles.", "Admin"));

            return Response.status(200).entity(faqs).build();
        } catch (Exception e) {
            logger.error("Error al obtener FAQs", e);
            return Response.status(500).entity("{\"error\":\"Error interno\"}").build();
        }
    }

}