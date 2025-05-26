package edu.upc.dsa.services;

import edu.upc.dsa.exceptions.partida.PartidaNotFoundException;
import edu.upc.dsa.manager.PartidaManager;
import edu.upc.dsa.manager.PartidaManagerImpl;
import edu.upc.dsa.models.Partida;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/partidas")
@Tag(name = "Partida", description = "Operaciones relacionadas con Partidas")
public class PartidaService {

    final static Logger logger = Logger.getLogger(PartidaService.class);
    private final PartidaManager pm = new PartidaManagerImpl();

    @Context
    SecurityContext securityContext;

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Crear una nueva partida para el usuario autenticado",
            description = "Crea una nueva partida asociada al usuario identificado en el token JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Partida creada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Partida.class))),
            @ApiResponse(responseCode = "500", description = "Error interno al crear partida")
    })
    public Response addPartida(Partida partida) {
        try {
            String id_usuario = securityContext.getUserPrincipal().getName();
            partida.setId_usuario(id_usuario);
            Partida creada = pm.addPartida(partida);
            if (creada != null) return Response.status(201).entity(creada).build();
            return Response.status(400).entity("{\"error\":\"No se pudo crear la partida\"}").build();
        } catch (Exception e) {
            logger.error("Error al crear partida", e);
            return Response.status(500).entity("{\"error\":\"Error interno al crear partida\"}").build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Crear partida para el usuario autenticado (sin datos explícitos)",
            description = "Crea una partida vacía o con valores por defecto para el usuario del token JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Partida creada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Partida.class))),
            @ApiResponse(responseCode = "500", description = "Error interno al crear partida")
    })
    public Response addPartidaSinDatos() {
        try {
            String id_usuario = securityContext.getUserPrincipal().getName();
            logger.info("ID_usuario: " +id_usuario);
            Partida creada = pm.addPartida(id_usuario);
            if (creada != null) return Response.status(201).entity(creada).build();
            return Response.status(400).entity("{\"error\":\"No se pudo crear la partida\"}").build();
        } catch (Exception e) {
            logger.error("Error al crear partida sin datos", e);
            return Response.status(500).entity("{\"error\":\"Error interno al crear partida sin datos\"}").build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Obtener todas las partidas del usuario autenticado",
            description = "Devuelve todas las partidas asociadas al usuario del token JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de partidas devuelta",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Partida.class))),
            @ApiResponse(responseCode = "500", description = "Error interno al obtener partidas")
    })
    public Response getPartidas() {
        try {
            String id_usuario = securityContext.getUserPrincipal().getName();

            logger.info("Este id usuario es: " +id_usuario);
            List<Partida> partidas = pm.getPartidas(id_usuario);
            return Response.ok(partidas).build();
        } catch (Exception e) {
            logger.error("Error al obtener partidas", e);
            return Response.status(500).entity("{\"error\":\"Error interno al obtener partidas\"}").build();
        }
    }

    @GET
    @Path("/{id_partida}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Obtener una partida específica del usuario autenticado",
            description = "Devuelve una partida dada su ID asociada al usuario del token JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partida encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Partida.class))),
            @ApiResponse(responseCode = "404", description = "Partida no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno al obtener partida")
    })
    public Response getPartida(@Parameter(description = "ID de la partida") @PathParam("id_partida") String id_partida) {
        try {
            String id_usuario = securityContext.getUserPrincipal().getName();
            Partida partida = pm.getPartida(id_usuario, id_partida);
            if (partida != null) return Response.ok(partida).build();
            return Response.status(404).entity("{\"error\":\"Partida no encontrada\"}").build();
        } catch (Exception e) {
            logger.error("Error al obtener partida", e);
            return Response.status(500).entity("{\"error\":\"Error interno al obtener partida\"}").build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Actualizar una partida del usuario autenticado",
            description = "Actualiza una partida existente perteneciente al usuario identificado por el token JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partida actualizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Partida.class))),
            @ApiResponse(responseCode = "404", description = "Partida no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno al actualizar partida")
    })
    public Response updatePartida(Partida partida) {
        try {
            String id_usuario = securityContext.getUserPrincipal().getName();
            // Validamos que la partida pertenece al usuario autenticado
            Partida existente = pm.getPartida(id_usuario, partida.getId_partida());

            // Asignamos el usuario actual a la partida (por seguridad)
            partida.setId_usuario(id_usuario);

            pm.updatePartida(partida);
            return Response.ok(partida).build();
        } catch (PartidaNotFoundException e) {
            logger.warn("Partida no encontrada para actualización", e);
            return Response.status(404).entity("{\"error\":\"Partida no encontrada\"}").build();
        } catch (Exception e) {
            logger.error("Error al actualizar partida", e);
            return Response.status(500).entity("{\"error\":\"Error interno al actualizar partida\"}").build();
        }
    }


    @DELETE
    @Path("/{id_partida}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Eliminar una partida del usuario autenticado",
            description = "Elimina una partida dado su ID para el usuario del token JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partida eliminada correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno al eliminar partida")
    })
    public Response deletePartida(@Parameter(description = "ID de la partida") @PathParam("id_partida") String id_partida) {
        try {
            String id_usuario = securityContext.getUserPrincipal().getName();
            pm.deletePartida(id_usuario, id_partida);
            return Response.ok("{\"mensaje\":\"Partida eliminada correctamente\"}").build();
        } catch (Exception e) {
            logger.error("Error al eliminar partida", e);
            return Response.status(500).entity("{\"error\":\"Error interno al eliminar partida\"}").build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Eliminar todas las partidas del usuario autenticado",
            description = "Elimina todas las partidas asociadas al usuario autenticado mediante JWT.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partidas eliminadas correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Response deletePartidas() {
        try {
            String id_usuario = securityContext.getUserPrincipal().getName();
            pm.deletePartidas(id_usuario);
            return Response.ok("{\"mensaje\":\"Partidas eliminadas correctamente\"}").build();
        } catch (Exception e) {
            return Response.status(500).entity("{\"error\":\"Error interno del servidor\"}").build();
        }
    }

    @GET
    @Path("/{id_partida}/monedas")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Obtener monedas de una partida específica",
            description = "Devuelve la cantidad de monedas de la partida dada por id_partida para el usuario del token JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cantidad de monedas devuelta"),
            @ApiResponse(responseCode = "500", description = "Error interno al obtener monedas")
    })
    public Response getMonedasDePartida(@Parameter(description = "ID de la partida") @PathParam("id_partida") String id_partida) {
        try {
            String id_usuario = securityContext.getUserPrincipal().getName();
            int monedas = pm.getMonedasDePartida(id_usuario, id_partida);
            return Response.ok("{\"monedas\":" + monedas + "}").build();
        } catch (Exception e) {
            logger.error("Error al obtener monedas de partida", e);
            return Response.status(500).entity("{\"error\":\"Error interno al obtener monedas\"}").build();
        }
    }
}

