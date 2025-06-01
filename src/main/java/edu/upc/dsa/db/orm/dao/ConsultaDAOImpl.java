package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.models.Consulta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConsultaDAOImpl implements ConsultaDAO {

    @Override
    public int addConsulta(Consulta consulta) {
        Session session = null;
        int result = 0;
        try {
            session = FactorySession.openSession();
            session.save(consulta);
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return result;
    }

    @Override
    public Consulta getConsulta(String id_consulta) {
        Session session = null;
        Consulta consulta = null;
        try {
            session = FactorySession.openSession();
            consulta = (Consulta) session.get(Consulta.class, id_consulta);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return consulta;
    }

    @Override
    public void updateConsulta(Consulta consulta) {
        Session session = null;
        try {
            session = FactorySession.openSession();
            session.update(consulta);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void deleteConsulta(String id_consulta) {
        Session session = null;
        try {
            Consulta consulta = this.getConsulta(id_consulta);
            if (consulta != null) {
                session = FactorySession.openSession();
                session.delete(consulta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Consulta> getConsultas() {
        Session session = null;
        List<Consulta> consultas = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            List<Object> resultados = session.findAll(Consulta.class);
            for (Object o : resultados) {
                consultas.add((Consulta) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return consultas;
    }

    @Override
    public List<Consulta> getConsultasByUsuario(String id_usuario) {
        Session session = null;
        List<Consulta> consultas = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("id_usuario", id_usuario);
            List<Object> resultados = session.findAll(Consulta.class, params);
            for (Object o : resultados) {
                consultas.add((Consulta) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return consultas;
    }
}
