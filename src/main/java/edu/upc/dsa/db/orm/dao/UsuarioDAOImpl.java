//package edu.upc.dsa.db.orm.dao;
//
//import edu.upc.dsa.db.orm.FactorySession;
//import edu.upc.dsa.db.orm.Session;
//import edu.upc.dsa.models.Usuario;
//
//import java.util.HashMap;
//import java.util.List;
//
//public class UsuarioDAOImpl implements IUsuarioDAO{
//
//    @Override
//    public int addUsuario(String nombreUsu, String password) {
//        Session session = null;
//        int result = 0;
//        try {
//            session = FactorySession.openSession();
//            Usuario usuario = new Usuario(nombreUsu, password);
//            session.save(usuario);
//            result = 1; // éxito
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (session != null)
//                session.close();
//        }
//        return result;
//    }
//
//    @Override
//    public Usuario getUsuario(String nombreUsu) {
//        Session session = null;
//        Usuario usuario = null;
//        try {
//            session = FactorySession.openSession();
//
//            // El ORM que usas parece usar findAll con parámetros para filtrar,
//            // como no tienes un id numérico, usamos un HashMap con "nombreUsu"
//            HashMap<String, String> params = new HashMap<>();
//            params.put("nombreUsu", nombreUsu);
//            List<Usuario> usuarios = session.findAll(Usuario.class, params);
//            if (usuarios != null && !usuarios.isEmpty()) {
//                usuario = usuarios.get(0);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (session != null)
//                session.close();
//        }
//        return usuario;
//    }
//
//    @Override
//    public void updateUsuario(String nombreUsu, String password) {
//        Usuario usuario = this.getUsuario(nombreUsu);
//        if (usuario != null) {
//            usuario.setPassword(password);
//            Session session = null;
//            try {
//                session = FactorySession.openSession();
//                session.update(usuario);
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (session != null)
//                    session.close();
//            }
//        }
//    }
//
//    @Override
//    public void deleteUsuario(String nombreUsu) {
//        Usuario usuario = this.getUsuario(nombreUsu);
//        if (usuario != null) {
//            Session session = null;
//            try {
//                session = FactorySession.openSession();
//                session.delete(usuario);
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (session != null)
//                    session.close();
//            }
//        }
//    }
//
//    @Override
//    public List<Usuario> getUsuarios() {
//        Session session = null;
//        List<Usuario> usuarios = null;
//        try {
//            session = FactorySession.openSession();
//            usuarios = session.findAll(Usuario.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (session != null)
//                session.close();
//        }
//        return usuarios;
//    }
//}
