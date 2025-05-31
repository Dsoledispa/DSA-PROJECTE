package edu.upc.dsa.manager;

import edu.upc.dsa.db.orm.dao.UsuarioDAO;
import edu.upc.dsa.db.orm.dao.UsuarioDAOImpl;
import edu.upc.dsa.exceptions.usuario.PasswordNotMatchException;
import edu.upc.dsa.exceptions.usuario.UsuarioNotFoundException;
import edu.upc.dsa.exceptions.usuario.UsuarioNotInsertedDbException;
import edu.upc.dsa.exceptions.usuario.UsuarioYaExisteException;
import edu.upc.dsa.models.Usuario;
import edu.upc.dsa.util.RandomUtils;
import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UsuarioManagerImpl implements UsuarioManager {

    final static Logger logger = Logger.getLogger(UsuarioManagerImpl.class);
    private PartidaManager pm;
    Usuario_InsigniaManager uim;
    private UsuarioDAO usuarioDAO;

    public UsuarioManagerImpl() {
        this.pm = new PartidaManagerImpl();
        this.uim = new Usuario_InsigniaManagerImpl();
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    @Override
    public Usuario addUsuario(Usuario u) {
        Usuario ComprobarId = usuarioDAO.getUsuario(u.getId_usuario());
        if (ComprobarId != null) {
            logger.error("Usuario con ID :" + u.getId_usuario() + " ya existe");
            throw new UsuarioYaExisteException("Usuario con ID :" + u.getId_usuario() + " ya existe");
        }
        Usuario comprobarNombre = usuarioDAO.getUsuarioPorNombre(u.getNombre());
        if (comprobarNombre != null) {
            logger.error("Usuario con nombre :" + u.getNombre() + " ya existe");
            throw new UsuarioYaExisteException("Usuario con nombre :" + u.getNombre() + " ya existe");
        }

        // Cifrar la contraseña
        String hashedPassword = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
        u.setPassword(hashedPassword);
        // Para el swagger
        String id = u.getId_usuario();
        if (id == null || id.trim().isEmpty() || "string".equalsIgnoreCase(id)) {
            u.setId_usuario(RandomUtils.getId());
        }
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
    public Usuario addUsuario(String id_usuario, String nombre, String password) {
        return this.addUsuario(new Usuario(id_usuario, nombre, password));
    }

    @Override
    public Usuario getUsuario(String id_usuario){
        Usuario u = usuarioDAO.getUsuario(id_usuario);
        if (u == null) throw new UsuarioNotFoundException("Usuario con : "+ id_usuario + " no encontrado");
        return u;
    }

    @Override
    public Usuario loginUsuario(String nombre, String password){
        Usuario u = usuarioDAO.getUsuarioPorNombre(nombre);
        if (u == null) throw new UsuarioNotFoundException("Usuario con nombre: "+ nombre + " no encontrado");
        if (!BCrypt.checkpw(password, u.getPassword())) {
            throw new PasswordNotMatchException("Credenciales incorrectas");
        }
        logger.info("Login exitoso para: " + nombre);
        return u;
    }

    @Override
    public void updateUsuario(Usuario u) {
        Usuario existente = usuarioDAO.getUsuario(u.getId_usuario());
        if (existente == null) {
            logger.error("No se puede actualizar usuario, no encontrado: " + u.getNombre());
            throw new UsuarioNotFoundException(u.getNombre() + " no encontrado");
        }

        Usuario comprobar = usuarioDAO.getUsuarioPorNombre(u.getNombre());
        if (comprobar != null && !comprobar.getId_usuario().equals(u.getId_usuario())) {
            throw new UsuarioYaExisteException("Usuario con nombre :" + u.getNombre() + " ya existe");
        }

        // Cifrar la nueva contraseña antes de guardar
        String hashedPassword = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
        u.setPassword(hashedPassword);

        usuarioDAO.updateUsuario(u);
        logger.info("Usuario actualizado: " + u.getNombre());
    }

    @Override
    public void deleteUsuario(String id_usuario){
        Usuario u = usuarioDAO.getUsuario(id_usuario);
        if (u == null) {
            logger.error("No se puede borrar usuario, no encontrado con su id: " + id_usuario);
            throw new UsuarioNotFoundException("Usuario con id :" +id_usuario + " no encontrado");
        }
        this.pm.deletePartidas(id_usuario);
        this.uim.eliminarTodasInsigniasDeUsuario(id_usuario);
        usuarioDAO.deleteUsuario(id_usuario);
        logger.info("Usuario borrado: " + u.getNombre());
    }

    @Override
    public void deleteAllUsuarios() {
        List<Usuario> usuarios = usuarioDAO.getUsuarios();
        for (Usuario u : usuarios){
            this.pm.deletePartidas(u.getId_usuario());
            this.uim.eliminarTodasInsigniasDeUsuario(u.getId_usuario());
            usuarioDAO.deleteUsuario(u.getId_usuario());
        }
        logger.info("Todos los usuarios borrados");
    }

    @Override
    public List<Usuario> getAllUsuarios() {
        List<Usuario> usuarios = usuarioDAO.getUsuarios();
        logger.info("getAllUsuarios: " + usuarios);
        return usuarios;
    }

    @Override
    public Usuario getUsuarioPorNombre(String nombre) {
        return usuarioDAO.getUsuarioPorNombre(nombre);
    }

    @Override
    public int sizeUsuarios() {
        return getAllUsuarios().size();
    }
}
