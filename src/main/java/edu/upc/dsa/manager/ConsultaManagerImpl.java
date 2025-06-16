package edu.upc.dsa.manager;

import edu.upc.dsa.db.orm.dao.ConsultaDAO;
import edu.upc.dsa.db.orm.dao.ConsultaDAOImpl;
import edu.upc.dsa.exceptions.consulta.ConsultaNotFoundException;
import edu.upc.dsa.exceptions.consulta.ConsultaYaExisteException;
import edu.upc.dsa.models.Consulta;
import edu.upc.dsa.util.RandomUtils;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;

public class ConsultaManagerImpl implements ConsultaManager {

    final static Logger logger = Logger.getLogger(ConsultaManagerImpl.class);
    private final ConsultaDAO consultaDAO;

    public ConsultaManagerImpl() {
        this.consultaDAO = new ConsultaDAOImpl();
    }

    @Override
    public Consulta addConsulta(Consulta consulta) {
        if (consultaDAO.getConsulta(consulta.getId_consulta()) != null) {
            logger.error("Consulta con id " + consulta.getId_consulta() + " ya existe");
            throw new ConsultaYaExisteException("Ya existe una consulta con ese ID");
        }
        String id = consulta.getId_consulta();
        if (id == null || id.trim().isEmpty() || "string".equalsIgnoreCase(id)) {
            consulta.setId_consulta(RandomUtils.getId());
        }
        consultaDAO.addConsulta(consulta);
        logger.info("Consulta añadida: " + consulta);
        return consulta;
    }

    @Override
    public Consulta addConsulta(String id_consulta, String titulo, String mensaje, String id_usuario) {
        Consulta consulta = new Consulta(id_consulta, LocalDateTime.now(), titulo, mensaje, id_usuario);
        return this.addConsulta(consulta);
    }

    @Override
    public List<Consulta> getConsultas() {
        List<Consulta> consultas = consultaDAO.getConsultas();
        logger.info("Consultas obtenidas: " + consultas.size());
        return consultas;
    }

    @Override
    public List<Consulta> getConsultasByUsuario(String id_usuario) {
        List<Consulta> consultas = consultaDAO.getConsultasByUsuario(id_usuario);
        logger.info("Consultas del usuario " + id_usuario + ": " + consultas.size());
        return consultas;
    }

    @Override
    public Consulta getConsulta(String id_consulta) {
        Consulta consulta = consultaDAO.getConsulta(id_consulta);
        if (consulta == null) {
            logger.warn("Consulta no encontrada: " + id_consulta);
            throw new ConsultaNotFoundException("Consulta no encontrada");
        }
        return consulta;
    }

    @Override
    public void updateConsulta(Consulta consulta) {
        if (consultaDAO.getConsulta(consulta.getId_consulta()) == null) {
            logger.warn("No se puede actualizar: consulta no existe");
            throw new ConsultaNotFoundException("Consulta no encontrada");
        }
        consultaDAO.updateConsulta(consulta);
        logger.info("Consulta actualizada: " + consulta);
    }

    @Override
    public void deleteConsulta(String id_consulta) {
        if (consultaDAO.getConsulta(id_consulta) == null) {
            logger.warn("No se puede eliminar: consulta no encontrada");
            throw new ConsultaNotFoundException("Consulta no encontrada");
        }
        consultaDAO.deleteConsulta(id_consulta);
        logger.info("Consulta eliminada con ID: " + id_consulta);
    }

    @Override
    public int sizeConsultas() {
        int size = consultaDAO.getConsultas().size();
        logger.info("Número de consultas totales: " + size);
        return size;
    }
}
