package edu.upc.dsa.manager;

import edu.upc.dsa.db.orm.dao.DenunciaDAO;
import edu.upc.dsa.db.orm.dao.DenunciaDAOImpl;
import edu.upc.dsa.exceptions.denuncia.DenunciaNotFoundException;
import edu.upc.dsa.exceptions.denuncia.DenunciaYaExisteException;
import edu.upc.dsa.models.Denuncia;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;

public class DenunciaManagerImpl implements DenunciaManager {

    final static Logger logger = Logger.getLogger(DenunciaManagerImpl.class);
    private final DenunciaDAO denunciaDAO;

    public DenunciaManagerImpl() {
        this.denunciaDAO = new DenunciaDAOImpl();
    }

    @Override
    public Denuncia addDenuncia(Denuncia denuncia) {
        if (denunciaDAO.getDenuncia(denuncia.getId_denuncia()) != null) {
            logger.error("Denuncia con id " + denuncia.getId_denuncia() + " ya existe");
            throw new DenunciaYaExisteException("Ya existe una denuncia con ese ID");
        }
        denunciaDAO.addDenuncia(denuncia);
        logger.info("Denuncia añadida: " + denuncia);
        return denuncia;
    }

    @Override
    public Denuncia addDenuncia(String id_denuncia, String titulo, String mensaje, String id_usuario) {
        Denuncia denuncia = new Denuncia(id_denuncia, LocalDateTime.now(), titulo, mensaje, id_usuario);
        return this.addDenuncia(denuncia);
    }

    @Override
    public List<Denuncia> getDenuncias() {
        List<Denuncia> denuncias = denunciaDAO.getDenuncias();
        logger.info("Denuncias obtenidas: " + denuncias.size());
        return denuncias;
    }

    @Override
    public List<Denuncia> getDenunciasByUsuario(String id_usuario) {
        List<Denuncia> denuncias = denunciaDAO.getDenunciasByUsuario(id_usuario);
        logger.info("Denuncias del usuario " + id_usuario + ": " + denuncias.size());
        return denuncias;
    }

    @Override
    public Denuncia getDenuncia(String id_denuncia) {
        Denuncia denuncia = denunciaDAO.getDenuncia(id_denuncia);
        if (denuncia == null) {
            logger.warn("Denuncia no encontrada: " + id_denuncia);
            throw new DenunciaNotFoundException("Denuncia no encontrada");
        }
        return denuncia;
    }

    @Override
    public void updateDenuncia(Denuncia denuncia) {
        if (denunciaDAO.getDenuncia(denuncia.getId_denuncia()) == null) {
            logger.warn("No se puede actualizar: denuncia no encontrada");
            throw new DenunciaNotFoundException("Denuncia no encontrada");
        }
        denunciaDAO.updateDenuncia(denuncia);
        logger.info("Denuncia actualizada: " + denuncia);
    }

    @Override
    public void deleteDenuncia(String id_denuncia) {
        if (denunciaDAO.getDenuncia(id_denuncia) == null) {
            logger.warn("No se puede eliminar: denuncia no encontrada");
            throw new DenunciaNotFoundException("Denuncia no encontrada");
        }
        denunciaDAO.deleteDenuncia(id_denuncia);
        logger.info("Denuncia eliminada con ID: " + id_denuncia);
    }

    @Override
    public int sizeDenuncias() {
        int size = denunciaDAO.getDenuncias().size();
        logger.info("Número de denuncias totales: " + size);
        return size;
    }
}
