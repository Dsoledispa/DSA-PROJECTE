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
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Api(value = "/tienda", description = "Servicio de tienda")
//@Path("/tienda")
//public class TiendaService {
//
//    final static Logger logger = Logger.getLogger(TiendaService.class);
//    private final TiendaManager tm;
//
//    public TiendaService() {
//        this.tm = TiendaManagerImpl.getInstance();
//        IniciarDatos.init();
//    }
//
//    @GET
//    @ApiOperation(value = "Obtener todos los productos", notes = "Lista todos los productos de la tienda")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Lista de productos", response = Objeto.class),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Path("/productos")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getProductos() {
//        try {
//            List<Objeto> productos = this.tm.getAllProductos();
//            return Response.status(200).entity(productos).build();
//        } catch (Exception e) {
//            logger.error("Error al obtener productos", e);
//            return Response.status(500).entity("{\"error\":\"Error al obtener productos\"}").build();
//        }
//    }
//
//    @GET
//    @ApiOperation(value = "Obtener productos por categoría", notes = "Devuelve los productos de una categoría")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Productos por categoría", response = Objeto.class),
//            @ApiResponse(code = 400, message = "Categoría no válida"),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Path("/categorias/{categoria}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getProductosPorCategoria(@PathParam("categoria") String categoria) {
//        try {
//            CategoriaObjeto cat;
//            try {
//                cat = CategoriaObjeto.valueOf(categoria.toUpperCase());
//            } catch (IllegalArgumentException e) {
//                return Response.status(400).entity("{\"error\":\"Categoría no válida\"}").build();
//            }
//
//            List<Objeto> productos = this.tm.getProductosPorCategoria(cat);
//            return Response.status(200).entity(productos).build();
//        } catch (Exception e) {
//            logger.error("Error al obtener productos por categoría", e);
//            return Response.status(500).entity("{\"error\":\"Error interno\"}").build();
//        }
//    }
//
//    @GET
//    @ApiOperation(value = "Obtener un producto por ID", notes = "Devuelve el producto con el ID dado")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Producto encontrado", response = Objeto.class),
//            @ApiResponse(code = 404, message = "Producto no encontrado")
//    })
//    @Path("/productos/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getProducto(@PathParam("id") String id) {
//        Objeto producto = this.tm.getProductoPorId(id);
//        if (producto == null) {
//            return Response.status(404).entity("{\"error\":\"Producto no encontrado\"}").build();
//        }
//        return Response.status(200).entity(producto).build();
//    }
//
//    @GET
//    @ApiOperation(value = "Obtener producto aleatorio", notes = "Devuelve un producto aleatorio de la tienda")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Producto aleatorio obtenido", response = Objeto.class),
//            @ApiResponse(code = 404, message = "No hay productos disponibles")
//    })
//    @Path("/productoAleatorio")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getProductoAleatorio() {
//        Objeto producto = this.tm.getProductoAleatorio();
//        if (producto == null) {
//            return Response.status(404).entity("{\"error\":\"No hay productos disponibles\"}").build();
//        }
//        return Response.status(200).entity(producto).build();
//    }
//
//    @GET
//    @ApiOperation(value = "Obtener categorías disponibles", notes = "Devuelve todas las categorías existentes")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Categorías obtenidas", response = String.class),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Path("/categorias")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getCategorias() {
//        try {
//            List<String> categorias = Arrays.stream(CategoriaObjeto.values())
//                    .map(Enum::name)
//                    .collect(Collectors.toList());
//            return Response.status(200).entity(categorias).build();
//        } catch (Exception e) {
//            logger.error("Error al obtener categorías", e);
//            return Response.status(500).entity("{\"error\":\"Error al obtener categorías\"}").build();
//        }
//    }
//}
