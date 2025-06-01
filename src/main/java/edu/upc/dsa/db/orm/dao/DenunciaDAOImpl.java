package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.models.Denuncia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DenunciaDAOImpl implements DenunciaDAO {

    @Override
    public int addDenuncia(Denuncia denuncia) {
        Session session = null;
        int result = 0;
        try {
            session = FactorySession.openSession();
            session.save(denuncia);
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return result;
    }

    @Override
    public Denuncia getDenuncia(String id_denuncia) {
        Session session = null;
        Denuncia denuncia = null;
        try {
            session = FactorySession.openSession();
            denuncia = (Denuncia) session.get(Denuncia.class, id_denuncia);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return denuncia;
    }

    @Override
    public void updateDenuncia(Denuncia denuncia) {
        Session session = null;
        try {
            session = FactorySession.openSession();
            session.update(denuncia);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void deleteDenuncia(String id_denuncia) {
        Session session = null;
        try {
            Denuncia denuncia = this.getDenuncia(id_denuncia);
            if (denuncia != null) {
                session = FactorySession.openSession();
                session.delete(denuncia);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Denuncia> getDenuncias() {
        Session session = null;
        List<Denuncia> denuncias = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            List<Object> resultados = session.findAll(Denuncia.class);
            for (Object o : resultados) {
                denuncias.add((Denuncia) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return denuncias;
    }

    @Override
    public List<Denuncia> getDenunciasByUsuario(String id_usuario) {
        Session session = null;
        List<Denuncia> denuncias = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("id_usuario", id_usuario);
            List<Object> resultados = session.findAll(Denuncia.class, params);
            for (Object o : resultados) {
                denuncias.add((Denuncia) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return denuncias;
    }
}
