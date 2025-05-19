package edu.upc.dsa.manager;

import edu.upc.dsa.models.CategoriaObjeto;
import edu.upc.dsa.models.Objeto;

import java.util.List;

public interface TiendaManager {
    // Métodos originales
    List<Objeto> getAllProductos();
    List<Objeto> getProductosPorCategoria(CategoriaObjeto categoria);
    Objeto getProductoPorId(String id_producto);
    Objeto getProductoAleatorio();
    Objeto addProducto(Objeto producto);
    Objeto addProducto(String id_objeto, String nombre, int precio, String imagen, String descripcion, CategoriaObjeto categoria);
    void deleteProducto(String id_producto);
    void clear();
    int sizeProductos();

    // Métodos adicionales para alinearse con ITiendaDAO
    String getMensajeResultado();
}