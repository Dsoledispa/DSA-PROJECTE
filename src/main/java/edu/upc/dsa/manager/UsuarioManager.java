package edu.upc.dsa.manager;

import edu.upc.dsa.exceptions.usuario.PasswordNotMatchException;
import edu.upc.dsa.exceptions.usuario.UsuarioNotFoundException;
import edu.upc.dsa.exceptions.usuario.UsuarioYaExisteException;
import edu.upc.dsa.models.Usuario;

import java.util.List;

public interface UsuarioManager {

    // Registrar usuario
    Usuario addUsuario(Usuario u);
    Usuario addUsuario(String nombreUsu, String password);
    Usuario comprobarUsuario(String nombreUsu);
    // Registrar usuario
    // Login
    Usuario getUsuario(String nombreUsu);
    Usuario loginUsuario(String nombreUsu, String password);
    // Login

    void deleteUsuario(String nombreUsu);

    List<Usuario> getAllUsuarios();

    int sizeUsuarios();

}
