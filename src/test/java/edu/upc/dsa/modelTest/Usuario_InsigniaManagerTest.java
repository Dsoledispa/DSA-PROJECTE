package edu.upc.dsa.modelTest;

import edu.upc.dsa.manager.*;
import edu.upc.dsa.models.Usuario_Insignia;
import edu.upc.dsa.models.Usuario;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class Usuario_InsigniaManagerTest {

    final static Logger logger = Logger.getLogger(Usuario_InsigniaManagerTest.class);

    UsuarioManager um;
    Usuario_InsigniaManager uim;

    @Before
    public void setUp() {
        this.um = new UsuarioManagerImpl();
        this.uim = new Usuario_InsigniaManagerImpl();

        // Crear usuario de prueba
        um.addUsuario("888", "testInsignias", "1234");
    }

    @After
    public void tearDown() {
        // Eliminar todas las insignias del usuario
        uim.eliminarTodasInsigniasDeUsuario("888");

        // Eliminar el usuario
        um.deleteUsuario("888");
    }

    @Test
    public void testAsignarYObtenerInsignias() {
        // Asignar insignias
        assertTrue(uim.asignarInsigniaAUsuario("888", "1")); // Policia
        assertTrue(uim.asignarInsigniaAUsuario("888", "2")); // Tornillo

        // Obtener insignias
        List<Usuario_Insignia> insignias = uim.getInsigniasDeUsuario("888");
        assertEquals(2, insignias.size());

        // Verificar contenido
        boolean tienePolicia = insignias.stream().anyMatch(i -> i.getId_insignia().equals("1"));
        boolean tieneTornillo = insignias.stream().anyMatch(i -> i.getId_insignia().equals("2"));

        assertTrue(tienePolicia);
        assertTrue(tieneTornillo);
    }

    @Test
    public void testEliminarInsignia() {
        // Asignar una insignia
        assertTrue(uim.asignarInsigniaAUsuario("888", "3")); // Valor

        // Verificar que se asignó
        List<Usuario_Insignia> insignias = uim.getInsigniasDeUsuario("888");
        assertEquals(1, insignias.size());

        // Eliminar la insignia
        assertTrue(uim.eliminarInsigniaDeUsuario("888", "3"));

        // Verificar que se eliminó
        List<Usuario_Insignia> despues = uim.getInsigniasDeUsuario("888");
        assertTrue(despues.isEmpty());
    }

    @Test
    public void testEliminarTodasInsignias() {
        uim.asignarInsigniaAUsuario("888", "1");
        uim.asignarInsigniaAUsuario("888", "2");

        List<Usuario_Insignia> antes = uim.getInsigniasDeUsuario("888");
        assertEquals(2, antes.size());

        uim.eliminarTodasInsigniasDeUsuario("888");

        List<Usuario_Insignia> despues = uim.getInsigniasDeUsuario("888");
        assertTrue(despues.isEmpty());
    }

    @Test
    public void testNoDuplicarInsignias() {
        // Intentar asignar la misma insignia dos veces
        assertTrue(uim.asignarInsigniaAUsuario("888", "1"));
        boolean resultadoDuplicado = uim.asignarInsigniaAUsuario("888", "1");

        // Debe fallar (por la restricción UNIQUE)
        assertFalse(resultadoDuplicado);

        // Verificar que solo hay una
        List<Usuario_Insignia> insignias = uim.getInsigniasDeUsuario("888");
        assertEquals(1, insignias.size());
    }
}
