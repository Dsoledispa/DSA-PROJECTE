package edu.upc.dsa.DAOTest;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.db.orm.dao.CatObjetoDAO;
import edu.upc.dsa.db.orm.dao.CatObjetoDAOImpl;
import edu.upc.dsa.models.CategoriaObjeto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CatObjetoDAOImplTest {

    private CatObjetoDAO dao;
    private Session session;
    private final String TEST_ID = "test_categoria";  // ID único para tests

    @Before
    public void setUp() {
        dao = new CatObjetoDAOImpl();

        // Borramos si ya existe la categoría con TEST_ID, para dejar limpio antes de testear
        session = FactorySession.openSession();
        CategoriaObjeto existing = (CategoriaObjeto) session.get(CategoriaObjeto.class, TEST_ID);
        if (existing != null) {
            session.delete(existing);
        }
        session.close();
    }

    @After
    public void tearDown() {
        // Borramos la categoría creada en el test para no dejar residuos
        session = FactorySession.openSession();
        CategoriaObjeto existing = (CategoriaObjeto) session.get(CategoriaObjeto.class, TEST_ID);
        if (existing != null) {
            session.delete(existing);
        }
        session.close();
    }

    @Test
    public void testAddAndGetCategoriaObjeto() {
        int result = dao.addCategoriaObjeto(TEST_ID, "CategoriaTest");
        assertEquals(1, result);

        CategoriaObjeto categoria = dao.getCategoriaObjeto(TEST_ID);
        assertNotNull(categoria);
        assertEquals(TEST_ID, categoria.getId_categoria());
        assertEquals("CategoriaTest", categoria.getNombre());
    }

    @Test
    public void testUpdateCategoriaObjeto() {
        dao.addCategoriaObjeto(TEST_ID, "CategoriaTest");
        dao.updateCategoriaObjeto(TEST_ID, "CategoriaActualizada");

        CategoriaObjeto updated = dao.getCategoriaObjeto(TEST_ID);
        assertNotNull(updated);
        assertEquals("CategoriaActualizada", updated.getNombre());
    }

    @Test
    public void testDeleteCategoriaObjeto() {
        dao.addCategoriaObjeto(TEST_ID, "CategoriaTest");
        dao.deleteCategoriaObjeto(TEST_ID);

        CategoriaObjeto deleted = dao.getCategoriaObjeto(TEST_ID);
        assertNull(deleted);
    }

    @Test
    public void testGetCategoriasObjeto() {
        dao.addCategoriaObjeto(TEST_ID, "CategoriaTest");

        List<CategoriaObjeto> lista = dao.getCategoriasObjeto();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        boolean found = false;
        for (CategoriaObjeto c : lista) {
            if (TEST_ID.equals(c.getId_categoria()) && "CategoriaTest".equals(c.getNombre())) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
}