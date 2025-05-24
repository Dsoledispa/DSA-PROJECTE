package edu.upc.dsa.modelTest;

import edu.upc.dsa.exceptions.usuario.PasswordNotMatchException;
import edu.upc.dsa.exceptions.usuario.UsuarioNotFoundException;
import edu.upc.dsa.exceptions.usuario.UsuarioYaExisteException;
import edu.upc.dsa.manager.UsuarioManager;
import edu.upc.dsa.manager.UsuarioManagerImpl;
import edu.upc.dsa.models.Usuario;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UsuarioManagerTest {

    final static Logger logger = Logger.getLogger(UsuarioManagerTest.class);
    UsuarioManager um;

    @Before
    public void setUp() {
        this.um = new UsuarioManagerImpl(); // No singleton, usa DAO
    }

    @Test
    public void addUsuarioTest(){
        List<Usuario> usuariosAntes = um.getAllUsuarios();
        int sizeAntes = usuariosAntes.size();

        try {
            this.um.addUsuario("John", "1234");
            List<Usuario> usuariosDespues = um.getAllUsuarios();
            assertEquals(sizeAntes + 1, usuariosDespues.size());

            // Intentar registrar un nombre repetido debe lanzar excepción
            assertThrows(UsuarioYaExisteException.class, () ->
                    this.um.addUsuario("John", "otraClave")
            );
        } finally {
            // Limpieza: eliminar usuario creado para no dejar rastro
            try {
                this.um.deleteUsuario("John");
            } catch (UsuarioNotFoundException ignored) {}
        }
    }

    @Test
    public void getUsuarioTest(){
        try {
            try {
                this.um.getUsuario("Alonso");
            } catch (UsuarioNotFoundException e) {
                this.um.addUsuario("Alonso", "1234");
            }
            Usuario u = this.um.getUsuario("Alonso");

            assertEquals("Alonso", u.getNombreUsu());
            assertNotNull(u.getPassword());
            assertNotEquals("1234", u.getPassword());  // debe estar cifrada

            // Usuario que no existe debe lanzar excepción
            assertThrows(UsuarioNotFoundException.class, () ->
                    this.um.getUsuario("Inexistente")
            );
        } finally {
            try {
                this.um.deleteUsuario("Alonso");
            } catch (UsuarioNotFoundException ignored) {}
        }
    }

    @Test
    public void loginUsuarioTest() {
        try {
            try {
                this.um.addUsuario("Alonso", "1234");
            } catch (UsuarioYaExisteException ignored) {}


            Usuario u = this.um.loginUsuario("Alonso", "1234");
            assertEquals("Alonso", u.getNombreUsu());

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
        }
    }

    @Test
    public void updateUsuarioTest(){
        String nombreUsu = "Luis";
        String oldPass = "pass1";
        String newPass = "pass2";

        try {
            // Crear usuario si no existe
            try {
                um.getUsuario(nombreUsu);
            } catch (UsuarioNotFoundException e) {
                um.addUsuario(nombreUsu, oldPass);
            }

            // Login con la contraseña antigua debe funcionar
            Usuario u = um.loginUsuario(nombreUsu, oldPass);
            assertEquals(nombreUsu, u.getNombreUsu());

            // Actualizar contraseña
            um.updateUsuario(nombreUsu, newPass);

            // Login con antigua debe fallar
            assertThrows(PasswordNotMatchException.class, () -> {
                um.loginUsuario(nombreUsu, oldPass);
            });

            // Login con nueva debe funcionar
            Usuario u2 = um.loginUsuario(nombreUsu, newPass);
            assertEquals(nombreUsu, u2.getNombreUsu());

        } finally {
            try {
                um.deleteUsuario(nombreUsu);
            } catch (UsuarioNotFoundException ignored) {}
        }
    }

    @Test
    public void deleteUsuarioTest() throws UsuarioYaExisteException {
        String nombreUsu = "Carlos";
        String password = "deleteTest";

        try {
            um.addUsuario(nombreUsu, password);

            // Confirmamos que el usuario fue creado
            Usuario u = um.getUsuario(nombreUsu);
            assertEquals(nombreUsu, u.getNombreUsu());

            // Eliminar usuario
            um.deleteUsuario(nombreUsu);

            // Verificar que el usuario ya no existe
            assertThrows(UsuarioNotFoundException.class, () -> {
                um.getUsuario(nombreUsu);
            });

        } catch (UsuarioNotFoundException e) {
            fail("El usuario debería existir antes de ser eliminado");
        }
    }
}
