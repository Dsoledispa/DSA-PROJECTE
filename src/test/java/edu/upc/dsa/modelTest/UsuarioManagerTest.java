package edu.upc.dsa.modelTest;

import edu.upc.dsa.exceptions.usuario.PasswordNotMatchException;
import edu.upc.dsa.exceptions.usuario.UsuarioNotFoundException;
import edu.upc.dsa.exceptions.usuario.UsuarioYaExisteException;
import edu.upc.dsa.manager.UsuarioManager;
import edu.upc.dsa.manager.UsuarioManagerImpl;
import edu.upc.dsa.models.Usuario;
import edu.upc.dsa.util.IniciarDatosTests;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UsuarioManagerTest {

    final static Logger logger = Logger.getLogger(UsuarioManagerTest.class);
    UsuarioManager um;

    @Before
    public void setUp() {
        this.um = UsuarioManagerImpl.getInstance();
        this.um.clear(); // Nos aseguramos que no haya datos anteriores
        IniciarDatosTests.initUsuarios(this.um);
    }

    @After
    public void tearDown() {
        this.um.clear();
    }

    @Test
    public void addUsuarioTest() throws Exception {
        assertEquals(4, um.sizeUsuarios());
        this.um.addUsuario("John", "1234");
        assertEquals(5, um.sizeUsuarios());
        // Intentar registrar un nombre repetido debe lanzar excepción
        assertThrows(UsuarioYaExisteException.class, () ->
                this.um.addUsuario("John", "otraClave"));
    }

    @Test
    public void getUsuarioTest() throws Exception {
        Usuario u = this.um.getUsuario("Paco");
        assertEquals("Paco", u.getNombreUsu());
        // No verificamos la contraseña directamente porque ahora se almacena cifrada
        assertNotNull(u.getPassword());
        assertNotEquals("1234", u.getPassword());  // debe estar cifrada

        assertThrows(UsuarioNotFoundException.class, () ->
                this.um.getUsuario("Inexistente"));
    }

    @Test
    public void loginUsuarioTest() throws Exception {
        Usuario u = this.um.loginUsuario("Paco", "1234");
        assertEquals("Paco", u.getNombreUsu());

        // Login con contraseña incorrecta
        assertThrows(PasswordNotMatchException.class, () ->
                this.um.loginUsuario("Paco", "claveIncorrecta"));
        // Login con usuario no existente
        assertThrows(UsuarioNotFoundException.class, () ->
                this.um.loginUsuario("NoExiste", "1234"));
    }

}
