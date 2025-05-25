package edu.upc.dsa.modelTest;

import edu.upc.dsa.exceptions.partida.PartidaNotFoundException;
import edu.upc.dsa.exceptions.partida.PartidaYaExisteException;
import edu.upc.dsa.manager.PartidaManager;
import edu.upc.dsa.manager.PartidaManagerImpl;
import edu.upc.dsa.models.Partida;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PartidaManagerTest {

    final static Logger logger = Logger.getLogger(PartidaManagerTest.class);
    PartidaManager pm;

    @Before
    public void setUp() {
        this.pm = new PartidaManagerImpl();

        // Crear una partida inicial para el usuario Diego
        pm.addPartida("1", "Diego", 3, 100, 0);
    }

    @After
    public void tearDown() {
        // Borrar todas las partidas del usuario Diego
        List<Partida> partidas = pm.getPartidas("Diego");
        for (Partida p : partidas) {
            pm.deletePartida("Diego", p.getId_partida());
        }
    }

    @Test
    public void addAndGetPartidaTest() {
        // Verificar cantidad inicial
        assertEquals(1, pm.sizePartidas("Diego"));

        // Agregar nueva partida
        pm.addPartida("2", "Diego", 5, 200, 50);
        assertEquals(2, pm.sizePartidas("Diego"));

        // Obtener partida específica
        Partida partida = pm.getPartida("Diego", "2");
        assertEquals("Diego", partida.getId_usuario());
        assertEquals(200, partida.getMonedas().intValue());
    }

    @Test
    public void addPartidaConSoloUsuarioTest() {
        Partida nueva = pm.addPartida("Diego");
        assertNotNull(nueva.getId_partida());
        assertEquals("Diego", nueva.getId_usuario());
    }

    @Test
    public void getMonedasDePartidaTest() {
        int monedas = pm.getMonedasDePartida("Diego", "1");
        assertEquals(100, monedas);
    }

    @Test
    public void updatePartidaTest() {
        Partida partida = pm.getPartida("Diego", "1");
        partida.setMonedas(999);
        pm.updatePartida(partida);

        Partida actualizada = pm.getPartida("Diego", "1");
        assertEquals(999, actualizada.getMonedas().intValue());
    }

    @Test
    public void deletePartidaTest() {
        pm.addPartida("3", "Diego", 3, 50, 0);
        assertEquals(2, pm.sizePartidas("Diego"));

        pm.deletePartida("Diego", "3");
        assertEquals(1, pm.sizePartidas("Diego"));
    }

    @Test
    public void getPartidasTest() {
        pm.addPartida("4", "Diego", 3, 80, 10);
        pm.addPartida("5", "Diego", 2, 30, 20);

        List<Partida> partidas = pm.getPartidas("Diego");
        assertEquals(3, partidas.size());
    }

    @Test
    public void partidaYaExisteExceptionTest() {
        assertThrows(PartidaYaExisteException.class, () -> {
            pm.addPartida("1", "Diego", 3, 100, 0); // misma ID que la inicial
        });
    }

    @Test
    public void partidaNotFoundExceptionTest() {
        assertThrows(PartidaNotFoundException.class, () -> {
            pm.getPartida("Diego", "999"); // no existe
        });
    }

}

