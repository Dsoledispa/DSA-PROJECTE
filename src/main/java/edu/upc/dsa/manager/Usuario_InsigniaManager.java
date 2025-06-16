package edu.upc.dsa.manager;

import edu.upc.dsa.models.Insignia;

import java.util.List;

public interface Usuario_InsigniaManager {

    List<Insignia> getInsigniasDeUsuario(String id_usuario);

    boolean asignarInsigniaAUsuario(String id_usuario, String id_insignia);

    boolean eliminarInsigniaDeUsuario(String id_usuario, String id_insignia);

    void eliminarTodasInsigniasDeUsuario(String id_usuario);

    void eliminarUsuariosConInsignia(String id_insignia);

}