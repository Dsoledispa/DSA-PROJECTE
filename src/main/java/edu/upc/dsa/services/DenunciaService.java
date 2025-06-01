package edu.upc.dsa.services;

import edu.upc.dsa.exceptions.denuncia.DenunciaNotFoundException;
import edu.upc.dsa.exceptions.denuncia.DenunciaYaExisteException;
import edu.upc.dsa.manager.DenunciaManager;
import edu.upc.dsa.manager.DenunciaManagerImpl;
import edu.upc.dsa.models.Denuncia;
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

@Path("/denuncias")
@Tag(name = "Denuncia", description = "Operaciones relacionadas con denuncias de los usuarios")
public class DenunciaService {

    final static Logger logger = Logger.getLogger(DenunciaService.class);
    private final DenunciaManager dm = new DenunciaManagerImpl();

    @Context
    SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Crear una nueva denuncia",
            description = "Permite al usuario autenticado crear una nueva denuncia",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Denuncia creada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Denuncia.class))),
            @ApiResponse(responseCode = "409", description = "Ya existe una denuncia con ese ID"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response addDenuncia(Denuncia denuncia) {
        try {
            denuncia.setId_usuario(securityContext.getUserPrincipal().getName());
            denuncia.setFecha(LocalDateTime.now());
            Denuncia nueva = dm.addDenuncia(denuncia);
            return Response.status(201).entity(nueva).build();
        } catch (DenunciaYaExisteException e) {
            logger.warn("Denuncia duplicada", e);
            return Response.status(409).entity("{\"error\":\"Denuncia ya existe\"}").build();
        } catch (Exception e) {
            logger.error("Error al crear denuncia", e);
            return Response.status(500).entity("{\"error\":\"Error interno al crear denuncia\"}").build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Obtener todas las denuncias",
            description = "Devuelve todas las denuncias registradas",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Denuncias obtenidas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Denuncia.class))),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response getDenuncias() {
        try {
            List<Denuncia> denuncias = dm.getDenuncias();
            return Response.ok(denuncias).build();
        } catch (Exception e) {
            logger.error("Error al obtener denuncias", e);
            return Response.status(500).entity("{\"error\":\"Error interno al obtener denuncias\"}").build();
        }
    }

    @GET
    @Path("/usuario")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Obtener las denuncias del usuario autenticado",
            description = "Devuelve todas las denuncias realizadas por el usuario autenticado",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Denuncias del usuario obtenidas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Denuncia.class))),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response getDenunciasByUsuario() {
        try {
            String id_usuario = securityContext.getUserPrincipal().getName();
            List<Denuncia> denuncias = dm.getDenunciasByUsuario(id_usuario);
            return Response.ok(denuncias).build();
        } catch (Exception e) {
            logger.error("Error al obtener denuncias por usuario", e);
            return Response.status(500).entity("{\"error\":\"Error interno al obtener denuncias\"}").build();
        }
    }

    @GET
    @Path("/{id_denuncia}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Obtener una denuncia por ID",
            description = "Devuelve una denuncia específica dada su ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Denuncia encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Denuncia.class))),
            @ApiResponse(responseCode = "404", description = "Denuncia no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response getDenuncia(@Parameter(description = "ID de la denuncia") @PathParam("id_denuncia") String id_denuncia) {
        try {
            Denuncia denuncia = dm.getDenuncia(id_denuncia);
            return Response.ok(denuncia).build();
        } catch (DenunciaNotFoundException e) {
            return Response.status(404).entity("{\"error\":\"Denuncia no encontrada\"}").build();
        } catch (Exception e) {
            logger.error("Error al obtener denuncia", e);
            return Response.status(500).entity("{\"error\":\"Error interno al obtener denuncia\"}").build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Actualizar una denuncia existente",
            description = "Actualiza los datos de una denuncia existente",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Denuncia actualizada"),
            @ApiResponse(responseCode = "404", description = "Denuncia no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response updateDenuncia(Denuncia denuncia) {
        try {
            dm.updateDenuncia(denuncia);
            return Response.ok(denuncia).build();
        } catch (DenunciaNotFoundException e) {
            return Response.status(404).entity("{\"error\":\"Denuncia no encontrada\"}").build();
        } catch (Exception e) {
            logger.error("Error al actualizar denuncia", e);
            return Response.status(500).entity("{\"error\":\"Error interno al actualizar denuncia\"}").build();
        }
    }

    @DELETE
    @Path("/{id_denuncia}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Eliminar una denuncia por ID",
            description = "Elimina una denuncia específica dado su ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Denuncia eliminada"),
            @ApiResponse(responseCode = "404", description = "Denuncia no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response deleteDenuncia(@Parameter(description = "ID de la denuncia") @PathParam("id_denuncia") String id_denuncia) {
        try {
            dm.deleteDenuncia(id_denuncia);
            return Response.ok("{\"message\":\"Denuncia eliminada correctamente\"}").build();
        } catch (DenunciaNotFoundException e) {
            return Response.status(404).entity("{\"error\":\"Denuncia no encontrada\"}").build();
        } catch (Exception e) {
            logger.error("Error al eliminar denuncia", e);
            return Response.status(500).entity("{\"error\":\"Error interno al eliminar denuncia\"}").build();
        }
    }
}
