//package edu.upc.dsa.modelTest;
//
//import edu.upc.dsa.manager.*;
//import edu.upc.dsa.util.IniciarDatosTests;
//import org.apache.log4j.Logger;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertThrows;
//
//public class PartidaManagerTest {
//
//    final static Logger logger = Logger.getLogger(PartidaManagerTest.class);
//    UsuarioManager um;
//    PartidaManager pm;
//
//    @Before
//    public void setUp() {
//        this.um = UsuarioManagerImpl.getInstance();
//        this.pm = PartidaManagerImpl.getInstance();
//        this.um.clear();
//        this.pm.clear();
//
//        IniciarDatosTests.initUsuarios(this.um);
//        IniciarDatosTests.initPartidas(this.pm);
//    }
//
//    @After
//    public void tearDown() {
//        this.um.clear();
//        this.pm.clear();
//    }
//
//    @Test
//    public void addPartidaTest() throws Exception {
//        this.pm.getPartidas("Diego");
//        assertEquals(1,this.pm.sizePartidas("Diego"));
//        this.pm.addPartida("2", "Diego", 3, 100, 0);
//        assertEquals(2,this.pm.sizePartidas("Diego"));
//        this.pm.deletePartida("Diego", "1");
//        assertEquals(1,this.pm.sizePartidas("Diego"));
//    }
//}
