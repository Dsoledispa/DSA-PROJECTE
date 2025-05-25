package edu.upc.dsa.modelTest;

import edu.upc.dsa.manager.TiendaManager;
import edu.upc.dsa.manager.TiendaManagerImpl;
import edu.upc.dsa.models.CategoriaObjeto;
import edu.upc.dsa.models.Objeto;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TiendaManagerTest {

    final static Logger logger = Logger.getLogger(TiendaManagerTest.class);
    TiendaManager tm;

    @Before
    public void setUp() {
        this.tm = new TiendaManagerImpl(); // Usa DAO con conexión a la base de datos
    }

    @Test
    public void testGetAllProductos() {
        List<Objeto> productos = this.tm.getAllProductos();
        assertNotNull(productos);
        assertTrue(productos.size() >= 3); // Espada, Armadura, Poción
    }

    @Test
    public void testGetProductosPorCategoria() {
        // ID "1" es ARMAS, según el SQL de datos iniciales
        String idCategoriaArmas = "1";

        List<Objeto> armas = this.tm.getProductosPorCategoria(idCategoriaArmas);
        assertNotNull(armas);
        assertFalse(armas.isEmpty());

        for (Objeto o : armas) {
            assertEquals(idCategoriaArmas, o.getCategoria().getId_categoria());
        }
    }

    @Test
    public void testGetProductoPorId() {
        Objeto espada = this.tm.getProductoPorId("1"); // Espada tiene ID "1"
        assertNotNull(espada);
        assertEquals("Espada", espada.getNombre());
    }

    @Test
    public void testAddYDeleteProducto() {
        Objeto nuevo = new Objeto("999", "Daga", 15, "/img/daga.png", "Una daga afilada", new CategoriaObjeto("1", "ARMAS"));

        Objeto añadido = this.tm.addProducto(nuevo);
        assertNotNull(añadido);
        assertEquals("Daga", añadido.getNombre());

        Objeto recuperado = this.tm.getProductoPorId("999");
        assertNotNull(recuperado);

        this.tm.deleteProducto("999");
        assertNull(this.tm.getProductoPorId("999"));
    }

    @Test
    public void testUpdateProducto() {
        // Creamos producto temporal para modificar
        Objeto pocion2 = new Objeto("998", "Poción2", 25, "/img/pocion2.png", "Otra poción", new CategoriaObjeto("3", "POCIONES"));
        this.tm.addProducto(pocion2);

        Objeto actualizada = new Objeto("998", "Poción Mejorada", 30, "/img/pocion2.png", "Poción mejorada", new CategoriaObjeto("3", "POCIONES"));
        this.tm.updateProducto(actualizada);

        Objeto result = this.tm.getProductoPorId("998");
        assertEquals("Poción Mejorada", result.getNombre());
        assertEquals(30, result.getPrecio());

        this.tm.deleteProducto("998");
    }

    @Test
    public void getProductoAleatorioTest() {

        // Llamamos al método a testear
        Objeto aleatorio = tm.getProductoAleatorio();

        // Verificamos que no sea null
        assertNotNull(aleatorio);
    }

    @Test
    public void testSizeProductos() {
        int size = this.tm.sizeProductos();
        assertTrue(size >= 3); // Las iniciales
    }
}


