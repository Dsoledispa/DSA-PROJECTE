package edu.upc.dsa.util;

import edu.upc.dsa.manager.CatObjetoManager;
import edu.upc.dsa.manager.PartidaManager;
//import edu.upc.dsa.manager.TiendaManager;
import edu.upc.dsa.manager.UsuarioManager;

public class IniciarDatosTests {
    public static void initUsuarios(UsuarioManager um) {
        um.addUsuario("Paco", "1234");
        um.addUsuario("Ana", "1234");
        um.addUsuario("Miguel", "1234");
        um.addUsuario("Diego", "1234");
    }

//    public static void initProductos(TiendaManager tm) {
//        tm.addProducto("1","Espada", 30, "/img/espada.jpg", "Una espada", CategoriaObjeto.ARMAS);
//        tm.addProducto("2", "Armadura", 50, "/img/armadura.png", "Una armadura", CategoriaObjeto.ARMADURAS);
//        tm.addProducto("3", "Poción", 20, "/img/pocion.png", "Una pocion", CategoriaObjeto.POCIONES);
//
//    }

    public static void initCategoriaObjetos(CatObjetoManager com){
        com.addCatObjeto(1, "Armas");
        com.addCatObjeto(2, "Armaduras");
        com.addCatObjeto(3, "Pociones");
    }

    public static void initPartidas(PartidaManager pm) {
        pm.addPartida("1", "Diego", 3, 100, 0);
    }


}
