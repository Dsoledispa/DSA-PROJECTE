package edu.upc.dsa.manager;

import edu.upc.dsa.db.orm.dao.UsuarioDAO;
import edu.upc.dsa.db.orm.dao.UsuarioDAOImpl;
import edu.upc.dsa.exceptions.usuario.PasswordNotMatchException;
import edu.upc.dsa.exceptions.usuario.UsuarioNotFoundException;
import edu.upc.dsa.exceptions.usuario.UsuarioNotInsertedDbException;
import edu.upc.dsa.exceptions.usuario.UsuarioYaExisteException;
import edu.upc.dsa.models.Usuario;
import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UsuarioManagerImpl implements UsuarioManager {

    private UsuarioDAO usuarioDAO;
    final static Logger logger = Logger.getLogger(UsuarioManagerImpl.class);

    public UsuarioManagerImpl() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    @Override
    public Usuario addUsuario(Usuario u) {
        String nombreUsu = u.getNombreUsu();
        Usuario comprobar = this.comprobarUsuario(nombreUsu);
        if (comprobar != null) {
            logger.error("Usuario con " + nombreUsu + " ya existe");
            throw new UsuarioYaExisteException("Usuario con " + nombreUsu + " ya existe");
        }

        // Cifrar la contraseña
        String hashedPassword = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
        u.setPassword(hashedPassword);
        logger.info("Que esta pasando"+u);
        int result = usuarioDAO.addUsuario(u);
        if (result != 1) {
            logger.error("Error al añadir usuario: " + u);
            throw new UsuarioNotInsertedDbException("Error al insertar usuario en BD");
        }
        logger.info("Usuario añadido: " + u);
        return u;
    }

    @Override
    public Usuario addUsuario(String nombreUsu, String password) {
        return this.addUsuario(new Usuario(nombreUsu, password));
    }

    @Override
    public Usuario comprobarUsuario(String nombreUsu) {
        return usuarioDAO.getUsuario(nombreUsu);
    }

    @Override
    public Usuario getUsuario(String nombreUsu){
        Usuario u = usuarioDAO.getUsuario(nombreUsu);
        if (u == null) throw new UsuarioNotFoundException(nombreUsu + " no encontrado");
        return u;
    }

    @Override
    public Usuario loginUsuario(String nombreUsu, String password){
        Usuario u = getUsuario(nombreUsu);
        if (!BCrypt.checkpw(password, u.getPassword())) {
            throw new PasswordNotMatchException("Credenciales incorrectas");
        }
        logger.info("Login exitoso para: " + nombreUsu);
        return u;
    }

    @Override
    public void updateUsuario(Usuario u) {
        Usuario existente = usuarioDAO.getUsuario(u.getNombreUsu());
        if (existente == null) {
            logger.error("No se puede actualizar usuario, no encontrado: " + u.getNombreUsu());
            throw new UsuarioNotFoundException(u.getNombreUsu() + " no encontrado");
        }

        // Cifrar la nueva contraseña antes de guardar
        String hashedPassword = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
        u.setPassword(hashedPassword);

        usuarioDAO.updateUsuario(u);
        logger.info("Usuario actualizado: " + u.getNombreUsu());
    }

    @Override
    public void deleteUsuario(String nombreUsu){
        Usuario u = usuarioDAO.getUsuario(nombreUsu);
        if (u == null) {
            logger.error("No se puede borrar usuario, no encontrado: " + nombreUsu);
            throw new UsuarioNotFoundException(nombreUsu + " no encontrado");
        }
        usuarioDAO.deleteUsuario(nombreUsu);
        logger.info("Usuario borrado: " + nombreUsu);
    }

    @Override
    public List<Usuario> getAllUsuarios() {
        List<Usuario> usuarios = usuarioDAO.getUsuarios();
        logger.info("getAllUsuarios: " + usuarios);
        return usuarios;
    }

    @Override
    public int sizeUsuarios() {
        return getAllUsuarios().size();
    }
}
