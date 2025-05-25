package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.models.Objeto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ObjetoDAOImpl implements ObjetoDAO {

    @Override
    public int addObjeto(Objeto objeto) {
        Session session = null;
        int result = 0;
        try {
            session = FactorySession.openSession();
            session.save(objeto);
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return result;
    }

    @Override
    public Objeto getObjeto(String id_objeto) {
        Session session = null;
        Objeto objeto = null;
        try {
            session = FactorySession.openSession();
            objeto = (Objeto) session.get(Objeto.class, id_objeto);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return objeto;
    }

    @Override
    public void updateObjeto(Objeto objeto) {
        Session session = null;
        try {
            session = FactorySession.openSession();
            session.update(objeto);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void deleteObjeto(String id_objeto) {
        Session session = null;
        try {
            Objeto objeto = this.getObjeto(id_objeto);
            if (objeto != null) {
                session = FactorySession.openSession();
                session.delete(objeto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Objeto> getObjetos() {
        Session session = null;
        List<Objeto> objetos = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            List<Object> resultados = session.findAll(Objeto.class);
            for (Object o : resultados) {
                objetos.add((Objeto) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return objetos;
    }

    @Override
    public List<Objeto> getObjetosByCategoria(String id_categoria) {
        Session session = null;
        List<Objeto> objetos = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("id_categoria", id_categoria);
            List<Object> resultados = session.findAll(Objeto.class, params);
            for (Object o : resultados) {
                objetos.add((Objeto) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return objetos;
    }
}