package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.models.Inventario;

import java.util.List;

public interface InventarioDAO {

    int addInventario(Inventario inventario);

    List<Inventario> getInventarioByPartida(String id_partida);

    boolean deleteByPartidaAndObjeto(String id_partida, String id_objeto);

    void deleteAllFromPartida(String id_partida);
}
