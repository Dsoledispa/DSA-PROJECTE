package edu.upc.dsa.modelTest;

import edu.upc.dsa.exceptions.usuario.PasswordNotMatchException;
import edu.upc.dsa.exceptions.usuario.UsuarioNotFoundException;
import edu.upc.dsa.exceptions.usuario.UsuarioYaExisteException;
import edu.upc.dsa.manager.UsuarioManager;
import edu.upc.dsa.manager.UsuarioManagerImpl;
import edu.upc.dsa.models.Usuario;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UsuarioManagerTest {

    final static Logger logger = Logger.getLogger(UsuarioManagerTest.class);
    UsuarioManager um;

    @Before
    public void setUp() {
        this.um = new UsuarioManagerImpl();
    }

    @Test
    public void idnullTest(){
        // String id = UUID.randomUUID().toString(); idea interesante
        this.um.addUsuario(null, "Rohan", "1234");
        Usuario usuario = this.um.getUsuarioPorNombre("Rohan");
        this.um.deleteUsuario(usuario.getId_usuario());

    }

    @Test
    public void addUsuarioTest(){
        List<Usuario> usuariosAntes = um.getAllUsuarios();
        int sizeAntes = usuariosAntes.size();

        try {
            this.um.addUsuario("555","John", "1234");
            List<Usuario> usuariosDespues = um.getAllUsuarios();
            assertEquals(sizeAntes + 1, usuariosDespues.size());

            // Intentar registrar un nombre repetido debe lanzar excepción
            assertThrows(UsuarioYaExisteException.class, () ->
                    this.um.addUsuario(null,"John", "otraClave")
            );
        } finally {
            // Limpieza: eliminar usuario creado para no dejar rastro
            try {
                this.um.deleteUsuario("555");
            } catch (UsuarioNotFoundException ignored) {}
        }
    }

    @Test
    public void getUsuarioTest(){
        try {
            try {
                this.um.getUsuario("Alonso");
            } catch (UsuarioNotFoundException e) {
                this.um.addUsuario("555","Alonso", "1234");
            }
            Usuario u = this.um.getUsuario("555");

            assertEquals("Alonso", u.getNombre());
            assertNotNull(u.getPassword());
            assertNotEquals("1234", u.getPassword());  // debe estar cifrada

            // Usuario que no existe debe lanzar excepción
            assertThrows(UsuarioNotFoundException.class, () ->
                    this.um.getUsuario("Inexistente")
            );
        } finally {
            try {
                this.um.deleteUsuario("555");
            } catch (UsuarioNotFoundException ignored) {}
        }
    }

    @Test
    public void loginUsuarioTest() {
        try {
            try {
                this.um.addUsuario("555","Alonso", "1234");
            } catch (UsuarioYaExisteException ignored) {}


            Usuario u = this.um.loginUsuario("Alonso", "1234");
            assertEquals("Alonso", u.getNombre());

            // Login con contraseña incorrecta
            assertThrows(PasswordNotMatchException.class, () ->
                    this.um.loginUsuario("Alonso", "claveIncorrecta")
            );

            // Login con usuario no existente
            assertThrows(UsuarioNotFoundException.class, () ->
                    this.um.loginUsuario("NoExiste", "1234")
            );

        } catch (Exception e) {
            fail("Excepción inesperada: " + e.getMessage());
        } finally {
            try {
                um.deleteUsuario("555");
            } catch (UsuarioNotFoundException ignored) {}
        }
    }

    @Test
    public void updateUsuarioTest(){

        try {
            // Crear usuario si no existe
            try {
                um.getUsuario("555");
            } catch (UsuarioNotFoundException e) {
                um.addUsuario("555","Luis", "pass1");
            }

            // Login con la contraseña antigua debe funcionar
            Usuario u = um.loginUsuario("Luis", "pass1");
            assertEquals("Luis", u.getNombre());

            // Actualizar contraseña
            Usuario actualizado = new Usuario("555","Luis", "pass2");
            um.updateUsuario(actualizado);

            // Login con antigua debe fallar
            assertThrows(PasswordNotMatchException.class, () -> {
                um.loginUsuario("Luis", "pass1");
            });

            // Login con nueva debe funcionar
            Usuario u2 = um.loginUsuario("Luis", "pass2");
            assertEquals("Luis", u2.getNombre());

        } finally {
            try {
                um.deleteUsuario("555");
            } catch (UsuarioNotFoundException ignored) {}
        }
    }

    @Test
    public void deleteUsuarioTest() throws UsuarioYaExisteException {
        String nombreUsu = "Carlos";
        String password = "deleteTest";

        try {
            um.addUsuario("555","Carlos", "deleteTest");

            // Confirmamos que el usuario fue creado
            Usuario u = um.getUsuario("555");
            assertEquals("Carlos", u.getNombre());

            // Eliminar usuario
            um.deleteUsuario("555");

            // Verificar que el usuario ya no existe
            assertThrows(UsuarioNotFoundException.class, () -> {
                um.getUsuario("555");
            });

        } catch (UsuarioNotFoundException e) {
            fail("El usuario debería existir antes de ser eliminado");
        }
    }
}
