//package edu.upc.dsa.manager;
//
//import edu.upc.dsa.exceptions.partida.PartidaYaExisteException;
//import edu.upc.dsa.exceptions.usuario.UsuarioNotFoundException;
//import edu.upc.dsa.models.Partida;
//import edu.upc.dsa.models.Usuario;
//import org.apache.log4j.Logger;
//
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//
//public class PartidaManagerImpl implements PartidaManager{
//
//    private static PartidaManager instance;
//    final static Logger logger = Logger.getLogger(PartidaManagerImpl.class);
//
//    private PartidaManagerImpl() {}
//
//    // Patron singleton
//    public static PartidaManager getInstance() {
//        if (instance == null) instance = new PartidaManagerImpl();
//        return instance;
//    }
//
//    @Override
//    public Partida addPartida(Partida p) {
//        //logger.info("Nueva partida " + p);
//        String id_partida = p.getId_partida();
//        String id_usuario = p.getId_usuario();
//        List<Partida> partidas = getPartidas(id_usuario);
//        for (Partida partida : partidas){
//            if (partida.getId_partida().equals(id_partida)){
//                logger.error("Partida con " + id_partida + " ya existe");
//                throw new PartidaYaExisteException("Partida con " + id_partida + " ya existe");
//            }
//        }
//        try {
//            Usuario usuario = UsuarioManagerImpl.getInstance().getUsuario(id_usuario);
//            if (usuario == null){
//                logger.error("Usuario no encontrado: " + id_usuario);
//                throw new UsuarioNotFoundException("Usuario no encontrado: " + id_usuario);
//            }
//            usuario.getPartidas().add(p);
//            logger.info("Usuario con partidas: " +usuario);
//        } catch (Exception e) {
//            logger.error("Error al obtener el usuario: " + id_usuario, e);
//            throw e;
//        }
//        return p;
//    }
//
//    @Override
//    public Partida addPartida(String id_partida, String id_usuario, Integer vidas, Integer monedas, Integer puntuacion) {
//        return this.addPartida(new Partida(id_partida, id_usuario, vidas, monedas, puntuacion));
//    }
//
//    @Override
//    public Partida addPartida(String id_usuario) {
//        Partida partida= new Partida (null, id_usuario, 3, 100, 0);
//        return this.addPartida(partida);
//    }
//
//    @Override
//    public List<Partida> getPartidas(String id_usuario) {
//        Usuario usuario = UsuarioManagerImpl.getInstance().getUsuario(id_usuario);
//        List<Partida> partidas = usuario.getPartidas();
//        logger.info("Partidas: "+partidas);
//        return partidas;
//    }
//
//    @Override
//    public Partida getPartida(String id_usuario, String id_partida) {
//        Usuario usuario = UsuarioManagerImpl.getInstance().getUsuario(id_usuario);
//        if (usuario == null) {
//            logger.warn("Usuario no encontrado: " + id_usuario);
//            throw new UsuarioNotFoundException("Usuario no encontrado: " + id_usuario);
//        }
//
//        List<Partida> partidas = usuario.getPartidas();
//        for (Partida partida : partidas) {
//            if (partida.getId_partida().equals(id_partida)) {
//                return partida; // Devuelve la partida si se encuentra
//            }
//        }
//
//        logger.warn("Partida no encontrada: " + id_partida + " para el usuario " + id_usuario);
//        throw new RuntimeException("Partida no encontrada");
//    }
//
//    @Override
//    public void deletePartida(String id_usuario, String id_partida) {
//        Usuario usuario = UsuarioManagerImpl.getInstance().getUsuario(id_usuario);
//        if (usuario == null) {
//            logger.warn("Usuario no encontrado: " + id_usuario);
//            throw new UsuarioNotFoundException("Usuario no encontrado: " + id_usuario);
//        }
//
//        List<Partida> partidas = usuario.getPartidas();
//        Iterator<Partida> iterator = partidas.iterator();
//
//        while (iterator.hasNext()) {
//            Partida p = iterator.next();
//            if (p.getId_partida().equals(id_partida)) {
//                iterator.remove();
//                logger.info("Partida eliminada: " + p);
//                return;
//            }
//        }
//
//        logger.warn("Partida no encontrada: " + id_partida + " para el usuario " + id_usuario);
//    }
//
//    @Override
//    public int getMonedasDePartida(String id_usuario, String id_partida) {
//        Usuario usuario = UsuarioManagerImpl.getInstance().getUsuario(id_usuario);
//        if (usuario == null) {
//            logger.warn("Usuario no encontrado: " + id_usuario);
//            throw new UsuarioNotFoundException("Usuario no encontrado: " + id_usuario);
//        }
//
//        List<Partida> partidas = usuario.getPartidas();
//        for (Partida partida : partidas) {
//            if (partida.getId_partida().equals(id_partida)) {
//                return partida.getMonedas(); // Devuelve las monedas de la partida
//            }
//        }
//
//        logger.warn("Partida no encontrada: " + id_partida + " para el usuario " + id_usuario);
//        throw new RuntimeException("Partida no encontrada");
//    }
//
//
//    @Override
//    public void clear() {
//        List<Usuario> usuarios = UsuarioManagerImpl.getInstance().getAllUsuarios();
//        for (Usuario u : usuarios) {
//            u.getPartidas().clear();
//        }
//        //Todas las partidas han sido eliminadas de todos los usuarios
//    }
//
//    @Override
//    public int sizePartidas(String id_usuario) {
//        Usuario usuario = UsuarioManagerImpl.getInstance().getUsuario(id_usuario);
//        int partidas = usuario.getPartidas().size();
//        logger.info("size partidas " + partidas);
//        return partidas;
//    }
//}
