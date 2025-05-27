package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.models.Inventario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventarioDAOImpl implements InventarioDAO {

    @Override
    public int addInventario(Inventario inventario) {
        Session session = null;
        int result = 0;
        try {
            session = FactorySession.openSession();
            session.save(inventario);
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return result;
    }

    @Override
    public List<Inventario> getInventarioByPartida(String id_partida) {
        Session session = null;
        List<Inventario> inventarioList = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("id_partida", id_partida);
            List<Object> resultados = session.findAll(Inventario.class, params);
            for (Object o : resultados) {
                inventarioList.add((Inventario) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return inventarioList;
    }

    @Override
    public boolean deleteByPartidaAndObjeto(String id_partida, String id_objeto) {
        Session session = null;
        boolean deleted = false;
        try {
            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("id_partida", id_partida);
            params.put("id_objeto", id_objeto);
            List<Object> resultados = session.findAll(Inventario.class, params);
            if (!resultados.isEmpty()) {
                session.delete((Inventario) resultados.get(0));
                deleted = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return deleted;
    }

    @Override
    public void deleteAllFromPartida(String id_partida) {
        Session session = null;
        try {
            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("id_partida", id_partida);
            List<Object> resultados = session.findAll(Inventario.class, params);
            for (Object o : resultados) {
                session.delete((Inventario) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }
}
