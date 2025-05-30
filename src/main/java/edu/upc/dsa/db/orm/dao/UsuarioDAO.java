package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.models.Usuario;

import java.util.List;

public interface UsuarioDAO {
    // Añade un usuario y devuelve un entero (podríamos usar 1 para éxito o 0 para fallo,
    // ya que no tienes id numérico)
    int addUsuario(Usuario usuario);

    // Obtiene un usuario por id_usuario (clave primaria)
    Usuario getUsuario(String id_usuario);

    // Obtener nombre para comprobar si ya se usa
    Usuario getUsuarioPorNombre(String nombre);

    // Actualiza datos de usuario identificado por nombreUsu
    void updateUsuario(Usuario usuario);

    // Borra un usuario por id_usuario
    void deleteUsuario(String id_usuario);

    // Lista todos los usuarios
    List<Usuario> getUsuarios();
}