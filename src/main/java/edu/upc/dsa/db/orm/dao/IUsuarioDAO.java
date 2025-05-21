package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.models.Usuario;

import java.util.List;

public interface IUsuarioDAO {
    // Añade un usuario y devuelve un entero (podríamos usar 1 para éxito o 0 para fallo,
    // ya que no tienes id numérico)
    int addUsuario(String nombreUsu, String password);

    //// Obtiene un usuario por nombre de usuario (clave primaria)
    //Usuario getUsuario(String nombreUsu);
//
    //// Actualiza datos de usuario identificado por nombreUsu
    //void updateUsuario(String nombreUsu, String password);
//
    //// Borra un usuario por nombreUsu
    //void deleteUsuario(String nombreUsu);
//
    //// Lista todos los usuarios
    //List<Usuario> getUsuarios();
}