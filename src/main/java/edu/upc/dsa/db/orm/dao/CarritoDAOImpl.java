package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.models.Carrito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CarritoDAOImpl implements CarritoDAO {

    @Override
    public int addCarrito(Carrito carrito) {
        Session session = null;
        int result = 0;
        try {
            session = FactorySession.openSession();
            session.save(carrito);
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return result;
    }

    @Override
    public List<Carrito> getCarritoByPartida(String id_partida) {
        Session session = null;
        List<Carrito> carritoList = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("id_partida", id_partida);
            List<Object> resultados = session.findAll(Carrito.class, params);
            for (Object o : resultados) {
                carritoList.add((Carrito) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return carritoList;
    }

    @Override
    public void deleteCarrito(String id_carrito) {
        Session session = null;
        try {
            session = FactorySession.openSession();
            Carrito carrito = (Carrito) session.get(Carrito.class, id_carrito);
            if (carrito != null) {
                session.delete(carrito);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
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
            List<Object> resultados = session.findAll(Carrito.class, params);
            if (!resultados.isEmpty()) {
                session.delete((Carrito) resultados.get(0));
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
            List<Object> resultados = session.findAll(Carrito.class, params);
            for (Object o : resultados) {
                session.delete((Carrito) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }
}
