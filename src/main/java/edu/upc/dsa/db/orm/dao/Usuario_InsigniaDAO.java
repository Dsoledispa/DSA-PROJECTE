package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.models.Usuario_Insignia;

import java.util.List;

public interface Usuario_InsigniaDAO {

    int addUsuarioInsignia(Usuario_Insignia usuarioInsignia);

    List<Usuario_Insignia> getByUsuario(String id_usuario);

    boolean deleteByUsuarioAndInsignia(String id_usuario, String id_insignia);

    void deleteAllFromUsuario(String id_usuario);

    void deleteAllFromInsignia(String id_insignia);

}
