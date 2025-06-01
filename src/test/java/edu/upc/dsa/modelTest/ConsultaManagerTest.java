package edu.upc.dsa.modelTest;

import edu.upc.dsa.exceptions.consulta.ConsultaNotFoundException;
import edu.upc.dsa.exceptions.consulta.ConsultaYaExisteException;
import edu.upc.dsa.manager.ConsultaManager;
import edu.upc.dsa.manager.ConsultaManagerImpl;
import edu.upc.dsa.manager.UsuarioManager;
import edu.upc.dsa.manager.UsuarioManagerImpl;
import edu.upc.dsa.models.Consulta;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ConsultaManagerTest {

    final static Logger logger = Logger.getLogger(ConsultaManagerTest.class);
    UsuarioManager um;
    ConsultaManager csm;

    @Before
    public void setUp() {
        this.um = new UsuarioManagerImpl();
        this.csm = new ConsultaManagerImpl();

        this.um.addUsuario("u-test", "UsuarioConsulta", "pass123");
        csm.addConsulta("c1", "Título prueba", "Mensaje de prueba", "u-test");
    }

    @After
    public void tearDown() {
        List<Consulta> consultas = csm.getConsultasByUsuario("u-test");
        for (Consulta c : consultas) {
            csm.deleteConsulta(c.getId_consulta());
        }
        this.um.deleteUsuario("u-test");
    }

    @Test
    public void addAndGetConsultaTest() {
        assertEquals(1, csm.getConsultasByUsuario("u-test").size());

        csm.addConsulta("c2", "Otra consulta", "Otro mensaje", "u-test");
        assertEquals(2, csm.getConsultasByUsuario("u-test").size());

        Consulta c = csm.getConsulta("c2");
        assertEquals("u-test", c.getId_usuario());
        assertEquals("Otra consulta", c.getTitulo());
    }

    @Test
    public void updateConsultaTest() {
        Consulta c = csm.getConsulta("c1");
        c.setMensaje("Mensaje actualizado");
        csm.updateConsulta(c);

        Consulta updated = csm.getConsulta("c1");
        assertEquals("Mensaje actualizado", updated.getMensaje());
    }

    @Test
    public void deleteConsultaTest() {
        csm.addConsulta("c3", "Borrar", "Se eliminará", "u-test");
        assertEquals(2, csm.getConsultasByUsuario("u-test").size());

        csm.deleteConsulta("c3");
        assertEquals(1, csm.getConsultasByUsuario("u-test").size());
    }

    @Test
    public void getConsultasTest() {
        csm.addConsulta("c4", "Consulta A", "Contenido A", "u-test");
        csm.addConsulta("c5", "Consulta B", "Contenido B", "u-test");

        List<Consulta> todas = csm.getConsultas();
        List<Consulta> delUsuario = csm.getConsultasByUsuario("u-test");

        assertTrue(todas.size() >= 3);  // Puede haber otras consultas de otros tests
        assertEquals(3, delUsuario.size());
    }

    @Test
    public void consultaYaExisteExceptionTest() {
        assertThrows(ConsultaYaExisteException.class, () -> {
            csm.addConsulta("c1", "Título duplicado", "Mensaje duplicado", "u-test");
        });
    }

    @Test
    public void consultaNotFoundExceptionTest() {
        assertThrows(ConsultaNotFoundException.class, () -> {
            csm.getConsulta("no-existe");
        });

        assertThrows(ConsultaNotFoundException.class, () -> {
            Consulta c = new Consulta("no-existe", null, "?", "?", "u-test");
            csm.updateConsulta(c);
        });

        assertThrows(ConsultaNotFoundException.class, () -> {
            csm.deleteConsulta("no-existe");
        });
    }
}
