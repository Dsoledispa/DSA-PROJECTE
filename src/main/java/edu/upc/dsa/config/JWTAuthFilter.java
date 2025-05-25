package edu.upc.dsa.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.ws.rs.container.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@Provider
@PreMatching
public class JWTAuthFilter implements ContainerRequestFilter {

    private static final String secretKey = "d42a2373271508dae325e933cddcfe13d504512272bdb6e89123f2e80717ad9d"; // Clave secreta

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();

        // Excluir rutas que contienen "usuarios"
        if (path.contains("usuarios")) {
            return;
        }

        // Excluir rutas de swagger, api-docs, wadl, etc
        if (path.startsWith("swagger") ||
                path.equals("swagger.json") ||
                path.equals("application.wadl") ||
                path.startsWith("webjars/") ||
                path.contains("api-docs") ||
                path.contains("openapi") ||
                path.contains("debug")) {
            return;
        }
        // Obtener el encabezado "Authorization"
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Token no proporcionado o inválido\"}")
                    .build());
            return;
        }

        // Extraer el token del encabezado
        String token = authorizationHeader.substring(7);

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();

            final SecurityContext currentSecurityContext = requestContext.getSecurityContext();

            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> username;
                }

                @Override
                public boolean isUserInRole(String role) {
                    return false;
                }

                @Override
                public boolean isSecure() {
                    return currentSecurityContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Bearer";
                }
            });

        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Token inválido\"}")
                    .build());
        }
    }
}