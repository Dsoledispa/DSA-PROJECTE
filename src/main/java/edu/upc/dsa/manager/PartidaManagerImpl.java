package edu.upc.dsa.manager;

import edu.upc.dsa.db.orm.dao.PartidaDAO;
import edu.upc.dsa.db.orm.dao.PartidaDAOImpl;
import edu.upc.dsa.exceptions.partida.PartidaNotFoundException;
import edu.upc.dsa.exceptions.partida.PartidaNotInsertedDbException;
import edu.upc.dsa.exceptions.partida.PartidaYaExisteException;
import edu.upc.dsa.exceptions.usuario.UsuarioNotFoundException;
import edu.upc.dsa.models.Partida;
import org.apache.log4j.Logger;

import java.util.List;

public class PartidaManagerImpl implements PartidaManager {

    final static Logger logger = Logger.getLogger(PartidaManagerImpl.class);
    private final PartidaDAO partidaDAO;

    public PartidaManagerImpl() {
        this.partidaDAO = new PartidaDAOImpl();
    }

    @Override
    public Partida addPartida(Partida p) {
        String id_partida = p.getId_partida();
        String id_usuario = p.getId_usuario();

        Partida existente = partidaDAO.getPartida(id_partida);
        if (existente != null) {
            logger.error("Partida con " + id_partida + " ya existe");
            throw new PartidaYaExisteException("Partida con " + id_partida + " ya existe");
        }

        int result = partidaDAO.addPartida(p);
        if (result == 0) {
            throw new PartidaNotInsertedDbException("Error al insertar partida en BD");
        }

        logger.info("Partida añadida: " + p);
        return p;
    }

    @Override
    public Partida addPartida(String id_partida, String id_usuario, Integer vidas, Integer monedas, Integer puntuacion) {
        return this.addPartida(new Partida(id_partida, id_usuario, vidas, monedas, puntuacion));
    }

    @Override
    public Partida addPartida(String id_usuario) {
        Partida partida = new Partida(null, id_usuario, 3, 100, 0);
        return this.addPartida(partida);
    }

    @Override
    public List<Partida> getPartidas(String id_usuario) {
        List<Partida> partidas = partidaDAO.getPartidasByUsuario(id_usuario);
        logger.info("Partidas de " + id_usuario + ": " + partidas);
        return partidas;
    }

    @Override
    public Partida getPartida(String id_usuario, String id_partida) {
        Partida partida = partidaDAO.getPartida(id_partida);
        if (partida == null || !partida.getId_usuario().equals(id_usuario)) {
            logger.warn("Partida no encontrada: " + id_partida + " para el usuario " + id_usuario);
            throw new PartidaNotFoundException("Partida no encontrada");
        }
        return partida;
    }

    @Override
    public void updatePartida(Partida partida) {
        Partida existente = partidaDAO.getPartida(partida.getId_partida());
        if (existente == null) throw new PartidaNotFoundException("No se puede actualizar: partida no existe");
        partidaDAO.updatePartida(partida);
        logger.info("Partida actualizada: " + partida);
    }

    @Override
    public void deletePartida(String id_usuario, String id_partida) {
        Partida partida = this.getPartida(id_usuario, id_partida);
        if (partida != null) {
            partidaDAO.deletePartida(id_partida);
            logger.info("Partida eliminada: " + partida);
        }
    }

    @Override
    public void deletePartidas(String id_usuario) {
        List<Partida> partidas = this.getPartidas(id_usuario);
        for (Partida p : partidas) {
            partidaDAO.deletePartida(p.getId_partida());
            logger.info("Partida eliminada: " + p);
        }
        logger.info("Todas las partidas de " + id_usuario + " han sido eliminadas");
    }

    @Override
    public int getMonedasDePartida(String id_usuario, String id_partida) {
        Partida partida = this.getPartida(id_usuario, id_partida);
        return partida.getMonedas();
    }

    @Override
    public int sizePartidas(String id_usuario) {
        int size = this.getPartidas(id_usuario).size();
        logger.info("Número de partidas de " + id_usuario + ": " + size);
        return size;
    }

}

