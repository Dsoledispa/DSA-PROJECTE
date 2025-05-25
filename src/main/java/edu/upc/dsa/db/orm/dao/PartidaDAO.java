package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.models.Partida;

import java.util.List;

public interface PartidaDAO {

    int addPartida(Partida partida);

    Partida getPartida(String id_partida);

    void updatePartida(Partida partida);

    void deletePartida(String id_partida);

    List<Partida> getPartidas();

    List<Partida> getPartidasByUsuario(String id_usuario);
}