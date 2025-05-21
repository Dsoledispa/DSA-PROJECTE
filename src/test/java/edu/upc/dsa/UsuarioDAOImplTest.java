package edu.upc.dsa;

import edu.upc.dsa.db.orm.dao.IUsuarioDAO;
import edu.upc.dsa.db.orm.dao.UsuarioDAOImpl;
import edu.upc.dsa.models.Usuario;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UsuarioDAOImplTest {

    final static Logger logger = Logger.getLogger(UsuarioDAOImplTest.class);
    private static IUsuarioDAO Udao;

    @Before
    public void setUp() {
        Udao = new UsuarioDAOImpl();
        // Limpiar por si hay datos de tests anteriores
        //Udao.deleteUsuario("usuarioTest1");
        //Udao.deleteUsuario("usuarioTest2");
    }

    @After
    public void tearDown() {
        //Udao.deleteUsuario("usuarioTest1");
        //Udao.deleteUsuario("usuarioTest2");
    }

    @Test
    public void testAddUsuario() {
        int result = Udao.addUsuario("usuarioTest1", "password1");
        assertEquals(1, result);
    }

    //@Test
    //public void testGetUsuarioExistente() {
    //    Udao.addUsuario("usuarioTest1", "password1");
    //    Usuario u = Udao.getUsuario("usuarioTest1");
    //    assertNotNull(u);
    //    assertEquals("usuarioTest1", u.getNombreUsu());
    //}
//
    //@Test
    //public void testGetUsuarioNoExistente() {
    //    Usuario u = Udao.getUsuario("noExiste");
    //    assertNull(u);
    //}
//
    //@Test
    //public void testGetUsuarios() {
    //    Udao.addUsuario("usuarioTest2", "password2");
    //    List<Usuario> usuarios = Udao.getUsuarios();
    //    assertTrue(usuarios.size() >= 1);
    //}
}
