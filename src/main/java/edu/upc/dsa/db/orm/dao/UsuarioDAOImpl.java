package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.models.Usuario;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    final static Logger logger = Logger.getLogger(UsuarioDAOImpl.class);

    @Override
    public int addUsuario(Usuario usuario) {
        Session session = null;
        int result = 0;
        try {
            session = FactorySession.openSession();
            session.save(usuario);
            result = 1;
            logger.info("Usuario con id: "+usuario.getId_usuario()+" guardado: " + usuario.getNombre());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return result;
    }

    @Override
    public Usuario getUsuario(String id_usuario) {
        Session session = null;
        Usuario usuario = null;
        try {
            session = FactorySession.openSession();
            usuario = (Usuario) session.get(Usuario.class, id_usuario);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return usuario;
    }

    @Override
    public Usuario getUsuarioPorNombre(String nombre) {
        Session session = null;
        Usuario usuario = null;
        try {
            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("nombre", nombre);
            usuario = (Usuario) session.get(Usuario.class, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return usuario;
    }

    @Override
    public void updateUsuario(Usuario usuario) {
        Session session = null;
        try {
            session = FactorySession.openSession();
            session.update(usuario);
            logger.info("Usuario con id: "+usuario.getId_usuario()+" actualizado: " + usuario.getNombre());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void deleteUsuario(String id_usuario) {
        Session session = null;
        try {
            Usuario usuario = this.getUsuario(id_usuario);
            if (usuario != null) {
                session = FactorySession.openSession();
                session.delete(usuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Usuario> getUsuarios() {
        Session session = null;
        List<Usuario> usuarios = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            List<Object> resultados = session.findAll(Usuario.class);
            for (Object obj : resultados) {
                usuarios.add((Usuario) obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return usuarios;
    }
}