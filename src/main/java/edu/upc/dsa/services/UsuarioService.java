package edu.upc.dsa.services;

import edu.upc.dsa.exceptions.usuario.PasswordNotMatchException;
import edu.upc.dsa.exceptions.usuario.UsuarioNotFoundException;
import edu.upc.dsa.exceptions.usuario.UsuarioYaExisteException;
import edu.upc.dsa.manager.UsuarioManager;
import edu.upc.dsa.manager.UsuarioManagerImpl;
import edu.upc.dsa.models.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Date;

@Path("/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con usuarios")
public class UsuarioService {

    final static Logger logger = Logger.getLogger(UsuarioService.class);
    private UsuarioManager um = new UsuarioManagerImpl();
    private static final String secretKey = "d42a2373271508dae325e933cddcfe13d504512272bdb6e89123f2e80717ad9d";

    // Generar token JWT
    private String generateToken(Usuario u) {
        return Jwts.builder()
                .setSubject(u.getId_usuario())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @POST
    @Operation(summary = "Registrar usuario (solo usar nombre y password)", description = "Crea un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado"),
            @ApiResponse(responseCode = "409", description = "Usuario ya existe")
    })
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newUsuario(Usuario u) {
        logger.info("Que tenemos de usuario : " +u);
        try {
            this.um.addUsuario(u);
            logger.info("Usuario registrado: " + u.getNombre());
            return Response.status(201).entity("{\"mensaje\":\"Usuario registrado correctamente\"}").build();
        } catch (UsuarioYaExisteException e) {
            logger.warn("Intento de registrar usuario ya existente: " + u.getNombre());
            return Response.status(409).entity("{\"error\":\"Usuario ya existe\"}").build();
        } catch (Exception e) {
            logger.error("Error al registrar usuario", e);
            return Response.serverError().entity("{\"error\":\"Error interno al registrar usuario\"}").build();
        }
    }

    @POST
    @Operation(summary = "Login usuario (solo usar nombre y password)", description = "Inicio de sesión y obtención de token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Login correcto, token devuelto"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "Contraseña incorrecta")
    })
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUsuario(Usuario u) {
        try {
            Usuario usuarioLogueado = this.um.loginUsuario(u.getNombre(), u.getPassword());
            String token = generateToken(usuarioLogueado);
            return Response.status(201).entity("{\"token\":\"" + token + "\"}").build();
        } catch (UsuarioNotFoundException e) {
            return Response.status(404).entity("{\"error\":\"Usuario no encontrado\"}").build();
        } catch (PasswordNotMatchException e) {
            return Response.status(401).entity("{\"error\":\"Contraseña incorrecta\"}").build();
        } catch (Exception e) {
            logger.error("Error en login", e);
            return Response.serverError().entity("{\"error\":\"Error interno en login\"}").build();
        }
    }

    @GET
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve la lista de todos los usuarios registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios devuelta")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsuarios() {
        try {
            List<Usuario> usuarios = this.um.getAllUsuarios();
            return Response.ok(usuarios).build();
        } catch (Exception e) {
            logger.error("Error al obtener todos los usuarios", e);
            return Response.serverError().entity("{\"error\":\"Error interno al obtener usuarios\"}").build();
        }
    }

    @GET
    @Operation(summary = "Obtener usuario por ID", description = "Devuelve el usuario que coincide con el ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @Path("/{id_usuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuario(@PathParam("id_usuario") String id_usuario) {
        try {
            Usuario u = this.um.getUsuario(id_usuario);
            return Response.ok(u).build();
        } catch (UsuarioNotFoundException e) {
            return Response.status(404).entity("{\"error\":\"Usuario no encontrado\"}").build();
        } catch (Exception e) {
            logger.error("Error al obtener usuario por ID", e);
            return Response.serverError().entity("{\"error\":\"Error interno al obtener usuario\"}").build();
        }
    }

    @GET
    @Operation(summary = "Obtener usuario por nombre", description = "Devuelve el usuario que coincide con el nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @Path("/nombre/{nombre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarioPorNombre(@PathParam("nombre") String nombre) {
        try {
            Usuario u = this.um.getUsuarioPorNombre(nombre);
            if (u == null) throw new UsuarioNotFoundException("Usuario no encontrado");
            return Response.ok(u).build();
        } catch (UsuarioNotFoundException e) {
            return Response.status(404).entity("{\"error\":\"Usuario no encontrado\"}").build();
        } catch (Exception e) {
            logger.error("Error al obtener usuario por nombre", e);
            return Response.serverError().entity("{\"error\":\"Error interno al obtener usuario\"}").build();
        }
    }

    @PUT
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos del usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUsuario(Usuario usuarioActualizado) {
        try {
            this.um.updateUsuario(usuarioActualizado);
            return Response.ok("{\"mensaje\":\"Usuario actualizado\"}").build();
        } catch (UsuarioNotFoundException e) {
            return Response.status(404).entity("{\"error\":\"Usuario no encontrado\"}").build();
        } catch (Exception e) {
            logger.error("Error al actualizar usuario", e);
            return Response.serverError().entity("{\"error\":\"Error interno al actualizar usuario\"}").build();
        }
    }


    @DELETE
    @Operation(summary = "Eliminar usuario", description = "Elimina el usuario con el nombre dado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @Path("/{id_usuario}")
    public Response deleteUsuario(@PathParam("id_usuario") String id_usuario) {
        try {
            this.um.deleteUsuario(id_usuario);
            return Response.ok("{\"mensaje\":\"Usuario eliminado\"}").build();
        } catch (UsuarioNotFoundException e) {
            return Response.status(404).entity("{\"error\":\"Usuario no encontrado\"}").build();
        }
    }

    @DELETE
    @Operation(summary = "Eliminar todos los usuarios", description = "Elimina todos los usuarios del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todos los usuarios eliminados")
    })
    public Response deleteAllUsuarios() {
        try {
            this.um.deleteAllUsuarios();
            return Response.ok("{\"mensaje\":\"Todos los usuarios eliminados\"}").build();
        } catch (Exception e) {
            logger.error("Error al eliminar todos los usuarios", e);
            return Response.serverError().entity("{\"error\":\"Error interno al eliminar usuarios\"}").build();
        }
    }
}
