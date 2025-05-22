package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.models.CategoriaObjeto;

import java.util.List;

public interface ICatObjetoDAO {
    // Añade una categoría y devuelve int (1 éxito, 0 fallo)
    int addCategoriaObjeto(String id_categoria, String nombre);

    // Obtiene una categoría por id
    CategoriaObjeto getCategoriaObjeto(String id_categoria);

    // Actualiza una categoría por id
    void updateCategoriaObjeto(String id_categoria, String nombre);

    // Borra una categoría por id
    void deleteCategoriaObjeto(String id_categoria);

    // Lista todas las categorías
    List<CategoriaObjeto> getCategoriasObjeto();
}