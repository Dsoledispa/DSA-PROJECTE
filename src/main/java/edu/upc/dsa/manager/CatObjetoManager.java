package edu.upc.dsa.manager;

import edu.upc.dsa.models.CategoriaObjeto;

import java.util.List;

public interface CatObjetoManager {

    // Obtiene una categoría por su ID
    CategoriaObjeto getCatObjeto(int id_categoria);

    // Devuelve todas las categorías
    List<CategoriaObjeto> getAllCatObjeto();

    // Añade una nueva categoría
    CategoriaObjeto addCatObjeto(CategoriaObjeto categoria);
    CategoriaObjeto addCatObjeto(int id_categoria, String nombre);

    // Elimina una categoría por su ID
    void deleteCatObjeto(int id_categoria);

    // Devuelve el número de categorías
    int sizeCatObjeto();

    // Limpiar las categorias (para tests)
    void clear();
}