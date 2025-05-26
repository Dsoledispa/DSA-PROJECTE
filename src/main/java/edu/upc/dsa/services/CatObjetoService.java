package edu.upc.dsa.services;

import edu.upc.dsa.manager.CatObjetoManager;
import edu.upc.dsa.manager.CatObjetoManagerImpl;
import edu.upc.dsa.models.CategoriaObjeto;
import edu.upc.dsa.exceptions.categoriaObjeto.CatObjetoYaExisteException;
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

@Path("/categoria")
@Tag(name = "Categorías", description = "Operaciones relacionadas con las categorías de objetos")
public class CatObjetoService {

    final static Logger logger = Logger.getLogger(CatObjetoService.class);
    private final CatObjetoManager com = new CatObjetoManagerImpl();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener todas las categorías", description = "Devuelve todas las categorías de objetos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías devuelta",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriaObjeto.class)))
    })
    public Response getAllCategorias() {
        try {
            List<CategoriaObjeto> categorias = com.getAllCatObjeto();
            return Response.ok(categorias).build();
        } catch (Exception e) {
            logger.error("Error al obtener categorías", e);
            return Response.status(500).entity("{\"error\":\"Error interno al obtener categorías\"}").build();
        }
    }

    @GET
    @Path("/{id_categoria}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener categoría por ID", description = "Devuelve una categoría dado su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriaObjeto.class))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public Response getCategoriaById(@PathParam("id_categoria") String id_categoria) {
        try {
            CategoriaObjeto categoria = com.getCatObjeto(id_categoria);
            if (categoria != null) return Response.ok(categoria).build();
            return Response.status(404).entity("{\"error\":\"Categoría no encontrada\"}").build();
        } catch (Exception e) {
            logger.error("Error al obtener categoría por ID", e);
            return Response.status(500).entity("{\"error\":\"Error interno al obtener categoría\"}").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Añadir una categoría", description = "Añade una nueva categoría de objeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría añadida correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriaObjeto.class))),
            @ApiResponse(responseCode = "400", description = "Error al añadir categoría")
    })
    public Response addCategoria(CategoriaObjeto categoria) {
        try {
            CategoriaObjeto creada = com.addCatObjeto(categoria);
            return Response.status(201).entity(creada).build();
        } catch (CatObjetoYaExisteException e) {
            logger.warn("Intento de añadir una categoría ya existente", e);
            return Response.status(400).entity("{\"error\":\"Ya existe una categoría con ese nombre\"}").build();
        } catch (Exception e) {
            logger.error("Error al añadir categoría", e);
            return Response.status(500).entity("{\"error\":\"Error interno al añadir categoría\"}").build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Actualizar categoría", description = "Actualiza los datos de una categoría existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Ya existe otra categoría con ese nombre")
    })
    public Response updateCategoria(CategoriaObjeto categoria) {
        try {
            com.updateCatObjeto(categoria);
            return Response.ok("{\"mensaje\":\"Categoría actualizada correctamente\"}").build();
        } catch (CatObjetoYaExisteException e) {
            logger.warn("Conflicto al actualizar: nombre duplicado", e);
            return Response.status(400).entity("{\"error\":\"Ya existe otra categoría con ese nombre\"}").build();
        } catch (Exception e) {
            logger.error("Error al actualizar categoría", e);
            return Response.status(500).entity("{\"error\":\"Error interno al actualizar categoría\"}").build();
        }
    }

    @DELETE
    @Path("/{id_categoria}")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría de objeto por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría eliminada correctamente")
    })
    public Response deleteCategoria(@PathParam("id_categoria") String id_categoria) {
        try {
            com.deleteCatObjeto(id_categoria);
            return Response.ok("{\"mensaje\":\"Categoría eliminada correctamente\"}").build();
        } catch (Exception e) {
            logger.error("Error al eliminar categoría", e);
            return Response.status(500).entity("{\"error\":\"Error interno al eliminar categoría\"}").build();
        }
    }
}
