package edu.upc.dsa.services;

import edu.upc.dsa.manager.TiendaManager;
import edu.upc.dsa.manager.TiendaManagerImpl;
import edu.upc.dsa.models.Objeto;
import edu.upc.dsa.models.CategoriaObjeto;
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

@Path("/tienda")
@Tag(name = "Tienda", description = "Operaciones relacionadas con Objeto")
public class TiendaService {

    final static Logger logger = Logger.getLogger(TiendaService.class);
    private final TiendaManager tm = new TiendaManagerImpl();

    @GET
    @Path("/productos")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener todos los productos", description = "Devuelve todos los productos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos devuelta",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Objeto.class)))
    })
    public Response getAllProductos() {
        try {
            List<Objeto> productos = tm.getAllProductos();
            return Response.ok(productos).build();
        } catch (Exception e) {
            logger.error("Error al obtener productos", e);
            return Response.status(500).entity("{\"error\":\"Error interno al obtener productos\"}").build();
        }
    }

    @GET
    @Path("/productos/categoria/{id_categoria}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener productos por categoría", description = "Devuelve los productos de una categoría específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos de la categoría devuelta",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Objeto.class)))
    })
    public Response getProductosPorCategoria(@PathParam("id_categoria") String id_categoria) {
        try {
            List<Objeto> productos = tm.getProductosPorCategoria(id_categoria);
            return Response.ok(productos).build();
        } catch (Exception e) {
            logger.error("Error al obtener productos por categoría", e);
            return Response.status(500).entity("{\"error\":\"Error interno al obtener productos por categoría\"}").build();
        }
    }

    @GET
    @Path("/producto/{id_producto}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener producto por ID", description = "Devuelve un producto dado su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Objeto.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public Response getProductoPorId(@PathParam("id_producto") String id_producto) {
        try {
            Objeto producto = tm.getProductoPorId(id_producto);
            if (producto != null) return Response.ok(producto).build();
            return Response.status(404).entity("{\"error\":\"Producto no encontrado\"}").build();
        } catch (Exception e) {
            logger.error("Error al obtener producto por ID", e);
            return Response.status(500).entity("{\"error\":\"Error interno al obtener producto\"}").build();
        }
    }

    @GET
    @Path("/producto/aleatorio")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener producto aleatorio", description = "Devuelve un producto aleatorio del catálogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto aleatorio devuelto",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Objeto.class))),
            @ApiResponse(responseCode = "404", description = "No hay productos disponibles")
    })
    public Response getProductoAleatorio() {
        try {
            Objeto producto = tm.getProductoAleatorio();
            if (producto != null) return Response.ok(producto).build();
            return Response.status(404).entity("{\"error\":\"No hay productos disponibles\"}").build();
        } catch (Exception e) {
            logger.error("Error al obtener producto aleatorio", e);
            return Response.status(500).entity("{\"error\":\"Error interno al obtener producto aleatorio\"}").build();
        }
    }

    @POST
    @Path("/producto")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Añadir un producto", description = "Añade un nuevo producto al catálogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto añadido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Objeto.class))),
            @ApiResponse(responseCode = "400", description = "Error al añadir producto")
    })
    public Response addProducto(Objeto producto) {
        try {
            Objeto creado = tm.addProducto(producto);
            if (creado != null) return Response.status(201).entity(creado).build();
            return Response.status(400).entity("{\"error\":\"No se pudo añadir el producto\"}").build();
        } catch (Exception e) {
            logger.error("Error al añadir producto", e);
            return Response.status(500).entity("{\"error\":\"Error interno al añadir producto\"}").build();
        }
    }

    @PUT
    @Path("/producto")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Actualizar producto", description = "Actualiza los datos de un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente")
    })
    public Response updateProducto(Objeto producto) {
        try {
            tm.updateProducto(producto);
            return Response.ok("{\"mensaje\":\"Producto actualizado correctamente\"}").build();
        } catch (Exception e) {
            logger.error("Error al actualizar producto", e);
            return Response.status(500).entity("{\"error\":\"Error interno al actualizar producto\"}").build();
        }
    }

    @DELETE
    @Path("/producto/{id_producto}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del catálogo por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente")
    })
    public Response deleteProducto(@PathParam("id_producto") String id_producto) {
        try {
            tm.deleteProducto(id_producto);
            return Response.ok("{\"mensaje\":\"Producto eliminado correctamente\"}").build();
        } catch (Exception e) {
            logger.error("Error al eliminar producto", e);
            return Response.status(500).entity("{\"error\":\"Error interno al eliminar producto\"}").build();
        }
    }
}

