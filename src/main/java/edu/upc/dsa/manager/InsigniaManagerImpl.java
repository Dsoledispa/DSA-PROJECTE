package edu.upc.dsa.manager;

import edu.upc.dsa.db.orm.dao.InsigniaDAO;
import edu.upc.dsa.db.orm.dao.InsigniaDAOImpl;
import edu.upc.dsa.models.Insignia;
import edu.upc.dsa.util.RandomUtils;
import org.apache.log4j.Logger;

import java.util.List;

public class InsigniaManagerImpl implements InsigniaManager {

    final static Logger logger = Logger.getLogger(InsigniaManagerImpl.class);
    Usuario_InsigniaManager uim;
    private InsigniaDAO insigniaDAO;

    public InsigniaManagerImpl() {
        this.uim = new Usuario_InsigniaManagerImpl();
        this.insigniaDAO = new InsigniaDAOImpl();
    }

    @Override
    public Insignia addInsignia(Insignia insignia) {
        if (insignia.getId_insignia() == null || insignia.getId_insignia().trim().isEmpty() || "string".equalsIgnoreCase(insignia.getId_insignia())) {
            insignia.setId_insignia(RandomUtils.getId());
        }

        int result = insigniaDAO.addInsignia(insignia);
        if (result != 1) {
            logger.error("Error al insertar insignia: " + insignia);
            throw new RuntimeException("Error al insertar insignia en BD");
        }

        logger.info("Insignia añadida: " + insignia);
        return insignia;
    }

    @Override
    public Insignia addInsignia(String id, String nombre, String avatar) {
        return this.addInsignia(new Insignia(id, nombre, avatar));
    }

    @Override
    public Insignia getInsignia(String id_insignia) {
        Insignia ins = insigniaDAO.getInsignia(id_insignia);
        if (ins == null) {
            logger.warn("Insignia con id " + id_insignia + " no encontrada");
        }
        return ins;
    }

    @Override
    public List<Insignia> getAllInsignias() {
        List<Insignia> lista = insigniaDAO.getInsignias();
        logger.info("getAllInsignias: " + lista.size() + " insignias encontradas");
        return lista;
    }

    @Override
    public void updateInsignia(Insignia insignia) {
        Insignia existente = insigniaDAO.getInsignia(insignia.getId_insignia());
        if (existente == null) {
            logger.error("No se puede actualizar, insignia no encontrada: " + insignia.getId_insignia());
            throw new RuntimeException("Insignia no encontrada para actualizar");
        }

        insigniaDAO.updateInsignia(insignia);
        logger.info("Insignia actualizada: " + insignia);
    }

    @Override
    public void deleteInsignia(String id_insignia) {
        Insignia ins = insigniaDAO.getInsignia(id_insignia);
        if (ins == null) {
            logger.warn("Intento de borrar insignia inexistente con id: " + id_insignia);
            return;
        }
        this.uim.eliminarUsuariosConInsignia(id_insignia);
        insigniaDAO.deleteInsignia(id_insignia);
        logger.info("Insignia borrada: " + ins.getNombre());
    }

    @Override
    public void deleteAllInsignias() {
        List<Insignia> todas = insigniaDAO.getInsignias();
        for (Insignia i : todas) {
            this.uim.eliminarUsuariosConInsignia(i.getId_insignia());
            insigniaDAO.deleteInsignia(i.getId_insignia());
        }
        logger.info("Todas las insignias han sido eliminadas");
    }

    @Override
    public int sizeInsignias() {
        return this.getAllInsignias().size();
    }
}
