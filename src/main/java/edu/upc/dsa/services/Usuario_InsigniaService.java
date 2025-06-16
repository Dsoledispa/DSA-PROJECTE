package edu.upc.dsa.services;

import edu.upc.dsa.manager.Usuario_InsigniaManager;
import edu.upc.dsa.manager.Usuario_InsigniaManagerImpl;
import edu.upc.dsa.models.Insignia;
import edu.upc.dsa.models.Usuario_Insignia;
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

@Path("/Usuario_Insignia")
@Tag(name = "Usuario_Insignia", description = "Operaciones sobre insignias asignadas a usuarios")
public class Usuario_InsigniaService {

    final static Logger logger = Logger.getLogger(Usuario_InsigniaService.class);
    private final Usuario_InsigniaManager uim = new Usuario_InsigniaManagerImpl();

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Obtener insignias de un usuario",
            description = "Devuelve la lista de insignias asignadas a un usuario",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Insignias obtenidas correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Insignia.class))),
            @ApiResponse(responseCode = "500", description = "Error al obtener insignias")
    })
    public Response getInsigniasDeUsuario() {
        try {
            String id_usuario = securityContext.getUserPrincipal().getName();
            List<Insignia> insignias = uim.getInsigniasDeUsuario(id_usuario);
            return Response.ok(insignias).build();
        } catch (Exception e) {
            logger.error("Error al obtener insignias del usuario", e);
            return Response.status(500).entity("{\"error\":\"Error al obtener insignias\"}").build();
        }
    }

    @POST
    @Path("/{id_insignia}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Asignar insignia a usuario",
            description = "Asigna una insignia a un usuario si no la tiene ya",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Insignia asignada correctamente"),
            @ApiResponse(responseCode = "400", description = "La insignia ya está asignada"),
            @ApiResponse(responseCode = "500", description = "Error al asignar insignia")
    })
    public Response asignarInsignia(@PathParam("id_insignia") String id_insignia) {
        try {
            String id_usuario = securityContext.getUserPrincipal().getName();
            boolean asignada = uim.asignarInsigniaAUsuario(id_usuario, id_insignia);
            if (asignada) {
                return Response.status(201).entity("{\"message\":\"Insignia asignada correctamente\"}").build();
            } else {
                return Response.status(400).entity("{\"error\":\"La insignia ya está asignada\"}").build();
            }
        } catch (Exception e) {
            logger.error("Error al asignar insignia al usuario", e);
            return Response.status(500).entity("{\"error\":\"Error al asignar insignia\"}").build();
        }
    }

    @DELETE
    @Path("/{id_insignia}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Eliminar insignia de un usuario",
            description = "Elimina una insignia concreta de un usuario",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Insignia eliminada correctamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo eliminar la insignia"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response eliminarInsignia(@PathParam("id_insignia") String id_insignia) {
        try {
            String id_usuario = securityContext.getUserPrincipal().getName();
            boolean eliminada = uim.eliminarInsigniaDeUsuario(id_usuario, id_insignia);
            if (eliminada) {
                return Response.ok("{\"message\":\"Insignia eliminada correctamente\"}").build();
            } else {
                return Response.status(400).entity("{\"error\":\"No se pudo eliminar la insignia\"}").build();
            }
        } catch (Exception e) {
            logger.error("Error al eliminar insignia", e);
            return Response.status(500).entity("{\"error\":\"Error al eliminar insignia\"}").build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Eliminar todas las insignias de un usuario",
            description = "Elimina todas las insignias asignadas a un usuario",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Todas las insignias eliminadas correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response eliminarTodasInsigniasUsuario() {
        try {
            String id_usuario = securityContext.getUserPrincipal().getName();
            uim.eliminarTodasInsigniasDeUsuario(id_usuario);
            return Response.ok("{\"message\":\"Todas las insignias eliminadas correctamente\"}").build();
        } catch (Exception e) {
            logger.error("Error al eliminar todas las insignias del usuario", e);
            return Response.status(500).entity("{\"error\":\"Error al eliminar insignias\"}").build();
        }
    }

}