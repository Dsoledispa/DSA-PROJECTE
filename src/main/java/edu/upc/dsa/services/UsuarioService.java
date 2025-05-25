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
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Date;

@Path("/usuarios")
public class UsuarioService {

    final static Logger logger = Logger.getLogger(UsuarioService.class);
    private UsuarioManager um = new UsuarioManagerImpl();
    private static final String secretKey = "d42a2373271508dae325e933cddcfe13d504512272bdb6e89123f2e80717ad9d";

    // Generar token JWT
    private String generateToken(Usuario u) {
        return Jwts.builder()
                .setSubject(u.getNombreUsu())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @POST
    @Operation(summary = "Registrar usuario", description = "Crea un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado"),
            @ApiResponse(responseCode = "409", description = "Usuario ya existe")
    })
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newUsuario(Usuario u) {
        try {
            this.um.addUsuario(u);
            logger.info("Usuario registrado: " + u.getNombreUsu());
            return Response.status(201).entity("{\"mensaje\":\"Usuario registrado correctamente\"}").build();
        } catch (UsuarioYaExisteException e) {
            logger.warn("Intento de registrar usuario ya existente: " + u.getNombreUsu());
            return Response.status(409).entity("{\"error\":\"Usuario ya existe\"}").build();
        }
    }

    @POST
    @Operation(summary = "Login usuario", description = "Inicio de sesión y obtención de token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Login correcto, token devuelto"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "Contraseña incorrecta")
    })
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUsuario(Usuario u){
        try {
            Usuario usuarioLogueado = this.um.loginUsuario(u.getNombreUsu(), u.getPassword());
            String token = generateToken(usuarioLogueado);
            return Response.status(201).entity("{\"token\":\"" + token + "\"}").build();
        } catch (UsuarioNotFoundException e) {
            return Response.status(404).entity("{\"error\":\"Usuario no encontrado\"}").build();
        } catch (PasswordNotMatchException e) {
            return Response.status(401).entity("{\"error\":\"Contraseña incorrecta\"}").build();
        }
    }

    @GET
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve la lista de todos los usuarios registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios devuelta")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsuarios() {
        List<Usuario> usuarios = this.um.getAllUsuarios();
        return Response.ok(usuarios).build();
    }

    @GET
    @Operation(summary = "Obtener usuario por nombre", description = "Devuelve el usuario que coincide con el nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @Path("/{nombreUsu}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuario(@PathParam("nombreUsu") String nombreUsu) {
        try {
            Usuario u = this.um.getUsuario(nombreUsu);
            return Response.ok(u).build();
        } catch (UsuarioNotFoundException e) {
            return Response.status(404).entity("{\"error\":\"Usuario no encontrado\"}").build();
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
        }
    }


    @DELETE
    @Operation(summary = "Eliminar usuario", description = "Elimina el usuario con el nombre dado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @Path("/{nombreUsu}")
    public Response deleteUsuario(@PathParam("nombreUsu") String nombreUsu) {
        try {
            this.um.deleteUsuario(nombreUsu);
            return Response.ok("{\"mensaje\":\"Usuario eliminado\"}").build();
        } catch (UsuarioNotFoundException e) {
            return Response.status(404).entity("{\"error\":\"Usuario no encontrado\"}").build();
        }
    }
}
