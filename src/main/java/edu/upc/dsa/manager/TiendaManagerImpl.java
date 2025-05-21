//package edu.upc.dsa.manager;
//
//import edu.upc.dsa.models.Objeto;
//import org.apache.log4j.Logger;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.stream.Collectors;
//
//public class TiendaManagerImpl implements TiendaManager{
//
//    private static TiendaManager instance;
//    protected List<Objeto> productos;
//    final static Logger logger = Logger.getLogger(TiendaManagerImpl.class);
//
//    private TiendaManagerImpl() {
//        this.productos = new ArrayList<>();
//    }
//
//    // Patron singleton
//    public static TiendaManager getInstance() {
//        if (instance == null) instance = new TiendaManagerImpl();
//        return instance;
//    }
//
//    @Override
//    public List<Objeto> getAllProductos() {
//        List<Objeto> productos = this.productos;
//        logger.info("Productos "+ productos);
//        return productos;
//    }
//
//    @Override
//    public List<Objeto> getProductosPorCategoria(CategoriaObjeto categoria) {
//        List<Objeto> ProductosCategorias = this.productos.stream()
//                .filter(p -> p.getCategoria() == categoria)
//                .collect(Collectors.toList());
//        logger.info("Productos por categorias " + ProductosCategorias);
//        return ProductosCategorias;
//    }
//
//    @Override
//    public Objeto getProductoPorId(String id_producto) {
//        Objeto Producto = this.productos.stream()
//                .filter(p -> p.getId_objeto().equals(id_producto))
//                .findFirst()
//                .orElse(null);
//        logger.info("Producto por id_producto:" + id_producto + " es " + Producto);
//        return Producto;
//    }
//
//    @Override
//    public Objeto getProductoAleatorio() {
//        if (productos.isEmpty()) {
//            logger.warn("No hay productos disponibles para seleccionar uno aleatorio.");
//            return null;
//        }
//        Random rand = new Random();
//        int index = rand.nextInt(productos.size());
//        Objeto productoAleatorio = productos.get(index);
//        logger.info("Producto aleatorio seleccionado: " + productoAleatorio);
//        return productoAleatorio;
//
//    }
//
//    @Override
//    public Objeto addProducto(Objeto producto) {
//        this.productos.add(producto);
//        return producto;
//    }
//
//    @Override
//    public Objeto addProducto(String id_objeto, String nombre, int precio, String imagen, String descripcion, CategoriaObjeto categoria) {
//        return this.addProducto(new Objeto(id_objeto, nombre, precio, imagen, descripcion, categoria));
//    }
//
//    @Override
//    public void deleteProducto(String id_producto) {
//        this.productos.removeIf(p -> p.getId_objeto().equals(id_producto));
//    }
//
//    @Override
//    public void clear() {
//        this.productos.clear();
//    }
//
//    @Override
//    public int sizeProductos() {
//        List<Objeto> productos = this.productos;
//        //logger.info(productos.size());
//        return productos.size();
//    }
//}
