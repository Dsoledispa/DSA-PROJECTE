package edu.upc.dsa.manager;

import edu.upc.dsa.exceptions.usuario.PasswordNotMatchException;
import edu.upc.dsa.exceptions.usuario.UsuarioNotFoundException;
import edu.upc.dsa.exceptions.usuario.UsuarioYaExisteException;
import edu.upc.dsa.models.Usuario;

import java.util.List;

public interface UsuarioManager {

    // Registrar usuario
    Usuario addUsuario(Usuario u);
    Usuario addUsuario(String id_usuario, String nombre, String password);
    // Registrar usuario
    // Login
    Usuario getUsuario(String id_usuario);
    Usuario loginUsuario(String nombre, String password);
    // Login

    void updateUsuario(Usuario u);

    void deleteUsuario(String id_usuario);
    void deleteAllUsuarios();

    List<Usuario> getAllUsuarios();

    Usuario getUsuarioPorNombre(String nombre);

    int sizeUsuarios();

}
