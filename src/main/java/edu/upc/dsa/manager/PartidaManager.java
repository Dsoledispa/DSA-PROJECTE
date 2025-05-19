package edu.upc.dsa.manager;

import edu.upc.dsa.models.Objeto;
import edu.upc.dsa.models.Partida;

import java.util.List;

public interface PartidaManager {
    // Métodos originales
    Partida addPartida(Partida p);
    Partida addPartida(String id_partida, String id_usuario, Integer vidas, Integer monedas, Integer puntuacion);
    Partida addPartida(String id_usuario);
    List<Partida> getPartidas(String id_usuario);
    Partida getPartida(String id_usuario, String id_partida);
    void deletePartida(String id_usuario, String id_partida);
    int getMonedasDePartida(String id_usuario, String id_partida);
    void clear();
    int sizePartidas(String id_usuario);

    // Métodos adicionales para alinearse con IPartidaDAO
    boolean actualizarPartida(Partida partida);
    boolean actualizarVidas(String id_partida, int vidas);
    boolean actualizarMonedas(String id_partida, int monedas);
    boolean actualizarPuntuacion(String id_partida, int puntuacion);
    List<Objeto> obtenerInventario(String id_partida);
    boolean añadirObjetoInventario(String id_partida, String objeto, int cantidad);
    boolean eliminarObjetoInventario(String id_partida, String objeto, int cantidad);
    boolean comprarObjeto(String id_partida, String objeto);
    boolean tieneMonedasSuficientes(String id_partida, String objeto);
    boolean usarObjeto(String id_partida, String objeto);
    List<Objeto> verInventario(String id_partida);
    String getMensajeResultado();
}