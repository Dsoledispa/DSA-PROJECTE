package edu.upc.dsa.manager;

import edu.upc.dsa.db.orm.dao.IUserDAO;
import edu.upc.dsa.db.orm.dao.UserDAOImpl;
import edu.upc.dsa.exceptions.usuario.PasswordNotMatchException;
import edu.upc.dsa.exceptions.usuario.UsuarioNotFoundException;
import edu.upc.dsa.exceptions.usuario.UsuarioYaExisteException;
import edu.upc.dsa.models.Usuario;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class UsuarioManagerImpl implements UsuarioManager {
    private static UsuarioManager instance;
    private final IUserDAO userDAO;
    final static Logger logger = Logger.getLogger(UsuarioManagerImpl.class);
    private String mensajeResultado;

    private UsuarioManagerImpl() {
        this.userDAO = new UserDAOImpl();
    }

    // Patrón singleton
    public static UsuarioManager getInstance() {
        if (instance == null) instance = new UsuarioManagerImpl();
        return instance;
    }

    @Override
    public Usuario addUsuario(Usuario u) throws UsuarioYaExisteException {
        boolean resultado = userDAO.registrarUsuario(u.getNombreUsu(), u.getPassword());
        if (!resultado) {
            this.mensajeResultado = userDAO.getMensajeResultado();
            logger.error("Usuario con " + u.getNombreUsu() + " ya existe");
            throw new UsuarioYaExisteException("Usuario con " + u.getNombreUsu() + " ya existe");
        }
        this.mensajeResultado = userDAO.getMensajeResultado();
        logger.info("Usuario añadido: " + u);
        return u;
    }

    @Override
    public Usuario addUsuario(String nombreUsu, String password) {
        return this.addUsuario(new Usuario(nombreUsu, password));
    }

    @Override
    public Usuario comprobarUsuario(String nombreUsu) {
        try {
            // Utilizamos loginUsuario con contraseña nula para verificar si existe el usuario
            boolean existe = userDAO.loginUsuario(nombreUsu, null);
            if (existe) {
                Usuario u = new Usuario();
                u.setNombreUsu(nombreUsu);
                // No podemos recuperar la contraseña real (está hasheada)
                return u;
            }
            this.mensajeResultado = userDAO.getMensajeResultado();
        } catch (Exception e) {
            logger.info(nombreUsu + " no encontrado ");
            this.mensajeResultado = "Usuario no encontrado";
        }
        return null;
    }

    @Override
    public Usuario getUsuario(String nombreUsu) throws UsuarioNotFoundException {
        Usuario u = comprobarUsuario(nombreUsu);
        if (u == null) {
            this.mensajeResultado = "Usuario " + nombreUsu + " no encontrado";
            throw new UsuarioNotFoundException(nombreUsu + " no encontrado");
        }
        return u;
    }

    @Override
    public Usuario loginUsuario(String nombreUsu, String password) throws PasswordNotMatchException {
        try {
            boolean loginExitoso = userDAO.loginUsuario(nombreUsu, password);
            if (!loginExitoso) {
                this.mensajeResultado = userDAO.getMensajeResultado();
                throw new PasswordNotMatchException("Credenciales incorrectas");
            }
            Usuario u = new Usuario();
            u.setNombreUsu(nombreUsu);
            this.mensajeResultado = userDAO.getMensajeResultado();
            logger.info("Login exitoso para: " + nombreUsu);
            return u;
        } catch (Exception e) {
            if (e instanceof PasswordNotMatchException) throw (PasswordNotMatchException) e;
            logger.error("Error en loginUsuario para " + nombreUsu, e);
            this.mensajeResultado = "Error en login: " + e.getMessage();
            throw new PasswordNotMatchException("Error en login: " + e.getMessage());
        }
    }

    @Override
    public List<Usuario> getAllUsuarios() {
        // Esta funcionalidad no existe en el DAO, devolvemos una lista vacía o simulada
        List<Usuario> usuarios = new ArrayList<>();
        logger.warn("getAllUsuarios: Funcionalidad no disponible con la integración de base de datos");
        this.mensajeResultado = "Funcionalidad no disponible con la integración de base de datos";
        return usuarios;
    }

    @Override
    public int sizeUsuarios() {
        // Esta funcionalidad no existe en el DAO, devolvemos 0 como valor por defecto
        logger.warn("sizeUsuarios: Funcionalidad no disponible con la integración de base de datos");
        this.mensajeResultado = "Funcionalidad no disponible con la integración de base de datos";
        return 0;
    }

    @Override
    public void clear() {
        // Esta funcionalidad no existe en el DAO, no hacemos nada
        logger.warn("clear: Funcionalidad no disponible con la integración de base de datos");
        this.mensajeResultado = "Funcionalidad no disponible con la integración de base de datos";
    }

    @Override
    public int iniciarPartida(String nombreUsuario) {
        try {
            int idPartida = userDAO.iniciarPartida(nombreUsuario);
            this.mensajeResultado = userDAO.getMensajeResultado();
            return idPartida;
        } catch (Exception e) {
            logger.error("Error al iniciar partida para " + nombreUsuario, e);
            this.mensajeResultado = "Error al iniciar partida: " + e.getMessage();
            return -1;
        }
    }

    @Override
    public String getMensajeResultado() {
        return this.mensajeResultado;
    }
}