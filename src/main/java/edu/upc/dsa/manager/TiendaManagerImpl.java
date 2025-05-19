package edu.upc.dsa.manager;

import edu.upc.dsa.db.orm.dao.ITiendaDAO;
import edu.upc.dsa.db.orm.dao.TiendaDAOImpl;
import edu.upc.dsa.models.CategoriaObjeto;
import edu.upc.dsa.models.Objeto;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TiendaManagerImpl implements TiendaManager {
    private static TiendaManager instance;
    private final ITiendaDAO tiendaDAO;
    final static Logger logger = Logger.getLogger(TiendaManagerImpl.class);
    private String mensajeResultado;

    private TiendaManagerImpl() {
        this.tiendaDAO = new TiendaDAOImpl();
    }

    // Patrón singleton
    public static TiendaManager getInstance() {
        if (instance == null) instance = new TiendaManagerImpl();
        return instance;
    }

    // Método auxiliar para convertir string a enum CategoriaObjeto
    private CategoriaObjeto stringToCategoria(String categoriaStr) {
        if (categoriaStr == null) return null;
        try {
            return CategoriaObjeto.valueOf(categoriaStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Categoría no reconocida: " + categoriaStr);
            return null;
        }
    }

    // Método auxiliar para convertir modelo de DB a modelo de negocio
    private Objeto convertirObjetoDBaObjeto(edu.upc.dsa.models.Objeto dbObjeto) {
        if (dbObjeto == null) return null;

        Objeto o = new Objeto();
        o.setId_objeto(dbObjeto.getObjeto()); // Usando objeto como ID
        o.setObjeto(dbObjeto.getObjeto());
        o.setPrecio(dbObjeto.getPrecio());
        o.setImagen(dbObjeto.getImagen());
        o.setDescripcion(dbObjeto.getDescripcion());

        // Convertir categoría string a enum
        o.setCategoria(stringToCategoria(dbObjeto.getCategoria()));

        return o;
    }

    @Override
    public List<Objeto> getAllProductos() {
        try {
            List<Objeto> resultObjetos = new ArrayList<>();
            List<edu.upc.dsa.models.Objeto> dbObjetos = tiendaDAO.listarObjetosTienda();

            for (edu.upc.dsa.models.Objeto dbObjeto : dbObjetos) {
                resultObjetos.add(convertirObjetoDBaObjeto(dbObjeto));
            }

            this.mensajeResultado = tiendaDAO.getMensajeResultado();
            return resultObjetos;
        } catch (Exception e) {
            this.mensajeResultado = "Error al obtener productos: " + e.getMessage();
            logger.error("Error al obtener productos", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Objeto> getProductosPorCategoria(CategoriaObjeto categoria) {
        // Como no hay un método directo para esto en el DAO, filtramos de todos los productos
        List<Objeto> todosProductos = getAllProductos();
        List<Objeto> productosFiltrados = new ArrayList<>();

        for (Objeto o : todosProductos) {
            if (o.getCategoria() == categoria) {
                productosFiltrados.add(o);
            }
        }

        this.mensajeResultado = "Productos filtrados por categoría: " + categoria;
        return productosFiltrados;
    }

    @Override
    public Objeto getProductoPorId(String id_producto) {
        try {
            edu.upc.dsa.models.Objeto dbObjeto = tiendaDAO.obtenerObjeto(id_producto);
            this.mensajeResultado = tiendaDAO.getMensajeResultado();

            if (dbObjeto == null) {
                return null;
            }

            return convertirObjetoDBaObjeto(dbObjeto);
        } catch (Exception e) {
            this.mensajeResultado = "Error al obtener producto por ID: " + e.getMessage();
            logger.error("Error al obtener producto por ID", e);
            return null;
        }
    }

    @Override
    public Objeto getProductoAleatorio() {
        List<Objeto> productos = getAllProductos();
        if (productos.isEmpty()) {
            this.mensajeResultado = "No hay productos disponibles";
            logger.warn("No hay productos disponibles para seleccionar uno aleatorio");
            return null;
        }

        Random rand = new Random();
        int index = rand.nextInt(productos.size());
        Objeto productoAleatorio = productos.get(index);
        this.mensajeResultado = "Producto aleatorio seleccionado: " + productoAleatorio.getObjeto();
        logger.info("Producto aleatorio seleccionado: " + productoAleatorio.getObjeto());
        return productoAleatorio;
    }

    @Override
    public Objeto addProducto(Objeto producto) {
        try {
            // Convertir enum a string para la base de datos
            String categoriaStr = producto.getCategoria() != null ?
                    producto.getCategoria().name() : null;

            boolean resultado = tiendaDAO.añadirObjetoTienda(
                    producto.getObjeto(),
                    categoriaStr,
                    producto.getPrecio()
            );

            this.mensajeResultado = tiendaDAO.getMensajeResultado();

            if (resultado) {
                logger.info("Producto añadido: " + producto.getObjeto());
                return producto;
            } else {
                logger.error("Error al añadir producto: " + this.mensajeResultado);
                return null;
            }
        } catch (Exception e) {
            this.mensajeResultado = "Error al añadir producto: " + e.getMessage();
            logger.error("Error al añadir producto", e);
            return null;
        }
    }

    @Override
    public Objeto addProducto(String id_objeto, String nombre, int precio, String imagen, String descripcion, CategoriaObjeto categoria) {
        Objeto o = new Objeto();
        o.setId_objeto(id_objeto);
        o.setObjeto(nombre);
        o.setPrecio(precio);
        o.setImagen(imagen);
        o.setDescripcion(descripcion);
        o.setCategoria(categoria);
        return addProducto(o);
    }

    @Override
    public void deleteProducto(String id_producto) {
        // No hay un método directo para eliminar productos en el DAO
        logger.warn("deleteProducto: Funcionalidad no disponible con la integración de base de datos");
        this.mensajeResultado = "Funcionalidad no disponible con la integración de base de datos";
    }

    @Override
    public void clear() {
        logger.warn("clear: Funcionalidad no disponible con la integración de base de datos");
        this.mensajeResultado = "Funcionalidad no disponible con la integración de base de datos";
    }

    @Override
    public int sizeProductos() {
        try {
            List<edu.upc.dsa.models.Objeto> objetos = tiendaDAO.listarObjetosTienda();
            this.mensajeResultado = "Número de productos obtenido correctamente";
            return objetos.size();
        } catch (Exception e) {
            this.mensajeResultado = "Error al obtener número de productos: " + e.getMessage();
            logger.error("Error al obtener número de productos", e);
            return 0;
        }
    }

    @Override
    public String getMensajeResultado() {
        return this.mensajeResultado;
    }
}