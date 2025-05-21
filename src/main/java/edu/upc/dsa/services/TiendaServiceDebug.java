//package edu.upc.dsa.services;
//
//import edu.upc.dsa.config.IniciarDatos;
//import edu.upc.dsa.manager.TiendaManager;
//import edu.upc.dsa.manager.TiendaManagerImpl;
//import edu.upc.dsa.models.Objeto;
//import io.swagger.annotations.*;
//import org.apache.log4j.Logger;
//
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.util.Arrays;
//import java.util.List;
//
//@Api(value = "/tienda/debug", description = "Servicio de prueba para tienda (Swagger sin JWT)")
//@Path("/tienda/debug")
//public class TiendaServiceDebug {
//
//    final static Logger logger = Logger.getLogger(TiendaServiceDebug.class);
//    private final TiendaManager tm;
//
//    public TiendaServiceDebug() {
//        this.tm = TiendaManagerImpl.getInstance();
//        IniciarDatos.init();
//    }
//
//    @GET
//    @Path("/productos")
//    @ApiOperation(value = "Obtener todos los productos")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Productos obtenidos correctamente"),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getAllProductos() {
//        try {
//            List<Objeto> productos = tm.getAllProductos();
//            return Response.ok(productos).build();
//        } catch (Exception e) {
//            logger.error("Error al obtener productos", e);
//            return Response.status(500).entity("{\"error\":\"Error al obtener productos\"}").build();
//        }
//    }
//
//    @GET
//    @Path("/productos/{categoria}")
//    @ApiOperation(value = "Obtener productos por categoría")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Productos por categoría obtenidos correctamente"),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getProductosPorCategoria(@PathParam("categoria") CategoriaObjeto categoria) {
//        try {
//            List<Objeto> productos = tm.getProductosPorCategoria(categoria);
//            return Response.ok(productos).build();
//        } catch (Exception e) {
//            logger.error("Error al obtener productos por categoría", e);
//            return Response.status(500).entity("{\"error\":\"Error al obtener productos por categoría\"}").build();
//        }
//    }
//
//    @GET
//    @Path("/producto/{id}")
//    @ApiOperation(value = "Obtener producto por ID")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Producto encontrado"),
//            @ApiResponse(code = 404, message = "Producto no encontrado"),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getProductoPorId(@PathParam("id") String id) {
//        try {
//            Objeto producto = tm.getProductoPorId(id);
//            if (producto == null) {
//                return Response.status(404).entity("{\"error\":\"Producto no encontrado\"}").build();
//            }
//            return Response.ok(producto).build();
//        } catch (Exception e) {
//            logger.error("Error al obtener producto por ID", e);
//            return Response.status(500).entity("{\"error\":\"Error interno\"}").build();
//        }
//    }
//
//    @GET
//    @Path("/categorias")
//    @ApiOperation(value = "Obtener categorías disponibles")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Categorías obtenidas correctamente"),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getCategorias() {
//        try {
//            List<CategoriaObjeto> categorias = Arrays.asList(CategoriaObjeto.values());
//            return Response.ok(categorias).build();
//        } catch (Exception e) {
//            logger.error("Error al obtener categorías", e);
//            return Response.status(500).entity("{\"error\":\"Error al obtener categorías\"}").build();
//        }
//    }
//
//    @POST
//    @Path("/addProducto")
//    @ApiOperation(value = "Añadir producto (admin)")
//    @ApiResponses(value = {
//            @ApiResponse(code = 201, message = "Producto añadido correctamente"),
//            @ApiResponse(code = 500, message = "Error al añadir producto")
//    })
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response addProducto(Objeto producto) {
//        try {
//            tm.addProducto(producto);
//            return Response.status(201).entity("{\"mensaje\":\"Producto añadido correctamente\"}").build();
//        } catch (Exception e) {
//            logger.error("Error al añadir producto", e);
//            return Response.status(500).entity("{\"error\":\"Error al añadir producto\"}").build();
//        }
//    }
//
//    @DELETE
//    @Path("/deleteProducto/{id}")
//    @ApiOperation(value = "Eliminar producto (admin)")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Producto eliminado correctamente"),
//            @ApiResponse(code = 404, message = "Producto no encontrado"),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response deleteProducto(@PathParam("id") String id) {
//        try {
//            tm.deleteProducto(id);
//            return Response.ok("{\"mensaje\":\"Producto eliminado correctamente\"}").build();
//        } catch (Exception e) {
//            logger.error("Error al eliminar producto", e);
//            return Response.status(500).entity("{\"error\":\"Error interno\"}").build();
//        }
//    }
//}
//
