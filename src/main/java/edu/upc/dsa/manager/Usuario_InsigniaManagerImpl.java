package edu.upc.dsa.manager;

import edu.upc.dsa.db.orm.dao.Usuario_InsigniaDAO;
import edu.upc.dsa.db.orm.dao.Usuario_InsigniaDAOImpl;
import edu.upc.dsa.models.Usuario_Insignia;
import org.apache.log4j.Logger;

import java.util.List;

public class Usuario_InsigniaManagerImpl implements Usuario_InsigniaManager {

    final static Logger logger = Logger.getLogger(Usuario_InsigniaManagerImpl.class);

    private final Usuario_InsigniaDAO usuarioInsigniaDAO;

    public Usuario_InsigniaManagerImpl() {
        this.usuarioInsigniaDAO = new Usuario_InsigniaDAOImpl();
    }

    @Override
    public List<Usuario_Insignia> getInsigniasDeUsuario(String id_usuario) {
        List<Usuario_Insignia> insignias = usuarioInsigniaDAO.getByUsuario(id_usuario);
        logger.info("Insignias de usuario " + id_usuario + ": " + insignias);
        return insignias;
    }

    @Override
    public boolean asignarInsigniaAUsuario(String id_usuario, String id_insignia) {
        List<Usuario_Insignia> existentes = usuarioInsigniaDAO.getByUsuario(id_usuario);
        for (Usuario_Insignia ui : existentes) {
            if (ui.getId_insignia().equals(id_insignia)) {
                logger.info("La insignia " + id_insignia + " ya está asignada al usuario " + id_usuario);
                return false;
            }
        }

        Usuario_Insignia usuarioInsignia = new Usuario_Insignia(null, id_usuario, id_insignia);
        int result = usuarioInsigniaDAO.addUsuarioInsignia(usuarioInsignia);
        if (result == 1) {
            logger.info("Insignia " + id_insignia + " asignada al usuario " + id_usuario);
            return true;
        } else {
            logger.warn("No se pudo asignar la insignia " + id_insignia + " al usuario " + id_usuario);
            return false;
        }
    }

    @Override
    public boolean eliminarInsigniaDeUsuario(String id_usuario, String id_insignia) {
        boolean success = usuarioInsigniaDAO.deleteByUsuarioAndInsignia(id_usuario, id_insignia);
        if (success) {
            logger.info("Insignia " + id_insignia + " eliminada del usuario " + id_usuario);
        } else {
            logger.warn("No se pudo eliminar la insignia " + id_insignia + " del usuario " + id_usuario);
        }
        return success;
    }

    @Override
    public void eliminarTodasInsigniasDeUsuario(String id_usuario) {
        usuarioInsigniaDAO.deleteAllFromUsuario(id_usuario);
        logger.info("Todas las insignias eliminadas para el usuario " + id_usuario);
    }

    @Override
    public void eliminarUsuariosConInsignia(String id_insignia) {
        usuarioInsigniaDAO.deleteAllFromInsignia(id_insignia);
        logger.info("Todas las relaciones eliminadas para la insignia " + id_insignia);
    }

}
