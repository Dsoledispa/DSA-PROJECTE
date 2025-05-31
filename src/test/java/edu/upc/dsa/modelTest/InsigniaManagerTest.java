package edu.upc.dsa.modelTest;

import edu.upc.dsa.manager.InsigniaManager;
import edu.upc.dsa.manager.InsigniaManagerImpl;
import edu.upc.dsa.models.Insignia;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class InsigniaManagerTest {

    final static Logger logger = Logger.getLogger(InsigniaManagerTest.class);
    InsigniaManager im;

    @Before
    public void setUp() {
        this.im = new InsigniaManagerImpl();
    }

    @Test
    public void testGetAllInsignias() {
        List<Insignia> insignias = this.im.getAllInsignias();
        assertNotNull(insignias);
        assertTrue(insignias.size() >= 3); // Policia, Tornillo, Valor
    }

    @Test
    public void testGetInsignia() {
        Insignia ins = this.im.getInsignia("1"); // Policia
        assertNotNull(ins);
        assertEquals("Policia", ins.getNombre());
    }

    @Test
    public void testAddInsignia() {
        String id = "999";
        String nombre = "TestInsignia";
        String avatar = "/img/test.png";

        // Asegúrate de no tener ya esta insignia
        Insignia existente = this.im.getInsignia(id);
        if (existente != null) {
            this.im.deleteInsignia(id);
        }

        Insignia nueva = this.im.addInsignia(id, nombre, avatar);
        assertNotNull(nueva);
        assertEquals(nombre, nueva.getNombre());
        assertEquals(avatar, nueva.getAvatar());

        // Limpieza
        this.im.deleteInsignia(id);
        assertNull(this.im.getInsignia(id));
    }

    @Test
    public void testUpdateInsignia() {
        String id = "998";
        String nombreOriginal = "TempInsignia";
        String nuevoNombre = "InsigniaModificada";
        String avatar = "/img/temp.png";

        // Añadir si no existe
        Insignia ins = this.im.getInsignia(id);
        if (ins == null) {
            this.im.addInsignia(id, nombreOriginal, avatar);
        } else {
            ins.setNombre(nombreOriginal);
            this.im.updateInsignia(ins);
        }

        Insignia modificada = new Insignia(id, nuevoNombre, avatar);
        this.im.updateInsignia(modificada);

        Insignia actualizada = this.im.getInsignia(id);
        assertNotNull(actualizada);
        assertEquals(nuevoNombre, actualizada.getNombre());

        // Limpieza
        this.im.deleteInsignia(id);
    }

    @Test
    public void testDeleteInsignia() {
        String id = "997";
        Insignia ins = new Insignia(id, "Eliminar", "/img/delete.png");

        this.im.addInsignia(ins);
        assertNotNull(this.im.getInsignia(id));

        this.im.deleteInsignia(id);
        assertNull(this.im.getInsignia(id));
    }

    @Test
    public void testSizeInsignias() {
        int size = this.im.sizeInsignias();
        assertTrue(size >= 3); // Al menos las 3 iniciales
    }
}
