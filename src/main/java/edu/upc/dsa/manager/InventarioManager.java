package edu.upc.dsa.manager;

import edu.upc.dsa.models.Objeto;

import java.util.List;

public interface InventarioManager {

    // Obtener todos los objetos del inventario de una partida
    List<Objeto> getInventarioDePartida(String id_partida);

    // Agrega un objeto al inventario de una partida
    boolean agregarObjetoAInventario(String id_partida, String id_objeto);

    // Elimina un objeto del inventario de una partida
    boolean eliminarObjetoDeInventario(String id_partida, String id_objeto);

    // Elimina todos los objetos del inventario de una partida
    void eliminarAllObjetosDeInventario(String id_partida);
}
