package edu.upc.dsa.manager;

import edu.upc.dsa.db.orm.dao.IPartidaDAO;
import edu.upc.dsa.db.orm.dao.PartidaDAOImpl;
import edu.upc.dsa.db.orm.dao.ITiendaDAO;
import edu.upc.dsa.db.orm.dao.TiendaDAOImpl;
import edu.upc.dsa.models.Carrito;
import edu.upc.dsa.models.CategoriaObjeto;
import edu.upc.dsa.models.Objeto;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarritoManagerImpl implements CarritoManager {
    private static CarritoManager instance;
    protected Map<String, Carrito> carritos; // key = id_usuario
    private final IPartidaDAO partidaDAO;
    private final ITiendaDAO tiendaDAO;
    final static Logger logger = Logger.getLogger(CarritoManagerImpl.class);
    private String mensajeResultado;

    private CarritoManagerImpl() {
        this.carritos = new HashMap<>();
        this.partidaDAO = new PartidaDAOImpl();
        this.tiendaDAO = new TiendaDAOImpl();
    }

    // Patrón singleton
    public static CarritoManager getInstance() {
        if (instance == null) instance = new CarritoManagerImpl();
        return instance;
    }

    // Método auxiliar para convertir String a CategoriaObjeto
    private CategoriaObjeto stringToCategoria(String categoriaStr) {
        if (categoriaStr == null) return null;
        try {
            return CategoriaObjeto.valueOf(categoriaStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Categoría no reconocida: " + categoriaStr);
            return null;
        }
    }

    @Override
    public Carrito getCarrito(String id_usuario) {
        Carrito carrito = this.carritos.computeIfAbsent(id_usuario, id -> new Carrito(id));
        logger.info("Carrito obtenido para usuario: " + id_usuario);
        this.mensajeResultado = "Carrito obtenido correctamente";
        return carrito;
    }

    @Override
    public void agregarProductoAlCarrito(String id_usuario, Objeto producto) {
        Carrito carrito = getCarrito(id_usuario);
        carrito.agregarObjeto(producto);
        logger.info("Producto " + producto.getObjeto() + " agregado al carrito de " + id_usuario);
        this.mensajeResultado = "Producto agregado al carrito correctamente";
    }

    @Override
    public void eliminarProductoDelCarrito(String id_usuario, String id_producto) {
        Carrito carrito = getCarrito(id_usuario);
        carrito.eliminarObjeto(id_producto);
        logger.info("Producto " + id_producto + " eliminado del carrito de " + id_usuario);
        this.mensajeResultado = "Producto eliminado del carrito correctamente";
    }

    @Override
    public List<Objeto> getProductosDelCarrito(String id_usuario) {
        List<Objeto> objetos = new ArrayList<>(getCarrito(id_usuario).getObjetos());
        logger.info("Obtenidos " + objetos.size() + " productos del carrito de " + id_usuario);
        this.mensajeResultado = "Productos del carrito obtenidos correctamente";
        return objetos;
    }

    @Override
    public boolean realizarCompra(String id_usuario, String id_partida) {
        Carrito carrito = getCarrito(id_usuario);
        int total = carrito.getTotal();
        logger.info("Intentando realizar compra para usuario " + id_usuario + " en partida " + id_partida + ". Total: " + total);

        try {
            // Verificar si hay suficientes monedas
            edu.upc.dsa.models.Partida dbPartida = partidaDAO.obtenerPartida(Integer.parseInt(id_partida));

            if (dbPartida == null) {
                logger.error("Partida no encontrada: " + id_partida);
                this.mensajeResultado = "Partida no encontrada";
                return false;
            }

            if (dbPartida.getMonedas() < total) {
                logger.warn("Monedas insuficientes: " + dbPartida.getMonedas() + " < " + total);
                this.mensajeResultado = "No tienes suficientes monedas para esta compra";
                return false;
            }

            // Actualizar monedas
            partidaDAO.actualizarMonedas(Integer.parseInt(id_partida), dbPartida.getMonedas() - total);

            // Añadir objetos al inventario
            for (Objeto objeto : carrito.getObjetos()) {
                // Verificamos que el objeto existe en la tienda
                edu.upc.dsa.models.Objeto dbObjeto = tiendaDAO.obtenerObjeto(objeto.getObjeto());

                if (dbObjeto == null) {
                    // Si no existe, intentamos añadirlo primero (esto podría no ser necesario en todos los casos)
                    String categoriaStr = objeto.getCategoria() != null ? objeto.getCategoria().name() : "POCIONES";
                    boolean objetoCreado = tiendaDAO.añadirObjetoTienda(
                            objeto.getObjeto(),
                            categoriaStr,
                            objeto.getPrecio()
                    );

                    if (!objetoCreado) {
                        logger.warn("No se pudo añadir el objeto a la tienda: " + objeto.getObjeto());
                        continue;
                    }
                }

                // Ahora añadimos el objeto al inventario de la partida
                partidaDAO.añadirObjetoInventario(
                        Integer.parseInt(id_partida),
                        objeto.getObjeto(),
                        1
                );

                logger.info("Objeto añadido al inventario: " + objeto.getObjeto());
            }

            // Vaciar carrito
            carrito.vaciar();

            this.mensajeResultado = "Compra realizada correctamente";
            logger.info("Compra realizada con éxito para partida " + id_partida);
            return true;

        } catch (Exception e) {
            this.mensajeResultado = "Error al realizar la compra: " + e.getMessage();
            logger.error("Error al realizar la compra", e);
            return false;
        }
    }

    @Override
    public void clear() {
        this.carritos.clear();
        logger.info("Todos los carritos han sido eliminados");
        this.mensajeResultado = "Todos los carritos han sido eliminados";
    }

    @Override
    public String getMensajeResultado() {
        return this.mensajeResultado;
    }
}