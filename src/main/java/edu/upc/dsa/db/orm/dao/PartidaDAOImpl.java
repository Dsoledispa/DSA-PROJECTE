package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.models.Partida;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PartidaDAOImpl implements PartidaDAO {

    @Override
    public int addPartida(Partida partida) {
        Session session = null;
        int result = 0;
        try {
            session = FactorySession.openSession();
            session.save(partida);
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return result;
    }

    @Override
    public Partida getPartida(String id_partida) {
        Session session = null;
        Partida partida = null;
        try {
            session = FactorySession.openSession();
            partida = (Partida) session.get(Partida.class, id_partida);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return partida;
    }

    @Override
    public void updatePartida(Partida partida) {
        Session session = null;
        try {
            session = FactorySession.openSession();
            session.update(partida);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void deletePartida(String id_partida) {
        Session session = null;
        try {
            Partida partida = this.getPartida(id_partida);
            if (partida != null) {
                session = FactorySession.openSession();
                session.delete(partida);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Partida> getPartidas() {
        Session session = null;
        List<Partida> partidas = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            List<Object> resultados = session.findAll(Partida.class);
            for (Object o : resultados) {
                partidas.add((Partida) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return partidas;
    }

    @Override
    public List<Partida> getPartidasByUsuario(String id_usuario) {
        Session session = null;
        List<Partida> partidas = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("id_usuario", id_usuario);
            List<Object> resultados = session.findAll(Partida.class, params);
            for (Object o : resultados) {
                partidas.add((Partida) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return partidas;
    }
}
