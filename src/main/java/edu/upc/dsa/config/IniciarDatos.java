//package edu.upc.dsa.config;
//
//import edu.upc.dsa.manager.TiendaManager;
//import edu.upc.dsa.manager.TiendaManagerImpl;
//import edu.upc.dsa.manager.UsuarioManager;
//import edu.upc.dsa.manager.UsuarioManagerImpl;
//
//public class IniciarDatos {
//
//    public static void init() {
//        // Obtener instancias de los managers
//        UsuarioManager um = UsuarioManagerImpl.getInstance();
//        TiendaManager tm = TiendaManagerImpl.getInstance();
//
//        // Cargar datos de usuarios si no hay usuarios
//        if (um.sizeUsuarios() == 0) {
//            um.addUsuario("Paco", "1234");
//            um.addUsuario("Ana", "1234");
//            um.addUsuario("Miguel", "1234");
//            um.addUsuario("Diego", "1234");
//        }
//
//        if (tm.sizeProductos() == 0 ) {
//            tm.addProducto("1","Espada", 30, "/img/espada.jpg", "Una espada", CategoriaObjeto.ARMAS);
//            tm.addProducto("2", "Armadura", 50, "/img/armadura.png", "Una armadura", CategoriaObjeto.ARMADURAS);
//            tm.addProducto("3", "Poción", 20, "/img/pocion.png", "Una pocion", CategoriaObjeto.POCIONES);
//        }
//    }
//
//}
