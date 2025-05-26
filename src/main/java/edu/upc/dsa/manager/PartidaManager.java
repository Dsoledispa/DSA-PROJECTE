package edu.upc.dsa.manager;

import edu.upc.dsa.models.Partida;

import java.util.List;

public interface PartidaManager {

    //Funciona como crear partida
    //id_usuario es el nombre del usuario
    //Es el que finalmente crea
    Partida addPartida(Partida p);
    //Es para tests
    Partida addPartida(String id_partida, String id_usuario, Integer vidas, Integer monedas, Integer puntuacion);
    //Es para a partir de id_usuario, crear el objeto p
    Partida addPartida(String id_usuario);

    //Obtener todas las partidas de un usuario
    List<Partida> getPartidas(String id_usuario);

    // Obtener una partida específica de un usuario
    Partida getPartida(String id_usuario, String id_partida);


    void updatePartida(Partida partida);

    // Elimina una partida del usuario
    void deletePartida(String id_usuario, String id_partida);

    // Elimina todas las partidas del usuario
    void deletePartidas(String id_usuario);

    // Obtener las monedas de una partida específica del usuario
    int getMonedasDePartida(String id_usuario, String id_partida);

    //solo de un usuario
    int sizePartidas(String id_usuario);
}
