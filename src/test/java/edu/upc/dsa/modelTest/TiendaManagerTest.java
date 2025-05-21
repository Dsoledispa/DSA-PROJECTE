//package edu.upc.dsa.modelTest;
//
//import edu.upc.dsa.manager.TiendaManager;
//import edu.upc.dsa.manager.TiendaManagerImpl;
//import edu.upc.dsa.models.Objeto;
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
//public class TiendaManagerTest {
//
//    final static Logger logger = Logger.getLogger(TiendaManagerTest.class);
//    TiendaManager tm;
//
//    @Before
//    public void setUp() {
//        this.tm = TiendaManagerImpl.getInstance();
//        this.tm.clear();
//
//        IniciarDatosTests.initProductos(this.tm);
//    }
//
//    @After
//    public void tearDown() {
//        this.tm.clear();
//    }
//
//    @Test
//    public void testGetAllProductos() {
//        List<Objeto> productos = this.tm.getAllProductos();
//        assertEquals(3, productos.size());
//    }
//
//    @Test
//    public void testGetProductoPorCategoria() {
//        List<Objeto> armas = this.tm.getProductosPorCategoria(CategoriaObjeto.ARMAS);
//        assertEquals(1, armas.size());
//        assertEquals("Espada", armas.get(0).getNombre());
//    }
//
//    @Test
//    public void testGetProductoPorId() {
//        Objeto o = this.tm.getProductoPorId("2");
//        assertNotNull(o);
//        assertEquals("Armadura", o.getNombre());
//    }
//
//    @Test
//    public void testDeleteProducto() {
//        this.tm.deleteProducto("3");
//        assertNull(this.tm.getProductoPorId("3"));
//        assertEquals(2, this.tm.getAllProductos().size());
//    }
//}
