//package edu.upc.dsa.modelTest;
//
//import edu.upc.dsa.manager.*;
//import edu.upc.dsa.models.Objeto;
//import edu.upc.dsa.models.Partida;
//import edu.upc.dsa.util.IniciarDatosTests;
//import org.apache.log4j.Logger;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class CarritoManagerTest {
//
//    final static Logger logger = Logger.getLogger(CarritoManagerTest.class);
//    UsuarioManager um;
//    TiendaManager tm;
//    PartidaManager pm;
//    CarritoManager cm;
//
//    @Before
//    public void setUp() {
//
//        this.um = UsuarioManagerImpl.getInstance();
//        this.tm = TiendaManagerImpl.getInstance();
//        this.pm = PartidaManagerImpl.getInstance();
//        this.cm = CarritoManagerImpl.getInstance();
//
//        this.um.clear();
//        this.tm.clear();
//        this.pm.clear();
//        this.cm.clear();
//
//        IniciarDatosTests.initUsuarios(this.um);
//        IniciarDatosTests.initProductos(this.tm);
//        IniciarDatosTests.initPartidas(this.pm);
//
//        this.cm.agregarProductoAlCarrito("Diego", tm.getProductoPorId("1"));
//        this.cm.agregarProductoAlCarrito("Diego", tm.getProductoPorId("2"));
//    }
//
//    @After
//    public void tearDown() {
//        this.um.clear();
//        this.tm.clear();
//        this.pm.clear();
//        this.cm.clear();
//    }
//
//    @Test
//    public void testGetProductosDelCarrito() {
//        List<Objeto> objetos = this.cm.getProductosDelCarrito("Diego");
//        assertEquals(2, objetos.size());
//        assertEquals("1", objetos.get(0).getId_objeto());
//        assertEquals("2", objetos.get(1).getId_objeto());
//    }
//
//    @Test
//    public void testEliminarProductoDelCarrito() {
//        this.cm.eliminarProductoDelCarrito("Diego", "1");
//        List<Objeto> objetos = this.cm.getProductosDelCarrito("Diego");
//        logger.info("A ver objetos:" +objetos);
//        assertEquals(1, objetos.size());
//        assertEquals("2", objetos.get(0).getId_objeto());
//        this.cm.eliminarProductoDelCarrito("Diego", "2");
//        List<Objeto> objeto2 = this.cm.getProductosDelCarrito("Diego");
//        logger.info("A ver objetos parte2:" +objeto2);
//        assertEquals(0, objeto2.size());
//    }
//
//    @Test
//    public void testRealizarCompraExitosa() {
//        boolean result = this.cm.realizarCompra("Diego", "1");
//        assertTrue(result);
//        Partida p = this.pm.getPartidas("Diego").get(0);
//        assertEquals(Integer.valueOf(20), p.getMonedas());// 100 -50 -30
//        assertEquals(2, p.getInventario().size());
//    }
//
//    @Test
//    public void testRealizarCompraFallidaPorDinero() {
//        this.cm.agregarProductoAlCarrito("Diego", tm.getProductoPorId("2"));
//        boolean result = this.cm.realizarCompra("Diego", "1");
//        assertFalse(result);
//    }
//
//}
