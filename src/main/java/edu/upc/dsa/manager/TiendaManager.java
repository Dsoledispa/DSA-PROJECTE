package edu.upc.dsa.manager;

import edu.upc.dsa.models.CategoriaObjeto;
import edu.upc.dsa.models.Objeto;

import java.util.List;

public interface TiendaManager {

    // Obtiene todos los productos disponibles en la tienda
    List<Objeto> getAllProductos();

    // Obtiene los productos de una categoría específica
    List<Objeto> getProductosPorCategoria(String id_categoria);

    // Obtiene un producto por su ID
    Objeto getProductoPorId(String id_producto);

    // Obtiene un objeto aleatorio
    Objeto getProductoAleatorio();

    // Agrega un nuevo producto a la tienda
    Objeto addProducto(Objeto producto);
    Objeto addProducto(String id_objeto, String nombre, int precio, String imagen, String descripcion, CategoriaObjeto categoria);

    // Actualiza un producto de la tienda
    void updateProducto(Objeto producto);

    // Elimina un producto de la tienda
    void deleteProducto(String id_producto);

    int sizeProductos();

}
