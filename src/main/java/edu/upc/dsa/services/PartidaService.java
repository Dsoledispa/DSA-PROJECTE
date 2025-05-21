//package edu.upc.dsa.services;
//
//
//import edu.upc.dsa.config.IniciarDatos;
//import edu.upc.dsa.manager.PartidaManager;
//import edu.upc.dsa.manager.PartidaManagerImpl;
//import edu.upc.dsa.models.Partida;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
//import org.apache.log4j.Logger;
//
//import javax.ws.rs.*;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.SecurityContext;
//import java.util.List;
//
//@Api(value = "/partidas", description = "Servicio de partida")
//@Path("/partidas")
//public class PartidaService {
//
//    final static Logger logger = Logger.getLogger(PartidaService.class);
//    private final PartidaManager pm;
//
//    @Context
//    SecurityContext securityContext;
//
//    public PartidaService() {
//        this.pm = PartidaManagerImpl.getInstance();
//        IniciarDatos.init(); // Asegurarse de que los datos están cargados
//    }
//
//    @POST
//    @ApiOperation(value = "Crear una nueva partida", notes = "Crea una nueva partida para un usuario existente")
//    @ApiResponses(value = {
//            @ApiResponse(code = 201, message = "Partida registrada correctamente", response = Partida.class),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response newPartida() {
//        try {
//            String nombreUsu = securityContext.getUserPrincipal().getName();
//            Partida nuevaPartida = this.pm.addPartida(nombreUsu);
//            return Response.status(201).entity(nuevaPartida).build();
//        } catch (Exception e) {
//            logger.error("Error al crear la partida: " + e.getMessage(), e);
//            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
//        }
//    }
//
//    @GET
//    @ApiOperation(value = "Obtener las partidas del usuario", notes = "Lista las partidas del usuario autenticado")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Partidas obtenidas correctamente", response = Partida.class),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response obtenerPartidas() {
//        try {
//            String nombreUsu = securityContext.getUserPrincipal().getName();
//            List<Partida> partidas = this.pm.getPartidas(nombreUsu);
//            return Response.status(200).entity(partidas).build();
//        } catch (Exception e) {
//            logger.error("Error al obtener las partidas", e);
//            return Response.status(500).entity("{\"error\":\"Error al obtener las partidas\"}").build();
//        }
//    }
//
//    @GET
//    @Path("/{id_partida}")
//    @ApiOperation(value = "Obtener una partida específica del usuario", notes = "Obtiene una partida específica del usuario autenticado")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Partida obtenida correctamente", response = Partida.class),
//            @ApiResponse(code = 404, message = "Partida no encontrada"),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response obtenerPartida(@PathParam("id_partida") String id_partida) {
//        try {
//            String nombreUsu = securityContext.getUserPrincipal().getName();
//            Partida partida = this.pm.getPartida(nombreUsu, id_partida); // Obtiene la partida específica
//            return Response.status(200).entity(partida).build();
//        } catch (Exception e) {
//            logger.error("Error al obtener la partida", e);
//            return Response.status(500).entity("{\"error\":\"Error al obtener la partida\"}").build();
//        }
//    }
//
//    @DELETE
//    @Path("/{id_partida}")
//    @ApiOperation(value = "Eliminar partida", notes = "Elimina una partida del usuario autenticado")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Partida eliminada correctamente", response = Partida.class),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response eliminarPartida(@PathParam("id_partida") String id_partida) {
//        try {
//            String nombreUsu = securityContext.getUserPrincipal().getName();
//            this.pm.deletePartida(nombreUsu, id_partida);
//            return Response.status(200).entity("{\"message\":\"Partida eliminada\"}").build();
//        } catch (Exception e) {
//            logger.error("Error al eliminar la partida", e);
//            return Response.status(500).entity("{\"error\":\"Error al eliminar la partida\"}").build();
//        }
//    }
//
//    @GET
//    @Path("/monedas/{id_partida}")
//    @ApiOperation(value = "Obtener las monedas de una partida específica del usuario")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Monedas obtenidas correctamente", response = Integer.class),
//            @ApiResponse(code = 404, message = "Partida no encontrada"),
//            @ApiResponse(code = 500, message = "Error interno")
//    })
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response obtenerMonedasDePartida(@PathParam("id_partida") String id_partida) {
//        try {
//            String nombreUsu = securityContext.getUserPrincipal().getName();
//            int monedas = pm.getMonedasDePartida(nombreUsu, id_partida); // Obtiene las monedas de la partida
//            return Response.status(200).entity("{\"monedas\":" + monedas + "}").build();
//        } catch (Exception e) {
//            logger.error("Error al obtener las monedas de la partida", e);
//            return Response.status(500).entity("{\"error\":\"Error al obtener las monedas\"}").build();
//        }
//    }
//}
