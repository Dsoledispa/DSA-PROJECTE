package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.models.Usuario_Insignia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Usuario_InsigniaDAOImpl implements Usuario_InsigniaDAO {

    @Override
    public int addUsuarioInsignia(Usuario_Insignia usuarioInsignia) {
        Session session = null;
        int result = 0;
        try {
            session = FactorySession.openSession();
            session.save(usuarioInsignia);
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return result;
    }

    @Override
    public List<Usuario_Insignia> getByUsuario(String id_usuario) {
        Session session = null;
        List<Usuario_Insignia> list = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("id_usuario", id_usuario);
            List<Object> resultados = session.findAll(Usuario_Insignia.class, params);
            for (Object o : resultados) {
                list.add((Usuario_Insignia) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return list;
    }

    @Override
    public boolean deleteByUsuarioAndInsignia(String id_usuario, String id_insignia) {
        Session session = null;
        boolean deleted = false;
        try {
            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("id_usuario", id_usuario);
            params.put("id_insignia", id_insignia);
            List<Object> resultados = session.findAll(Usuario_Insignia.class, params);
            if (!resultados.isEmpty()) {
                session.delete((Usuario_Insignia) resultados.get(0));
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
    public void deleteAllFromUsuario(String id_usuario) {
        Session session = null;
        try {
            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("id_usuario", id_usuario);
            List<Object> resultados = session.findAll(Usuario_Insignia.class, params);
            for (Object o : resultados) {
                session.delete((Usuario_Insignia) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void deleteAllFromInsignia(String id_insignia) {
        Session session = null;
        try {
            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("id_insignia", id_insignia);
            List<Object> resultados = session.findAll(Usuario_Insignia.class, params);
            for (Object o : resultados) {
                session.delete((Usuario_Insignia) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

}
