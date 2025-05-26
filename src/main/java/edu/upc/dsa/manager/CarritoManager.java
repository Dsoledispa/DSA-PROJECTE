package edu.upc.dsa.manager;

import edu.upc.dsa.models.Carrito;
import edu.upc.dsa.models.Objeto;

import java.util.List;

public interface CarritoManager {

    // Agrega un objeto al carrito de una partida
    Carrito agregarObjetoACarrito(String id_partida, String id_objeto);

    // Elimina un objeto específico del carrito de una partida
    boolean eliminarObjetoDeCarrito(String id_partida, String id_objeto);

    // Obtiene todos los objetos del carrito de una partida
    List<Objeto> getObjetosDelCarrito(String id_partida);

    // Realiza la compra: transfiere objetos al inventario si hay suficiente dinero y vacía el carrito
    boolean realizarCompra(String id_partida);

    // Vacía el carrito de una partida sin realizar compra (opcional)
    void vaciarCarrito(String id_partida);

    // Devuelve el coste total del carrito de una partida
    int getTotalCarrito(String id_partida);

}
