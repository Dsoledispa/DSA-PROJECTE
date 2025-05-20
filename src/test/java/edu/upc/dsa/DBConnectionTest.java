package edu.upc.dsa;

import edu.upc.dsa.db.orm.FactorySession;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class DBConnectionTest {

    @Test
    public void testFactorySessionConnection() {
        try (Connection conn = FactorySession.getConnection()) {
            assertNotNull("La conexión no debe ser nula", conn);
            assertFalse("La conexión no debe estar cerrada", conn.isClosed());
        } catch (SQLException e) {
            fail("Error al obtener conexión: " + e.getMessage());
        }
    }

}
