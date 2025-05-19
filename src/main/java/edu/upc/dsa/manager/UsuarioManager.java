package edu.upc.dsa.manager;

import edu.upc.dsa.exceptions.usuario.PasswordNotMatchException;
import edu.upc.dsa.exceptions.usuario.UsuarioNotFoundException;
import edu.upc.dsa.exceptions.usuario.UsuarioYaExisteException;
import edu.upc.dsa.models.Usuario;

import java.util.List;

public interface UsuarioManager {
    // Métodos originales del Manager
    Usuario addUsuario(Usuario u) throws UsuarioYaExisteException;
    Usuario addUsuario(String nombreUsu, String password);
    Usuario comprobarUsuario(String nombreUsu);
    Usuario getUsuario(String nombreUsu) throws UsuarioNotFoundException;
    Usuario loginUsuario(String nombreUsu, String password) throws PasswordNotMatchException;
    List<Usuario> getAllUsuarios();
    void clear();
    int sizeUsuarios();

    // Métodos adicionales para alinearse con IUserDAO
    int iniciarPartida(String nombreUsuario);
    String getMensajeResultado();
}