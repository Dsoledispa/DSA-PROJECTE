package edu.upc.dsa.modelTest;

import edu.upc.dsa.exceptions.categoriaObjeto.CatObjetoYaExisteException;
import edu.upc.dsa.manager.CatObjetoManager;
import edu.upc.dsa.manager.CatObjetoManagerImpl;
import edu.upc.dsa.models.CategoriaObjeto;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CatObjetoManagerTest {

    final static Logger logger = Logger.getLogger(CatObjetoManagerTest.class);
    CatObjetoManager com;

    @Before
    public void setUp() {
        this.com = new CatObjetoManagerImpl(); // Usa ICatObjetoDAOImpl por defecto
    }

    @Test
    public void testGetAllCatObjeto() {
        List<CategoriaObjeto> categorias = this.com.getAllCatObjeto();
        assertNotNull(categorias);
        assertTrue(categorias.size() >= 3); // ya hay 3 insertadas por SQL
    }

    @Test
    public void testGetCatObjeto() {
        CategoriaObjeto cat = this.com.getCatObjeto("1");
        assertNotNull(cat);
        assertEquals("ARMAS", cat.getNombre());
    }

    @Test
    public void testAddCatObjeto() {
        String id = "100";
        String nombre = "HECHIZOS";

        // Asegúrate de no tener ya esta categoría para evitar conflicto
        CategoriaObjeto existente = this.com.getCatObjeto(id);
        if (existente == null) {
            CategoriaObjeto nueva = this.com.addCatObjeto(id, nombre);
            assertNotNull(nueva);
            assertEquals(nombre, nueva.getNombre());
        }

        // No permitir añadir con nombre duplicado (case-insensitive)
        assertThrows(CatObjetoYaExisteException.class, () -> {
            this.com.addCatObjeto("otraID", "armas"); // ya existe como "ARMAS"
        });
        this.com.deleteCatObjeto(id);
        assertNull(this.com.getCatObjeto(id));
    }

    @Test
    public void testUpdateCatObjeto() {
        String id = "101";
        String nombreOriginal = "CONSUMIBLES";
        String nuevoNombre = "HECHIZOS";

        // Asegurarse de que existe
        CategoriaObjeto existente = this.com.getCatObjeto(id);
        if (existente == null) {
            this.com.addCatObjeto(id, nombreOriginal);
        }

        // Actualizar el nombre
        this.com.updateCatObjeto(id, nuevoNombre);

        CategoriaObjeto actualizada = this.com.getCatObjeto(id);
        assertNotNull(actualizada);
        assertEquals(nuevoNombre, actualizada.getNombre());

        this.com.deleteCatObjeto(id);
        assertNull(this.com.getCatObjeto(id));
    }

    @Test
    public void testDeleteCatObjeto() {
        // Añadimos temporalmente y luego eliminamos
        String id = "999";
        String nombre = "TEMPORAL";

        CategoriaObjeto nueva = this.com.addCatObjeto(id, nombre);
        assertNotNull(this.com.getCatObjeto(id));

        this.com.deleteCatObjeto(id);
        assertNull(this.com.getCatObjeto(id));
    }

    @Test
    public void testSizeCatObjeto() {
        int size = this.com.sizeCatObjeto();
        assertTrue(size >= 3); // por lo menos las 3 de base
    }
}