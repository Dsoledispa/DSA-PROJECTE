package edu.upc.dsa.modelTest;

import edu.upc.dsa.exceptions.partida.PartidaNotFoundException;
import edu.upc.dsa.exceptions.partida.PartidaYaExisteException;
import edu.upc.dsa.manager.PartidaManager;
import edu.upc.dsa.manager.PartidaManagerImpl;
import edu.upc.dsa.manager.UsuarioManager;
import edu.upc.dsa.manager.UsuarioManagerImpl;
import edu.upc.dsa.models.Partida;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PartidaManagerTest {

    final static Logger logger = Logger.getLogger(PartidaManagerTest.class);
    UsuarioManager um;
    PartidaManager pm;

    @Before
    public void setUp() {
        this.um = new UsuarioManagerImpl();
        this.pm = new PartidaManagerImpl();

        this.um.addUsuario("555","UsuarioTest", "1234");
        // Crear una partida inicial para el usuario UsuarioTest
        pm.addPartida("1", "555", 3, 100, 0);
    }

    @After
    public void tearDown() {
        // Borrar todas las partidas del usuario UsuarioTest
        List<Partida> partidas = pm.getPartidas("555");
        for (Partida p : partidas) {
            pm.deletePartida("555", p.getId_partida());
        }
        this.um.deleteUsuario("555");
    }

    @Test
    public void addAndGetPartidaTest() {
        // Verificar cantidad inicial
        assertEquals(1, pm.sizePartidas("555"));

        // Agregar nueva partida
        pm.addPartida("2", "555", 5, 200, 50);
        assertEquals(2, pm.sizePartidas("555"));

        // Obtener partida específica
        Partida partida = pm.getPartida("555", "2");
        assertEquals("555", partida.getId_usuario());
        assertEquals(200, partida.getMonedas().intValue());
    }

    @Test
    public void addPartidaConSoloUsuarioTest() {
        Partida nueva = pm.addPartida("555");
        assertNotNull(nueva.getId_partida());
        assertEquals("555", nueva.getId_usuario());
    }

    @Test
    public void getMonedasDePartidaTest() {
        int monedas = pm.getMonedasDePartida("555", "1");
        assertEquals(100, monedas);
    }

    @Test
    public void updatePartidaTest() {
        Partida partida = pm.getPartida("555", "1");
        partida.setMonedas(999);
        pm.updatePartida(partida);

        Partida actualizada = pm.getPartida("555", "1");
        assertEquals(999, actualizada.getMonedas().intValue());
    }

    @Test
    public void deletePartidaTest() {
        pm.addPartida("3", "555", 3, 50, 0);
        assertEquals(2, pm.sizePartidas("555"));

        pm.deletePartida("555", "3");
        assertEquals(1, pm.sizePartidas("555"));
    }

    @Test
    public void getPartidasTest() {
        pm.addPartida("4", "555", 3, 80, 10);
        pm.addPartida("5", "555", 2, 30, 20);

        List<Partida> partidas = pm.getPartidas("555");
        assertEquals(3, partidas.size());
    }

    @Test
    public void partidaYaExisteExceptionTest() {
        assertThrows(PartidaYaExisteException.class, () -> {
            pm.addPartida("1", "555", 3, 100, 0); // misma ID que la inicial
        });
    }

    @Test
    public void partidaNotFoundExceptionTest() {
        assertThrows(PartidaNotFoundException.class, () -> {
            pm.getPartida("555", "999"); // no existe
        });
    }

}

