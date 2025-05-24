package edu.upc.dsa.manager;

import edu.upc.dsa.models.CategoriaObjeto;

import java.util.List;


public interface CatObjetoManager {

    // Obtiene una categoría por su ID
    CategoriaObjeto getCatObjeto(String id_categoria);

    // Devuelve todas las categorías
    List<CategoriaObjeto> getAllCatObjeto();

    // Añade una nueva categoría
    CategoriaObjeto addCatObjeto(CategoriaObjeto categoria);
    CategoriaObjeto addCatObjeto(String id_categoria, String nombre);

    // Actualiza el nombre de una categoría por su ID
    void updateCatObjeto(String id_categoria, String nuevoNombre);

    // Elimina una categoría por su ID
    void deleteCatObjeto(String id_categoria);

    // Devuelve el número de categorías
    int sizeCatObjeto();
}