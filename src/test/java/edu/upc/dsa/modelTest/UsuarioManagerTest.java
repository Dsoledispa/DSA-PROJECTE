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
    public void addUsuarioTest() throws UsuarioYaExisteException {
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
    public void getUsuarioTest() throws UsuarioNotFoundException, UsuarioYaExisteException {
        try {
            try {
                this.um.getUsuario("Paco");
            } catch (UsuarioNotFoundException e) {
                this.um.addUsuario("Paco", "1234");
            }
            Usuario u = this.um.getUsuario("Paco");

            assertEquals("Paco", u.getNombreUsu());
            assertNotNull(u.getPassword());
            assertNotEquals("1234", u.getPassword());  // debe estar cifrada

            // Usuario que no existe debe lanzar excepción
            assertThrows(UsuarioNotFoundException.class, () ->
                    this.um.getUsuario("Inexistente")
            );
        } finally {
            try {
                this.um.deleteUsuario("Paco");
            } catch (UsuarioNotFoundException ignored) {}
        }
    }

    @Test
    public void loginUsuarioTest() throws UsuarioYaExisteException, PasswordNotMatchException, UsuarioNotFoundException {
        try {
            try {
                this.um.addUsuario("Paco", "1234");
            } catch (UsuarioYaExisteException ignored) {}

            Usuario u = this.um.loginUsuario("Paco", "1234");
            assertEquals("Paco", u.getNombreUsu());

            // Login con contraseña incorrecta
            assertThrows(PasswordNotMatchException.class, () ->
                    this.um.loginUsuario("Paco", "claveIncorrecta")
            );

            // Login con usuario no existente
            assertThrows(UsuarioNotFoundException.class, () ->
                    this.um.loginUsuario("NoExiste", "1234")
            );
        } finally {
            try {
                this.um.deleteUsuario("Paco");
            } catch (UsuarioNotFoundException ignored) {}
        }
    }
}
