package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.models.CategoriaObjeto;

import java.util.List;

public interface CatObjetoDAO {
    // Añade una categoría y devuelve int (1 éxito, 0 fallo)
    int addCategoriaObjeto(CategoriaObjeto categoria);

    // Obtiene una categoría por id
    CategoriaObjeto getCategoriaObjeto(String id_categoria);

    // Actualiza una categoría por id
    void updateCategoriaObjeto(CategoriaObjeto categoria);

    // Borra una categoría por id
    void deleteCategoriaObjeto(String id_categoria);

    // Lista todas las categorías
    List<CategoriaObjeto> getCategoriasObjeto();
}