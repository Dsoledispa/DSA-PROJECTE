package edu.upc.dsa.manager;

import edu.upc.dsa.models.Carrito;
import edu.upc.dsa.models.Objeto;

import java.util.List;

public interface CarritoManager {
    Carrito getCarrito(String id_usuario);
    void agregarProductoAlCarrito(String id_usuario, Objeto producto);
    void eliminarProductoDelCarrito(String id_usuario, String id_producto);
    List<Objeto> getProductosDelCarrito(String id_usuario);
    boolean realizarCompra(String id_usuario, String id_partida);
    void clear();
    String getMensajeResultado();
}