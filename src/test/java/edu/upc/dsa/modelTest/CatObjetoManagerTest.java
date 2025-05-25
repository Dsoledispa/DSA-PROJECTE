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
        this.com = new CatObjetoManagerImpl(); // Usa DAO que accede a base de datos
    }

    @Test
    public void testGetAllCatObjeto() {
        List<CategoriaObjeto> categorias = this.com.getAllCatObjeto();
        assertNotNull(categorias);
        assertTrue(categorias.size() >= 3); // ARMAS, ARMADURAS, POCIONES
    }

    @Test
    public void testGetCatObjeto() {
        CategoriaObjeto cat = this.com.getCatObjeto("1");
        assertNotNull(cat);
        assertEquals("ARMAS", cat.getNombre());
    }

    @Test
    public void testAddCatObjeto() {
        String id = "999";
        String nombre = "HECHIZOS";

        // Asegúrate de no tener ya esta categoría para evitar conflicto
        CategoriaObjeto existente = this.com.getCatObjeto(id);
        if (existente != null) {
            this.com.deleteCatObjeto(id); // limpiamos si quedó de otro test
        }

        CategoriaObjeto nueva = this.com.addCatObjeto(id, nombre);
        assertNotNull(nueva);
        assertEquals(nombre, nueva.getNombre());

        // Comprobar que no se puede insertar otra con el mismo nombre (case-insensitive)
        assertThrows(CatObjetoYaExisteException.class, () -> {
            this.com.addCatObjeto("otraID", "armas"); // "ARMAS" ya existe
        });

        // Limpieza
        this.com.deleteCatObjeto(id);
        assertNull(this.com.getCatObjeto(id));
    }

    @Test
    public void testUpdateCatObjeto() {
        String id = "998";
        String nombreOriginal = "TEMP";
        String nuevoNombre = "MODIFICADA";

        // Añadir o resetear si ya existe
        CategoriaObjeto cat = this.com.getCatObjeto(id);
        if (cat == null) {
            this.com.addCatObjeto(id, nombreOriginal);
        } else {
            this.com.updateCatObjeto(new CategoriaObjeto(id, nombreOriginal));
        }

        this.com.updateCatObjeto(new CategoriaObjeto(id, nuevoNombre));

        CategoriaObjeto actualizada = this.com.getCatObjeto(id);
        assertNotNull(actualizada);
        assertEquals(nuevoNombre, actualizada.getNombre());

        // Limpieza
        this.com.deleteCatObjeto(id);
        assertNull(this.com.getCatObjeto(id));
    }


    @Test
    public void testDeleteCatObjeto() {
        String id = "997";
        String nombre = "BORRAR";

        CategoriaObjeto nueva = this.com.addCatObjeto(id, nombre);
        assertNotNull(this.com.getCatObjeto(id));

        this.com.deleteCatObjeto(id);
        assertNull(this.com.getCatObjeto(id));
    }

    @Test
    public void testSizeCatObjeto() {
        int size = this.com.sizeCatObjeto();
        assertTrue(size >= 3); // Las 3 de base
    }
}
