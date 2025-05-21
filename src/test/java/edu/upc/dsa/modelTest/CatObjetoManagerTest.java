package edu.upc.dsa.modelTest;

import edu.upc.dsa.exceptions.categoriaObjeto.CatObjetoYaExisteException;
import edu.upc.dsa.manager.CatObjetoManager;
import edu.upc.dsa.manager.CatObjetoManagerImpl;
import edu.upc.dsa.models.CategoriaObjeto;
import edu.upc.dsa.util.IniciarDatosTests;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;


public class CatObjetoManagerTest {

    final static Logger logger = Logger.getLogger(CatObjetoManagerTest.class);
    CatObjetoManager com;

    @Before
    public void setUp() {
        this.com = CatObjetoManagerImpl.getInstance();
        this.com.clear();

        // Carga inicial de categorías
        IniciarDatosTests.initCategoriaObjetos(this.com);
    }

    @After
    public void tearDown() {
        this.com.clear();
    }

    @Test
    public void testGetAllCatObjeto() {
        List<CategoriaObjeto> categorias = this.com.getAllCatObjeto();
        assertEquals(3, categorias.size());
    }

    @Test
    public void testGetCatObjeto() {
        CategoriaObjeto cat = this.com.getCatObjeto(2);
        assertNotNull(cat);
        assertEquals("Armaduras", cat.getNombre());
    }

    @Test
    public void testAddCatObjeto() {
        this.com.addCatObjeto(4, "Comida");
        CategoriaObjeto cat = this.com.getCatObjeto(4);
        assertNotNull(cat);
        assertEquals("Comida", cat.getNombre());
        assertEquals(4, this.com.sizeCatObjeto());
        assertThrows(CatObjetoYaExisteException.class, () -> {
            this.com.addCatObjeto(10, "armaduras"); // "Armaduras" ya existe (case-insensitive)
        });
    }

    @Test
    public void testDeleteCatObjeto() {
        this.com.deleteCatObjeto(3);
        CategoriaObjeto cat = this.com.getCatObjeto(3);
        assertNull(cat);
        assertEquals(2, this.com.sizeCatObjeto());
    }

    @Test
    public void testSizeCatObjeto() {
        assertEquals(3, this.com.sizeCatObjeto());
    }

}