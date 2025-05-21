package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.models.CategoriaObjeto;

import java.util.HashMap;
import java.util.List;

public class ICatObjetoDAOImpl implements ICatObjetoDAO {

    @Override
    public int addCategoriaObjeto(int id_categoria, String nombre) {
        Session session = null;
        int result = 0;
        try {
            session = FactorySession.openSession();
            CategoriaObjeto categoria = new CategoriaObjeto(id_categoria, nombre);
            session.save(categoria);
            result = 1; // éxito
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
        return result;
    }

    @Override
    public CategoriaObjeto getCategoriaObjeto(int id_categoria) {
        Session session = null;
        CategoriaObjeto categoria = null;
        try {
            session = FactorySession.openSession();

            HashMap<String, Integer> params = new HashMap<>();
            params.put("id_categoria", id_categoria);

            List<CategoriaObjeto> categorias = session.findAll(CategoriaObjeto.class, params);
            if (categorias != null && !categorias.isEmpty()) {
                categoria = categorias.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
        return categoria;
    }

    @Override
    public void updateCategoriaObjeto(int id_categoria, String nombre) {
        CategoriaObjeto categoria = this.getCategoriaObjeto(id_categoria);
        if (categoria != null) {
            categoria.setNombre(nombre);
            Session session = null;
            try {
                session = FactorySession.openSession();
                session.update(categoria);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (session != null)
                    session.close();
            }
        }
    }

    @Override
    public void deleteCategoriaObjeto(int id_categoria) {
        CategoriaObjeto categoria = this.getCategoriaObjeto(id_categoria);
        if (categoria != null) {
            Session session = null;
            try {
                session = FactorySession.openSession();
                session.delete(categoria);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (session != null)
                    session.close();
            }
        }
    }

    @Override
    public List<CategoriaObjeto> getCategoriasObjeto() {
        Session session = null;
        List<CategoriaObjeto> categorias = null;
        try {
            session = FactorySession.openSession();
            categorias = session.findAll(CategoriaObjeto.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
        return categorias;
    }
}