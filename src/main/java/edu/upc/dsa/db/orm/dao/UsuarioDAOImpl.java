package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.models.Usuario;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    final static Logger logger = Logger.getLogger(UsuarioDAOImpl.class);

    @Override
    public int addUsuario(String nombreUsu, String password) {
        Session session = null;
        int result = 0;
        try {
            session = FactorySession.openSession();
            Usuario usuario = new Usuario(nombreUsu, password);
            logger.info("A ver que pasa aui "+usuario);
            session.save(usuario);
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return result;
    }

    @Override
    public Usuario getUsuario(String nombreUsu) {
        Session session = null;
        Usuario usuario = null;
        try {
            session = FactorySession.openSession();
            usuario = (Usuario) session.get(Usuario.class, nombreUsu);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return usuario;
    }

    @Override
    public void updateUsuario(String nombreUsu, String password) {
        Session session = null;
        try {
            Usuario usuario = this.getUsuario(nombreUsu);
            if (usuario != null) {
                usuario.setPassword(password);
                session = FactorySession.openSession();
                session.update(usuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void deleteUsuario(String nombreUsu) {
        Session session = null;
        try {
            Usuario usuario = this.getUsuario(nombreUsu);
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