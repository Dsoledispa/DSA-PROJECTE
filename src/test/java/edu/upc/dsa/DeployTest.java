package edu.upc.dsa;

import edu.upc.dsa.exceptions.usuario.UsuarioYaExisteException;
import edu.upc.dsa.manager.UsuarioManager;
import edu.upc.dsa.manager.UsuarioManagerImpl;
import edu.upc.dsa.models.Usuario;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DeployTest {

    final static Logger logger = Logger.getLogger(DeployTest.class);
    UsuarioManager um;

    @Before
    public void setUp() {
        this.um = new UsuarioManagerImpl(); // Asume que ya está conectado a la BBDD
    }

    @Test
    public void deployAddUsuarioTest() throws UsuarioYaExisteException {
        List<Usuario> usuariosAntes = um.getAllUsuarios();
        int sizeAntes = usuariosAntes.size();

        this.um.addUsuario("UsuarioDeploy", "claveSegura123");

        List<Usuario> usuariosDespues = um.getAllUsuarios();
        assertEquals(sizeAntes + 1, usuariosDespues.size());

        Usuario u = usuariosDespues.stream()
                .filter(user -> user.getNombreUsu().equals("UsuarioDeploy"))
                .findFirst()
                .orElse(null);

        assertNotNull("El usuario debería existir tras la inserción", u);
    }
}