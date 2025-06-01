package edu.upc.dsa.modelTest;

import edu.upc.dsa.exceptions.denuncia.DenunciaNotFoundException;
import edu.upc.dsa.exceptions.denuncia.DenunciaYaExisteException;
import edu.upc.dsa.manager.DenunciaManager;
import edu.upc.dsa.manager.DenunciaManagerImpl;
import edu.upc.dsa.manager.UsuarioManager;
import edu.upc.dsa.manager.UsuarioManagerImpl;
import edu.upc.dsa.models.Denuncia;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DenunciaManagerTest {

    final static Logger logger = Logger.getLogger(DenunciaManagerTest.class);
    UsuarioManager um;
    DenunciaManager dm;

    @Before
    public void setUp() {
        this.um = new UsuarioManagerImpl();
        this.dm = new DenunciaManagerImpl();

        this.um.addUsuario("u-denuncia", "UsuarioDenuncia", "pass123");
        dm.addDenuncia("d1", "Título denuncia", "Mensaje de denuncia", "u-denuncia");
    }

    @After
    public void tearDown() {
        List<Denuncia> denuncias = dm.getDenunciasByUsuario("u-denuncia");
        for (Denuncia d : denuncias) {
            dm.deleteDenuncia(d.getId_denuncia());
        }
        this.um.deleteUsuario("u-denuncia");
    }

    @Test
    public void addAndGetDenunciaTest() {
        assertEquals(1, dm.getDenunciasByUsuario("u-denuncia").size());

        dm.addDenuncia("d2", "Otra denuncia", "Otro mensaje", "u-denuncia");
        assertEquals(2, dm.getDenunciasByUsuario("u-denuncia").size());

        Denuncia d = dm.getDenuncia("d2");
        assertEquals("u-denuncia", d.getId_usuario());
        assertEquals("Otra denuncia", d.getTitulo());
    }

    @Test
    public void updateDenunciaTest() {
        Denuncia d = dm.getDenuncia("d1");
        d.setMensaje("Mensaje actualizado");
        dm.updateDenuncia(d);

        Denuncia updated = dm.getDenuncia("d1");
        assertEquals("Mensaje actualizado", updated.getMensaje());
    }

    @Test
    public void deleteDenunciaTest() {
        dm.addDenuncia("d3", "Borrar", "Se eliminará", "u-denuncia");
        assertEquals(2, dm.getDenunciasByUsuario("u-denuncia").size());

        dm.deleteDenuncia("d3");
        assertEquals(1, dm.getDenunciasByUsuario("u-denuncia").size());
    }

    @Test
    public void getDenunciasTest() {
        dm.addDenuncia("d4", "Denuncia A", "Contenido A", "u-denuncia");
        dm.addDenuncia("d5", "Denuncia B", "Contenido B", "u-denuncia");

        List<Denuncia> todas = dm.getDenuncias();
        List<Denuncia> delUsuario = dm.getDenunciasByUsuario("u-denuncia");

        assertTrue(todas.size() >= 3);  // Puede haber otras denuncias de otros tests
        assertEquals(3, delUsuario.size());
    }

    @Test
    public void denunciaYaExisteExceptionTest() {
        assertThrows(DenunciaYaExisteException.class, () -> {
            dm.addDenuncia("d1", "Título duplicado", "Mensaje duplicado", "u-denuncia");
        });
    }

    @Test
    public void denunciaNotFoundExceptionTest() {
        assertThrows(DenunciaNotFoundException.class, () -> {
            dm.getDenuncia("no-existe");
        });

        assertThrows(DenunciaNotFoundException.class, () -> {
            Denuncia d = new Denuncia("no-existe", null, "?", "?", "u-denuncia");
            dm.updateDenuncia(d);
        });

        assertThrows(DenunciaNotFoundException.class, () -> {
            dm.deleteDenuncia("no-existe");
        });
    }
}
