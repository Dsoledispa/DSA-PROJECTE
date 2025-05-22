//package edu.upc.dsa.manager;
//
//import edu.upc.dsa.models.Carrito;
//import edu.upc.dsa.models.Objeto;
//import edu.upc.dsa.models.Partida;
//import org.apache.log4j.Logger;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class CarritoManagerImpl implements CarritoManager{
//    private static CarritoManager instance;
//    protected Map<String, Carrito> carritos; // key = id_usuario
//    final static Logger logger = Logger.getLogger(CarritoManagerImpl.class);
//
//    private CarritoManagerImpl() {
//        this.carritos = new HashMap<>();
//    }
//
//    // Patron singleton
//    public static CarritoManager getInstance() {
//        if (instance == null) instance = new CarritoManagerImpl();
//        return instance;
//    }
//
//    @Override
//    public Carrito getCarrito(String id_usuario) {
//        Carrito carrito = this.carritos.computeIfAbsent(id_usuario, id -> new Carrito(id));
//        logger.info("Carrito obtenido: " + carrito);
//        return carrito;
//    }
//
//    @Override
//    public void agregarProductoAlCarrito(String id_usuario, Objeto producto) {
//        Carrito carrito = getCarrito(id_usuario);
//        carrito.agregarObjeto(producto);
//        carrito.getObjetos();
//        logger.info("Carrito de "+ id_usuario + " :"+ carrito.getObjetos());
//    }
//
//    @Override
//    public void eliminarProductoDelCarrito(String id_usuario, String id_producto) {
//        Carrito carrito = getCarrito(id_usuario);
//        carrito.eliminarObjeto(id_producto);
//        logger.info("Eliminado producto "+id_producto+" del carrito de "+ id_usuario);
//    }
//
//    @Override
//    public List<Objeto> getProductosDelCarrito(String id_usuario) {
//        List<Objeto> objetos = new ArrayList<>(getCarrito(id_usuario).getObjetos());
//        logger.info("Productos del carrito de "+id_usuario+": "+objetos);
//        return objetos;
//    }
//
//    @Override
//    public boolean realizarCompra(String id_usuario, String id_partida) {
//        Carrito carrito = getCarrito(id_usuario);
//        int total = carrito.getTotal();
//        logger.info("Intentando realizar compra para usuario "+id_usuario+" en partida "+id_partida+". Total: "+total);
//
//        PartidaManager pm = PartidaManagerImpl.getInstance();
//        List<Partida> partidas = pm.getPartidas(id_usuario);
//
//        for (Partida p : partidas) {
//            if (p.getId_partida().equals(id_partida)) {
//                if (p.getMonedas() >= total) {
//                    // Registrar los productos que se están comprando
//                    logger.info("Productos comprados:"+ carrito.getObjetos());
//
//                    p.setMonedas(p.getMonedas() - total);
//                    p.getInventario().addAll(carrito.getObjetos());
//                    carrito.vaciar();
//
//                    logger.info("Compra realizada con éxito para usuario "+id_usuario+". Monedas ahora: "+ p.getMonedas());
//                    return true;
//                }
//                logger.info("Compra fallida para usuario "+id_usuario+": fondos insuficientes. Monedas: "+p.getMonedas()+", Total: "+ total);
//                return false;
//            }
//        }
//
//        logger.info("Compra fallida para usuario "+id_usuario+": partida con ID "+id_partida+" no encontrada");
//        return false;
//    }
//
//    @Override
//    public void clear() {
//        this.carritos.clear();
//    }
//}
