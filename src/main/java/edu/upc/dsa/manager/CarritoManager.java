//package edu.upc.dsa.manager;
//
//import edu.upc.dsa.models.Carrito;
//import edu.upc.dsa.models.Objeto;
//
//import java.util.List;
//
//public interface CarritoManager {
//
//    // Obtiene el carrito de un usuario
//    Carrito getCarrito(String id_usuario);
//
//    // Agrega un producto al carrito de un usuario
//    void agregarProductoAlCarrito(String id_usuario, Objeto producto);
//
//    // Elimina un producto del carrito de un usuario
//    void eliminarProductoDelCarrito(String id_usuario, String id_producto);
//
//    // Obtiene todos los productos del carrito de un usuario
//    List<Objeto> getProductosDelCarrito(String id_usuario);
//
//    // Realiza la compra y vacía el carrito (puedes agregar más validaciones aquí)
//    boolean realizarCompra(String id_usuario, String id_partida);
//
//    // Limpiar todos los carritos (opcional para tests)
//    void clear();
//
//}
