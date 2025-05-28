package edu.upc.dsa.manager;

import edu.upc.dsa.db.orm.dao.ObjetoDAO;
import edu.upc.dsa.db.orm.dao.ObjetoDAOImpl;
import edu.upc.dsa.models.CategoriaObjeto;
import edu.upc.dsa.models.Objeto;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Random;

public class TiendaManagerImpl implements TiendaManager {

    final static Logger logger = Logger.getLogger(TiendaManagerImpl.class);
    private CatObjetoManager com;
    private final ObjetoDAO objetoDAO;

    public TiendaManagerImpl() {
        this.com = new CatObjetoManagerImpl();
        this.objetoDAO = new ObjetoDAOImpl();
    }

    @Override
    public List<Objeto> getAllProductos() {
        List<Objeto> productos = objetoDAO.getObjetos();
        for (Objeto producto : productos) {
            String id_categoria = producto.getCategoria().getId_categoria();
            CategoriaObjeto catObjeto = com.getCatObjeto(id_categoria);
            producto.setCategoria(catObjeto);
        }
        logger.info("Productos: " + productos);
        return productos;
    }

    @Override
    public List<Objeto> getProductosPorCategoria(String id_categoria) {
        List<Objeto> productos = objetoDAO.getObjetosByCategoria(id_categoria);
        for (Objeto producto : productos) {
            CategoriaObjeto catObjeto = com.getCatObjeto(id_categoria);
            producto.setCategoria(catObjeto);
        }
        logger.info("Productos por categoría (" + id_categoria + "): " + productos);
        return productos;
    }

    @Override
    public Objeto getProductoPorId(String id_producto) {
        Objeto producto = objetoDAO.getObjeto(id_producto);

        //Inserto los datos de categoria en el producto
        String id_categoria = producto.getCategoria().getId_categoria();
        CategoriaObjeto catObjeto = com.getCatObjeto(id_categoria);
        producto.setCategoria(catObjeto);

        logger.info("Producto por id_producto: " + id_producto + " es " + producto);
        return producto;
    }

    @Override
    public Objeto getProductoAleatorio() {
        List<Objeto> productos = objetoDAO.getObjetos();
        if (productos.isEmpty()) {
            logger.warn("No hay productos disponibles para seleccionar uno aleatorio.");
            return null;
        }
        Random rand = new Random();
        Objeto productoAleatorio = productos.get(rand.nextInt(productos.size()));

        //Inserto los datos de categoria en el productoAleatorio
        String id_categoria = productoAleatorio.getCategoria().getId_categoria();
        CategoriaObjeto catObjeto = com.getCatObjeto(id_categoria);
        productoAleatorio.setCategoria(catObjeto);

        logger.info("Producto aleatorio seleccionado: " + productoAleatorio);
        return productoAleatorio;
    }

    @Override
    public Objeto addProducto(Objeto producto) {
        int result = objetoDAO.addObjeto(producto);
        if (result == 1) {
            logger.info("Producto añadido correctamente: " + producto);
            return producto;
        } else {
            logger.warn("No se pudo añadir el producto: " + producto);
            return null;
        }
    }

    @Override
    public Objeto addProducto(String id_objeto, String nombre, int precio, String imagen, String descripcion, CategoriaObjeto categoria) {
        Objeto producto = new Objeto(id_objeto, nombre, precio, imagen, descripcion, categoria);
        return this.addProducto(producto);
    }

    @Override
    public void updateProducto(Objeto producto) {
        objetoDAO.updateObjeto(producto);
        logger.info("Producto actualizado: " + producto);
    }

    @Override
    public void deleteProducto(String id_producto) {
        objetoDAO.deleteObjeto(id_producto);
        logger.info("Producto eliminado: " + id_producto);
    }

    @Override
    public int sizeProductos() {
        int size = objetoDAO.getObjetos().size();
        logger.info("Número total de productos: " + size);
        return size;
    }
}
