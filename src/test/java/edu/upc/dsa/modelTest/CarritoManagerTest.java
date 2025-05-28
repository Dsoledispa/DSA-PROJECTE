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

public class CarritoManagerTest {

    final static Logger logger = Logger.getLogger(CarritoManagerTest.class);

    UsuarioManager um;
    PartidaManager pm;
    CarritoManager cm;
    TiendaManager tm;
    InventarioManager im;

    @Before
    public void setUp(){

        this.um = new UsuarioManagerImpl();
        this.pm = new PartidaManagerImpl();
        this.cm = new CarritoManagerImpl(pm);
        this.tm = new TiendaManagerImpl();
        this.im = new InventarioManagerImpl();
        
        // Crear usuario y partida con monedas suficientes
        this.um.addUsuario("carritoTest", "1234");
        pm.addPartida("1", "carritoTest", 3, 200, 0);
    }

    @After
    public void tearDown(){
        // Eliminamos all inventario de la partida
        im.eliminarAllObjetosDeInventario("1");

        // Borrar todas las partidas del usuario UsuarioTest
        List<Partida> partidas = pm.getPartidas("carritoTest");
        for (Partida p : partidas) {
            pm.deletePartida("carritoTest", p.getId_partida());
        }
        this.um.deleteUsuario("carritoTest");
    }

    @Test
    public void testCompraDesdeCarrito() {
        // Obtener objetos existentes por ID
        Objeto espada = tm.getProductoPorId("1"); // precio 30
        Objeto armadura = tm.getProductoPorId("2"); // precio 50

        // Hasta aqui llega el test sin petar
        // Agregar productos al carrito
        cm.agregarObjetoACarrito("1", espada.getId_objeto());
        cm.agregarObjetoACarrito("1", armadura.getId_objeto());

        // Total carrito
        cm.getTotalCarrito("1");

        // Eliminar objeto del carrito
        Objeto pocion = tm.getProductoPorId("3"); // precio 50
        cm.agregarObjetoACarrito("1", pocion.getId_objeto());
        cm.eliminarObjetoDeCarrito("1", pocion.getId_objeto());

        // Confirmar compra
        cm.realizarCompra("carritoTest","1");

        // Verificar inventario
        List<Objeto> objetos = im.getInventarioDePartida("1");
        logger.info("Carritosad : " +objetos);
        logger.info("Tamano inventario: "+objetos.size());
        assertEquals(2, objetos.size());

        // Verificar monedas restantes
        Partida partidaActualizada = pm.getPartida("carritoTest", "1");
        int esperado = 200 - espada.getPrecio() - armadura.getPrecio(); // 200 - 30 - 50 = 120
        assertEquals(esperado,(int) partidaActualizada.getMonedas());

        // Verificar que el carrito está vacío
        List<Objeto> carrito = cm.getObjetosDelCarrito("1");
        assertTrue(carrito.isEmpty());
    }

}
