package edu.upc.dsa.services;

import edu.upc.dsa.manager.CarritoManager;
import edu.upc.dsa.manager.CarritoManagerImpl;
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

@Path("/carrito")
@Tag(name = "Carrito", description = "Operaciones del carrito de compras")
public class CarritoService {

    final static Logger logger = Logger.getLogger(CarritoService.class);
    private final CarritoManager cm = new CarritoManagerImpl();

    @Context
    SecurityContext securityContext;

    @GET
    @Path("/{id_partida}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Obtener objetos del carrito",
            description = "Devuelve los objetos en el carrito de la partida dada",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de objetos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Objeto.class))),
            @ApiResponse(responseCode = "500", description = "Error al obtener los objetos del carrito")
    })
    public Response getObjetosDelCarrito(@PathParam("id_partida") String id_partida) {
        try {
            List<Objeto> objetos = cm.getObjetosDelCarrito(id_partida);
            return Response.ok(objetos).build();
        } catch (Exception e) {
            logger.error("Error al obtener objetos del carrito", e);
            return Response.status(500).entity("{\"error\":\"Error al obtener objetos del carrito\"}").build();
        }
    }

    @POST
    @Path("/{id_partida}/{id_objeto}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Agregar objeto al carrito",
            description = "Agrega un objeto al carrito de la partida dada",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Objeto agregado al carrito"),
            @ApiResponse(responseCode = "400", description = "No se pudo agregar el objeto"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response agregarObjetoACarrito(@PathParam("id_partida") String id_partida,
                                          @PathParam("id_objeto") String id_objeto) {
        try {
            if (cm.agregarObjetoACarrito(id_partida, id_objeto) != null) {
                return Response.status(201).entity("{\"message\":\"Objeto agregado al carrito\"}").build();
            }
            return Response.status(400).entity("{\"error\":\"No se pudo agregar el objeto al carrito\"}").build();
        } catch (Exception e) {
            logger.error("Error al agregar objeto al carrito", e);
            return Response.status(500).entity("{\"error\":\"Error al agregar objeto al carrito\"}").build();
        }
    }

    @DELETE
    @Path("/{id_partida}/{id_objeto}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Eliminar objeto del carrito",
            description = "Elimina un objeto específico del carrito de la partida dada",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Objeto eliminado del carrito"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar el objeto")
    })
    public Response eliminarObjetoDelCarrito(@PathParam("id_partida") String id_partida,
                                             @PathParam("id_objeto") String id_objeto) {
        try {
            cm.eliminarObjetoDeCarrito(id_partida, id_objeto);
            return Response.ok("{\"message\":\"Objeto eliminado del carrito\"}").build();
        } catch (Exception e) {
            logger.error("Error al eliminar objeto del carrito", e);
            return Response.status(500).entity("{\"error\":\"Error al eliminar objeto del carrito\"}").build();
        }
    }

    @POST
    @Path("/comprar/{id_partida}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Realizar compra",
            description = "Realiza la compra de todos los objetos en el carrito de la partida dada",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Compra realizada con éxito"),
            @ApiResponse(responseCode = "400", description = "Fondos insuficientes o partida no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public Response realizarCompra(@PathParam("id_partida") String id_partida) {
        try {
            String id_usuario = securityContext.getUserPrincipal().getName();
            boolean resultado = cm.realizarCompra(id_usuario, id_partida);
            if (resultado) {
                return Response.ok("{\"message\":\"Compra realizada con éxito\"}").build();
            } else {
                return Response.status(400).entity("{\"error\":\"Fondos insuficientes o partida no encontrada\"}").build();
            }
        } catch (Exception e) {
            logger.error("Error al realizar la compra", e);
            return Response.status(500).entity("{\"error\":\"Error al realizar la compra\"}").build();
        }
    }

    @DELETE
    @Path("/vaciar/{id_partida}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Vaciar carrito",
            description = "Elimina todos los objetos del carrito de la partida dada",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito vaciado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error al vaciar el carrito")
    })
    public Response vaciarCarrito(@PathParam("id_partida") String id_partida) {
        try {
            cm.vaciarCarrito(id_partida);
            return Response.ok("{\"message\":\"Carrito vaciado correctamente\"}").build();
        } catch (Exception e) {
            logger.error("Error al vaciar el carrito", e);
            return Response.status(500).entity("{\"error\":\"Error al vaciar el carrito\"}").build();
        }
    }
}
