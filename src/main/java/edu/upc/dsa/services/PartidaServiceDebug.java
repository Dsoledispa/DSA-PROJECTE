//package edu.upc.dsa.services;
//
//import edu.upc.dsa.config.IniciarDatos;
//import edu.upc.dsa.manager.PartidaManager;
//import edu.upc.dsa.manager.PartidaManagerImpl;
//import edu.upc.dsa.models.Partida;
//import io.swagger.annotations.*;
//import org.apache.log4j.Logger;
//
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.util.List;
//
//@Api(value = "/partidas/debug", description = "Servicio de prueba para partidas (Swagger sin JWT)")
//@Path("/partidas/debug")
//public class PartidaServiceDebug {
//
//    final static Logger logger = Logger.getLogger(PartidaServiceDebug.class);
//    private final PartidaManager pm;
//
//    public PartidaServiceDebug() {
//        this.pm = PartidaManagerImpl.getInstance();
//        IniciarDatos.init(); // Cargar datos para pruebas
//    }
//
//    @POST
//    @Path("/crearPartida")
//    @ApiOperation(value = "Crear una nueva partida (usuario de prueba)", notes = "Crea una nueva partida con un usuario fijo para pruebas")
//    @ApiResponses(value = {
//            @ApiResponse(code = 201, message = "Partida creada correctamente"),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response crearPartidaDesdeSwagger() {
//        String nombreUsu = "Diego";
//
//        try {
//            this.pm.addPartida(nombreUsu);
//            return Response.status(201).entity("{\"mensaje\":\"Partida creada para " + nombreUsu + "\"}").build();
//        } catch (Exception e) {
//            logger.error("Error creando partida de prueba", e);
//            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
//        }
//    }
//
//    @GET
//    @Path("/obtenerPartidas")
//    @ApiOperation(value = "Obtener partidas del usuario de prueba", notes = "Devuelve todas las partidas del usuario fijo")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Partidas obtenidas correctamente"),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response obtenerPartidasDesdeSwagger() {
//        String nombreUsu = "Diego";
//
//        try {
//            List<Partida> partidas = this.pm.getPartidas(nombreUsu);
//            return Response.status(200).entity(partidas).build();
//        } catch (Exception e) {
//            logger.error("Error obteniendo partidas de prueba", e);
//            return Response.status(500).entity("{\"error\":\"Error al obtener las partidas\"}").build();
//        }
//    }
//
//    @DELETE
//    @Path("/eliminarPartida/{id_partida}")
//    @ApiOperation(value = "Eliminar partida del usuario de prueba", notes = "Elimina una partida específica del usuario fijo")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Partida eliminada correctamente"),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response eliminarPartidaDesdeSwagger(@PathParam("id_partida") String id_partida) {
//        String nombreUsu = "Diego";
//
//        try {
//            this.pm.deletePartida(nombreUsu, id_partida);
//            return Response.status(200).entity("{\"message\":\"Partida eliminada\"}").build();
//        } catch (Exception e) {
//            logger.error("Error eliminando partida de prueba", e);
//            return Response.status(500).entity("{\"error\":\"Error al eliminar la partida\"}").build();
//        }
//    }
//}