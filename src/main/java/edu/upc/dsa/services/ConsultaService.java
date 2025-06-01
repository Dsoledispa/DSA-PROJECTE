package edu.upc.dsa.services;

import edu.upc.dsa.exceptions.consulta.ConsultaNotFoundException;
import edu.upc.dsa.exceptions.consulta.ConsultaYaExisteException;
import edu.upc.dsa.manager.ConsultaManager;
import edu.upc.dsa.manager.ConsultaManagerImpl;
import edu.upc.dsa.models.Consulta;
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
import java.time.LocalDateTime;
import java.util.List;

@Path("/consultas")
@Tag(name = "Consulta", description = "Operaciones relacionadas con consultas de los usuarios")
public class ConsultaService {

    final static Logger logger = Logger.getLogger(ConsultaService.class);
    private final ConsultaManager cm = new ConsultaManagerImpl();

    @Context
    SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Crear una nueva consulta",
            description = "Permite al usuario autenticado crear una nueva consulta",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consulta creada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Consulta.class))),
            @ApiResponse(responseCode = "409", description = "Ya existe una consulta con ese ID"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response addConsulta(Consulta consulta) {
        try {
            consulta.setId_usuario(securityContext.getUserPrincipal().getName());
            consulta.setFecha(LocalDateTime.now());
            Consulta nueva = cm.addConsulta(consulta);
            return Response.status(201).entity(nueva).build();
        } catch (ConsultaYaExisteException e) {
            logger.warn("Consulta duplicada", e);
            return Response.status(409).entity("{\"error\":\"Consulta ya existe\"}").build();
        } catch (Exception e) {
            logger.error("Error al crear consulta", e);
            return Response.status(500).entity("{\"error\":\"Error interno al crear consulta\"}").build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Obtener todas las consultas",
            description = "Devuelve todas las consultas disponibles",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consultas obtenidas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Consulta.class))),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response getConsultas() {
        try {
            List<Consulta> consultas = cm.getConsultas();
            return Response.ok(consultas).build();
        } catch (Exception e) {
            logger.error("Error al obtener consultas", e);
            return Response.status(500).entity("{\"error\":\"Error interno al obtener consultas\"}").build();
        }
    }

    @GET
    @Path("/usuario")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Obtener las consultas del usuario autenticado",
            description = "Devuelve todas las consultas asociadas al usuario autenticado",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consultas obtenidas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Consulta.class))),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response getConsultasByUsuario() {
        try {
            String id_usuario = securityContext.getUserPrincipal().getName();
            List<Consulta> consultas = cm.getConsultasByUsuario(id_usuario);
            return Response.ok(consultas).build();
        } catch (Exception e) {
            logger.error("Error al obtener consultas por usuario", e);
            return Response.status(500).entity("{\"error\":\"Error interno al obtener consultas\"}").build();
        }
    }

    @GET
    @Path("/{id_consulta}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Obtener una consulta por ID",
            description = "Devuelve una consulta específica dada su ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Consulta.class))),
            @ApiResponse(responseCode = "404", description = "Consulta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response getConsulta(@Parameter(description = "ID de la consulta") @PathParam("id_consulta") String id_consulta) {
        try {
            Consulta consulta = cm.getConsulta(id_consulta);
            return Response.ok(consulta).build();
        } catch (ConsultaNotFoundException e) {
            return Response.status(404).entity("{\"error\":\"Consulta no encontrada\"}").build();
        } catch (Exception e) {
            logger.error("Error al obtener consulta", e);
            return Response.status(500).entity("{\"error\":\"Error interno al obtener consulta\"}").build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Actualizar una consulta existente",
            description = "Actualiza los datos de una consulta existente",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta actualizada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Consulta.class))),
            @ApiResponse(responseCode = "404", description = "Consulta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response updateConsulta(Consulta consulta) {
        try {
            cm.updateConsulta(consulta);
            return Response.ok(consulta).build();
        } catch (ConsultaNotFoundException e) {
            return Response.status(404).entity("{\"error\":\"Consulta no encontrada\"}").build();
        } catch (Exception e) {
            logger.error("Error al actualizar consulta", e);
            return Response.status(500).entity("{\"error\":\"Error interno al actualizar consulta\"}").build();
        }
    }

    @DELETE
    @Path("/{id_consulta}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Eliminar una consulta por ID",
            description = "Elimina una consulta dada su ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta eliminada"),
            @ApiResponse(responseCode = "404", description = "Consulta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response deleteConsulta(@Parameter(description = "ID de la consulta") @PathParam("id_consulta") String id_consulta) {
        try {
            cm.deleteConsulta(id_consulta);
            return Response.ok().entity("{\"message\":\"Consulta eliminada\"}").build();
        } catch (ConsultaNotFoundException e) {
            return Response.status(404).entity("{\"error\":\"Consulta no encontrada\"}").build();
        } catch (Exception e) {
            logger.error("Error al eliminar consulta", e);
            return Response.status(500).entity("{\"error\":\"Error interno al eliminar consulta\"}").build();
        }
    }
}
