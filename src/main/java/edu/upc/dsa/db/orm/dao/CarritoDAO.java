package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.models.Carrito;

import java.util.List;

public interface CarritoDAO {

    int addCarrito(Carrito carrito);

    List<Carrito> getCarritoByPartida(String id_partida);

    void deleteCarrito(String id_carrito);

    // Nuevo: Eliminar una entrada específica del carrito por id_partida + id_objeto
    boolean deleteByPartidaAndObjeto(String id_partida, String id_objeto);

    // Renombrado: antes clearCarritoByPartida
    void deleteAllFromPartida(String id_partida);
}
