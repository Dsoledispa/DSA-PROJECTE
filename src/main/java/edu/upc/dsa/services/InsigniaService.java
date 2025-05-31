package edu.upc.dsa.services;

import edu.upc.dsa.manager.InsigniaManager;
import edu.upc.dsa.manager.InsigniaManagerImpl;
import edu.upc.dsa.models.Insignia;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/insignia")
@Tag(name = "Insignias", description = "Operaciones relacionadas con insignias")
public class InsigniaService {

    final static Logger logger = Logger.getLogger(InsigniaService.class);
    private final InsigniaManager manager = new InsigniaManagerImpl();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener todas las insignias", description = "Devuelve todas las insignias registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de insignias devuelta",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Insignia.class)))
    })
    public Response getAllInsignias() {
        try {
            List<Insignia> insignias = manager.getAllInsignias();
            return Response.ok(insignias).build();
        } catch (Exception e) {
            logger.error("Error al obtener insignias", e);
            return Response.status(500).entity("{\"error\":\"Error interno al obtener insignias\"}").build();
        }
    }

    @GET
    @Path("/{id_insignia}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener insignia por ID", description = "Devuelve una insignia dado su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insignia encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Insignia.class))),
            @ApiResponse(responseCode = "404", description = "Insignia no encontrada")
    })
    public Response getInsigniaById(@PathParam("id_insignia") String id_insignia) {
        try {
            Insignia ins = manager.getInsignia(id_insignia);
            if (ins != null) return Response.ok(ins).build();
            return Response.status(404).entity("{\"error\":\"Insignia no encontrada\"}").build();
        } catch (Exception e) {
            logger.error("Error al obtener insignia por ID", e);
            return Response.status(500).entity("{\"error\":\"Error interno al obtener insignia\"}").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Añadir una insignia", description = "Crea una nueva insignia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Insignia añadida correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Insignia.class))),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    public Response addInsignia(Insignia insignia) {
        try {
            Insignia creada = manager.addInsignia(insignia);
            return Response.status(201).entity(creada).build();
        } catch (Exception e) {
            logger.error("Error al añadir insignia", e);
            return Response.status(500).entity("{\"error\":\"Error interno al añadir insignia\"}").build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Actualizar una insignia", description = "Actualiza los datos de una insignia existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insignia actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Insignia no encontrada")
    })
    public Response updateInsignia(Insignia insignia) {
        try {
            manager.updateInsignia(insignia);
            return Response.ok("{\"mensaje\":\"Insignia actualizada correctamente\"}").build();
        } catch (Exception e) {
            logger.error("Error al actualizar insignia", e);
            return Response.status(500).entity("{\"error\":\"Error interno al actualizar insignia\"}").build();
        }
    }

    @DELETE
    @Path("/{id_insignia}")
    @Operation(summary = "Eliminar insignia por ID", description = "Elimina una insignia según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insignia eliminada correctamente")
    })
    public Response deleteInsignia(@PathParam("id_insignia") String id_insignia) {
        try {
            manager.deleteInsignia(id_insignia);
            return Response.ok("{\"mensaje\":\"Insignia eliminada correctamente\"}").build();
        } catch (Exception e) {
            logger.error("Error al eliminar insignia", e);
            return Response.status(500).entity("{\"error\":\"Error interno al eliminar insignia\"}").build();
        }
    }

    @DELETE
    @Operation(summary = "Eliminar todas las insignias", description = "Elimina todas las insignias de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todas las insignias eliminadas correctamente")
    })
    public Response deleteAllInsignias() {
        try {
            manager.deleteAllInsignias();
            return Response.ok("{\"mensaje\":\"Todas las insignias han sido eliminadas\"}").build();
        } catch (Exception e) {
            logger.error("Error al eliminar todas las insignias", e);
            return Response.status(500).entity("{\"error\":\"Error interno al eliminar todas las insignias\"}").build();
        }
    }
}
