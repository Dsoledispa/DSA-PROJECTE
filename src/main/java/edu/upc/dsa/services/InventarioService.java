package edu.upc.dsa.services;

import edu.upc.dsa.manager.InventarioManager;
import edu.upc.dsa.manager.InventarioManagerImpl;
import edu.upc.dsa.models.Objeto;
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

@Path("/inventario")
@Tag(name = "Inventario", description = "Operaciones sobre el inventario de partidas")
public class InventarioService {

    final static Logger logger = Logger.getLogger(InventarioService.class);
    private final InventarioManager im = new InventarioManagerImpl();

    @Context
    SecurityContext securityContext;

    @GET
    @Path("/{id_partida}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Obtener inventario de una partida",
            description = "Devuelve la lista de objetos del inventario de una partida",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventario obtenido correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Objeto.class))),
            @ApiResponse(responseCode = "500", description = "Error al obtener inventario")
    })
    public Response getInventario(@PathParam("id_partida") String id_partida) {
        try {
            List<Objeto> objetos = im.getInventarioDePartida(id_partida);
            return Response.ok(objetos).build();
        } catch (Exception e) {
            logger.error("Error al obtener inventario", e);
            return Response.status(500).entity("{\"error\":\"Error al obtener inventario\"}").build();
        }
    }

    @POST
    @Path("/{id_partida}/{id_objeto}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Agregar objeto al inventario",
            description = "Agrega un objeto al inventario de una partida",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Objeto agregado al inventario"),
            @ApiResponse(responseCode = "400", description = "No se pudo agregar el objeto"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response agregarObjeto(@PathParam("id_partida") String id_partida,
                                  @PathParam("id_objeto") String id_objeto) {
        try {
            boolean agregado = im.agregarObjetoAInventario(id_partida, id_objeto);
            if (agregado) {
                return Response.status(201).entity("{\"message\":\"Objeto agregado al inventario\"}").build();
            } else {
                return Response.status(400).entity("{\"error\":\"No se pudo agregar el objeto\"}").build();
            }
        } catch (Exception e) {
            logger.error("Error al agregar objeto al inventario", e);
            return Response.status(500).entity("{\"error\":\"Error al agregar objeto al inventario\"}").build();
        }
    }

    @POST
    @Path("/aleatorio/{id_partida}/{id_objeto}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Comprar un objeto aleatorio y guardarlo en el inventario",
            description = "Resta monedas de la partida y guarda el objeto aleatorio comprado en el inventario",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Objeto comprado y añadido al inventario"),
            @ApiResponse(responseCode = "400", description = "Fondos insuficientes o partida no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar la compra")
    })
    public Response comprarObjetoAleatorio(@PathParam("id_partida") String id_partida, @PathParam("id_objeto") String id_objeto) {
        try {
            String id_usuario = securityContext.getUserPrincipal().getName();
            boolean success = im.PagarYGuardarObjetoInventario(id_usuario, id_partida, id_objeto);
            if (success) {
                return Response.ok("{\"message\":\"Compra realizada y objeto añadido al inventario\"}").build();
            } else {
                return Response.status(400).entity("{\"error\":\"No se pudo completar la compra\"}").build();
            }
        } catch (Exception e) {
            logger.error("Error al realizar la compra de objeto", e);
            return Response.status(500).entity("{\"error\":\"Error al realizar la compra\"}").build();
        }
    }

    @DELETE
    @Path("/{id_partida}/{id_objeto}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Eliminar objeto del inventario",
            description = "Elimina un objeto del inventario de una partida",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Objeto eliminado del inventario"),
            @ApiResponse(responseCode = "400", description = "No se pudo eliminar el objeto"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response eliminarObjeto(@PathParam("id_partida") String id_partida,
                                   @PathParam("id_objeto") String id_objeto) {
        try {
            boolean eliminado = im.eliminarObjetoDeInventario(id_partida, id_objeto);
            if (eliminado) {
                return Response.ok("{\"message\":\"Objeto eliminado del inventario\"}").build();
            } else {
                return Response.status(400).entity("{\"error\":\"No se pudo eliminar el objeto\"}").build();
            }
        } catch (Exception e) {
            logger.error("Error al eliminar objeto del inventario", e);
            return Response.status(500).entity("{\"error\":\"Error al eliminar objeto del inventario\"}").build();
        }
    }

    @DELETE
    @Path("/{id_partida}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Eliminar todos los objetos del inventario de una partida",
            description = "Elimina todos los objetos del inventario de la partida especificada",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventario eliminado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno al eliminar inventario")
    })
    public Response eliminarTodosLosObjetos(@PathParam("id_partida") String id_partida) {
        try {
            im.eliminarAllObjetosDeInventario(id_partida);
            return Response.ok("{\"message\":\"Inventario eliminado correctamente\"}").build();
        } catch (Exception e) {
            logger.error("Error al eliminar inventario de la partida " + id_partida, e);
            return Response.status(500).entity("{\"error\":\"Error al eliminar inventario\"}").build();
        }
    }

}
