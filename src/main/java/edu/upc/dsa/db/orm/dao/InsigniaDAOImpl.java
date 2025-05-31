package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.models.Insignia;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class InsigniaDAOImpl implements InsigniaDAO {

    final static Logger logger = Logger.getLogger(InsigniaDAOImpl.class);

    @Override
    public int addInsignia(Insignia insignia) {
        Session session = null;
        int result = 0;
        try {
            session = FactorySession.openSession();
            session.save(insignia);
            result = 1;
            logger.info("Insignia añadida: " + insignia);
        } catch (Exception e) {
            logger.error("Error al añadir insignia", e);
        } finally {
            if (session != null) session.close();
        }
        return result;
    }

    @Override
    public Insignia getInsignia(String id_insignia) {
        Session session = null;
        Insignia insignia = null;
        try {
            session = FactorySession.openSession();
            insignia = (Insignia) session.get(Insignia.class, id_insignia);
        } catch (Exception e) {
            logger.error("Error al obtener insignia con id " + id_insignia, e);
        } finally {
            if (session != null) session.close();
        }
        return insignia;
    }

    @Override
    public List<Insignia> getInsignias() {
        Session session = null;
        List<Insignia> lista = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            List<Object> resultados = session.findAll(Insignia.class);
            for (Object o : resultados) {
                lista.add((Insignia) o);
            }
        } catch (Exception e) {
            logger.error("Error al obtener todas las insignias", e);
        } finally {
            if (session != null) session.close();
        }
        return lista;
    }

    @Override
    public void updateInsignia(Insignia insignia) {
        Session session = null;
        try {
            session = FactorySession.openSession();
            session.update(insignia);
            logger.info("Insignia actualizada: " + insignia);
        } catch (Exception e) {
            logger.error("Error al actualizar insignia", e);
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void deleteInsignia(String id_insignia) {
        Session session = null;
        try {
            Insignia insignia = this.getInsignia(id_insignia);
            if (insignia != null) {
                session = FactorySession.openSession();
                session.delete(insignia);
                logger.info("Insignia eliminada con id: " + id_insignia);
            }
        } catch (Exception e) {
            logger.error("Error al eliminar insignia con id " + id_insignia, e);
        } finally {
            if (session != null) session.close();
        }
    }
}
