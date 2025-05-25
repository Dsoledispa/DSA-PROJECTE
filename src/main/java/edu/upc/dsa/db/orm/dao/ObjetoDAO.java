package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.models.Objeto;

import java.util.List;

public interface ObjetoDAO {

    // Añadir un objeto (devuelve 1 si se ha guardado correctamente, 0 si ha fallado)
    int addObjeto(Objeto objeto);

    // Obtener un objeto por su ID
    Objeto getObjeto(String id_objeto);

    // Actualizar un objeto
    void updateObjeto(Objeto objeto);

    // Eliminar un objeto por su ID
    void deleteObjeto(String id_objeto);

    // Obtener todos los objetos
    List<Objeto> getObjetos();

    // Obtener objetos por id de categoría
    List<Objeto> getObjetosByCategoria(String id_categoria);
}