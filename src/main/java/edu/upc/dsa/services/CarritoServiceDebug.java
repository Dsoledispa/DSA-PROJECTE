//package edu.upc.dsa.services;
//
//import edu.upc.dsa.config.IniciarDatos;
//import edu.upc.dsa.manager.CarritoManager;
//import edu.upc.dsa.manager.CarritoManagerImpl;
//import edu.upc.dsa.manager.TiendaManager;
//import edu.upc.dsa.manager.TiendaManagerImpl;
//import edu.upc.dsa.models.Objeto;
//import io.swagger.annotations.*;
//import org.apache.log4j.Logger;
//
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.util.List;
//
//@Api(value = "/carrito/debug", description = "Servicio de carrito de compras (modo debug sin JWT)")
//@Path("/carrito/debug")
//public class CarritoServiceDebug {
//
//    final static Logger logger = Logger.getLogger(CarritoServiceDebug.class);
//    private final CarritoManager cm;
//    private final TiendaManager tm;
//
//    public CarritoServiceDebug() {
//        this.cm = CarritoManagerImpl.getInstance();
//        this.tm = TiendaManagerImpl.getInstance();
//        IniciarDatos.init(); // Datos de prueba
//    }
//
//    @GET
//    @ApiOperation(value = "Obtener productos del carrito del usuario Diego")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Carrito obtenido correctamente"),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getProductosDelCarrito() {
//        String nombreUsu = "Diego";
//
//        try {
//            List<Objeto> objetos = cm.getProductosDelCarrito(nombreUsu);
//            return Response.ok(objetos).build();
//        } catch (Exception e) {
//            logger.error("Error al obtener productos del carrito", e);
//            return Response.status(500).entity("{\"error\":\"Error al obtener productos del carrito\"}").build();
//        }
//    }
//
//    @POST
//    @Path("/{id_producto}")
//    @ApiOperation(value = "Agregar producto al carrito del usuario Diego")
//    @ApiResponses(value = {
//            @ApiResponse(code = 201, message = "Producto agregado correctamente"),
//            @ApiResponse(code = 404, message = "Producto no encontrado"),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response agregarProducto(@PathParam("id_producto") String id_producto) {
//        String nombreUsu = "Diego";
//
//        try {
//            Objeto producto = tm.getProductoPorId(id_producto);
//
//            if (producto == null) {
//                return Response.status(404).entity("{\"error\":\"Producto no encontrado\"}").build();
//            }
//
//            cm.agregarProductoAlCarrito(nombreUsu, producto);
//            return Response.status(201).entity("{\"message\":\"Producto agregado al carrito\"}").build();
//        } catch (Exception e) {
//            logger.error("Error al agregar producto al carrito", e);
//            return Response.status(500).entity("{\"error\":\"Error al agregar producto al carrito\"}").build();
//        }
//    }
//
//    @DELETE
//    @Path("/{id_producto}")
//    @ApiOperation(value = "Eliminar producto del carrito del usuario Diego")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Producto eliminado correctamente"),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response eliminarProducto(@PathParam("id_producto") String id_producto) {
//        String nombreUsu = "Diego";
//
//        try {
//            cm.eliminarProductoDelCarrito(nombreUsu, id_producto);
//            return Response.ok("{\"message\":\"Producto eliminado del carrito\"}").build();
//        } catch (Exception e) {
//            logger.error("Error al eliminar producto del carrito", e);
//            return Response.status(500).entity("{\"error\":\"Error al eliminar producto del carrito\"}").build();
//        }
//    }
//
//    @POST
//    @Path("/comprar/{id_partida}")
//    @ApiOperation(value = "Realizar compra desde el carrito del usuario Diego")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Compra realizada correctamente"),
//            @ApiResponse(code = 400, message = "Fondos insuficientes o partida no encontrada"),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response realizarCompra(@PathParam("id_partida") String id_partida) {
//        String nombreUsu = "Diego";
//
//        try {
//            boolean resultado = cm.realizarCompra(nombreUsu, id_partida);
//
//            if (resultado) {
//                return Response.ok("{\"message\":\"Compra realizada con éxito\"}").build();
//            } else {
//                return Response.status(400).entity("{\"error\":\"Compra fallida: fondos insuficientes o partida no encontrada\"}").build();
//            }
//        } catch (Exception e) {
//            logger.error("Error al realizar la compra", e);
//            return Response.status(500).entity("{\"error\":\"Error al realizar la compra\"}").build();
//        }
//    }
//
//    @DELETE
//    @ApiOperation(value = "Vaciar el carrito del usuario Diego")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Carrito vaciado correctamente"),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response vaciarCarrito() {
//        String nombreUsu = "Diego";
//
//        try {
//            cm.getCarrito(nombreUsu).vaciar();
//            return Response.ok("{\"message\":\"Carrito vaciado correctamente\"}").build();
//        } catch (Exception e) {
//            logger.error("Error al vaciar el carrito", e);
//            return Response.status(500).entity("{\"error\":\"Error al vaciar el carrito\"}").build();
//        }
//    }
//
//}
//
//
