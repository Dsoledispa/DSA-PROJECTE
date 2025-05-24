package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.models.CategoriaObjeto;

import java.util.ArrayList;
import java.util.List;

public class CatObjetoDAOImpl implements CatObjetoDAO {

    @Override
    public int addCategoriaObjeto(String id_categoria, String nombre) {
        Session session = null;
        int result = 0;
        try {
            session = FactorySession.openSession();
            CategoriaObjeto categoria = new CategoriaObjeto(id_categoria, nombre);
            session.save(categoria);
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return result;
    }

    @Override
    public CategoriaObjeto getCategoriaObjeto(String id_categoria) {
        Session session = null;
        CategoriaObjeto categoria = null;
        try {
            session = FactorySession.openSession();
            categoria = (CategoriaObjeto) session.get(CategoriaObjeto.class, id_categoria);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return categoria;
    }

    @Override
    public void updateCategoriaObjeto(String id_categoria, String nombre) {
        Session session = null;
        try {
            CategoriaObjeto categoria = this.getCategoriaObjeto(id_categoria);
            if (categoria != null) {
                categoria.setNombre(nombre);
                session = FactorySession.openSession();
                session.update(categoria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void deleteCategoriaObjeto(String id_categoria) {
        Session session = null;
        try {
            CategoriaObjeto categoria = this.getCategoriaObjeto(id_categoria);
            if (categoria != null) {
                session = FactorySession.openSession();
                session.delete(categoria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<CategoriaObjeto> getCategoriasObjeto() {
        Session session = null;
        List<CategoriaObjeto> categorias = new ArrayList<>(); // inicializamos para evitar null
        try {
            session = FactorySession.openSession();
            List<Object> resultados = session.findAll(CategoriaObjeto.class);
            for (Object obj : resultados) {
                categorias.add((CategoriaObjeto) obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return categorias;
    }
}
