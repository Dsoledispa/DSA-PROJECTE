package edu.upc.dsa.modelTest;

import edu.upc.dsa.manager.*;
import edu.upc.dsa.models.Objeto;
import edu.upc.dsa.models.Partida;
import edu.upc.dsa.models.Usuario;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class InventarioManagerTest {

    final static Logger logger = Logger.getLogger(InventarioManagerTest.class);

    UsuarioManager um;
    PartidaManager pm;
    InventarioManager im;
    TiendaManager tm;

    @Before
    public void setUp(){
        this.um = new UsuarioManagerImpl();
        this.pm = new PartidaManagerImpl();
        this.im = new InventarioManagerImpl();
        this.tm = new TiendaManagerImpl();

        this.um.addUsuario("555","inventarioTest", "1234");
        this.pm.addPartida("10", "555", 3, 100, 0);
    }

    @After
    public void tearDown(){
        List<Partida> partidas = pm.getPartidas("555");
        for (Partida p : partidas) {
            pm.deletePartida("555", p.getId_partida());
        }
        this.um.deleteUsuario("555");
    }

    @Test
    public void testInventarioBasico() {
        Objeto espada = this.tm.getProductoPorId("1"); // Espada

        // Añadir al inventario
        boolean añadido = im.agregarObjetoAInventario("10", espada.getId_objeto());
        assertTrue(añadido);

        // Obtener inventario
        List<Objeto> inventario = im.getInventarioDePartida("10");
        assertEquals(1, inventario.size());
        assertEquals("Espada", inventario.get(0).getNombre());

        // Eliminar objeto
        boolean eliminado = im.eliminarObjetoDeInventario("10", espada.getId_objeto());
        assertTrue(eliminado);

        // Verificar que se eliminó
        List<Objeto> inventarioDespues = im.getInventarioDePartida("10");
        assertTrue(inventarioDespues.isEmpty());

        // Intentar eliminar de nuevo (debe fallar)
        boolean eliminadoOtraVez = im.eliminarObjetoDeInventario("10", espada.getId_objeto());
        assertFalse(eliminadoOtraVez);
    }

    @Test
    public void testInventarioMultipleObjetos() {
        Objeto espada = this.tm.getProductoPorId("1");
        Objeto armadura = this.tm.getProductoPorId("2");
        Objeto pocion = this.tm.getProductoPorId("3");

        // Añadir varios objetos
        assertTrue(im.agregarObjetoAInventario("10", espada.getId_objeto()));
        assertTrue(im.agregarObjetoAInventario("10", armadura.getId_objeto()));
        assertTrue(im.agregarObjetoAInventario("10", pocion.getId_objeto()));

        List<Objeto> inventario = im.getInventarioDePartida("10");
        assertEquals(3, inventario.size());

        // Eliminar objetos
        im.eliminarObjetoDeInventario("10", espada.getId_objeto());
        im.eliminarObjetoDeInventario("10", armadura.getId_objeto());
        im.eliminarObjetoDeInventario("10", pocion.getId_objeto());
    }

    @Test
    public void testPagarYGuardarObjetoInventario() {
        Objeto objeto = tm.getProductoAleatorio();
        im.PagarYGuardarObjetoInventario("555", "10", objeto.getId_objeto());

        // Eliminar objeto
        boolean eliminado = im.eliminarObjetoDeInventario("10", objeto.getId_objeto());
        assertTrue(eliminado);

    }
}
