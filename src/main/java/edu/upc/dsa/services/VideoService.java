package edu.upc.dsa.services;

import edu.upc.dsa.models.Video;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/Video")
@Tag(name = "video", description = "Servicio de videos de soporte")
public class VideoService {

    final static Logger logger = Logger.getLogger(VideoService.class);

    public VideoService() {
        // Constructor vacío
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Obtener lista de videos",
            description = "Devuelve todos los videos de soporte disponibles"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de videos obtenida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Video.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Response getVideos() {
        try {
            List<Video> videos = new ArrayList<>();

            videos.add(new Video("https://www.youtube.com/watch?v=M6pQ63ssBKQ", "FAQs"));
            videos.add(new Video("https://www.youtube.com/watch?v=ox7WSYmAm5A", "No recuerdo mi contraseña"));
            videos.add(new Video("https://www.youtube.com/watch?v=dcOMFYfVqqc", "¿Fecha para la versión en iOS?"));
            videos.add(new Video("https://www.youtube.com/watch?v=SRfU9meXlC8", "¿ Cómo jugar?"));

            logger.info("Devolviendo " + videos.size() + " videos");
            return Response.status(200).entity(videos).build();

        } catch (Exception e) {
            logger.error("Error al obtener videos", e);
            return Response.status(500).entity("{\"error\":\"Error al obtener videos\"}").build();
        }
    }
}