package edu.upc.dsa.manager;

import edu.upc.dsa.exceptions.categoriaObjeto.CatObjetoYaExisteException;
import edu.upc.dsa.models.CategoriaObjeto;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CatObjetoManagerImpl implements CatObjetoManager {

    private static CatObjetoManager instance;
    protected List<CategoriaObjeto> categorias;
    final static Logger logger = Logger.getLogger(CatObjetoManagerImpl.class);

    private CatObjetoManagerImpl() {
        this.categorias = new ArrayList<>();
    }

    // Patrón singleton
    public static CatObjetoManager getInstance() {
        if (instance == null) instance = new CatObjetoManagerImpl();
        return instance;
    }

    @Override
    public CategoriaObjeto getCatObjeto(int id_categoria) {
        CategoriaObjeto cat = this.categorias.stream()
                .filter(c -> c.getId_categoria() == id_categoria)
                .findFirst()
                .orElse(null);
        logger.info("getCatObjeto con id=" + id_categoria + ": " + cat);
        return cat;
    }

    @Override
    public List<CategoriaObjeto> getAllCatObjeto() {
        logger.info("getAllCatObjeto: " + this.categorias);
        return this.categorias;
    }

    @Override
    public CategoriaObjeto addCatObjeto(CategoriaObjeto categoria) {
        // Buscar si ya existe una categoría con ese nombre (ignorando mayúsculas)
        boolean yaExiste = this.getAllCatObjeto().stream()
                .anyMatch(c -> c.getNombre().equalsIgnoreCase(categoria.getNombre()));

        if (yaExiste) {
            logger.warn("Ya existe la categoría con nombre='" + categoria.getNombre() + "'");
            throw new CatObjetoYaExisteException(categoria.getNombre());
        }

        this.categorias.add(categoria);
        logger.info("addCatObjeto: " + categoria);
        return categoria;
    }


    @Override
    public CategoriaObjeto addCatObjeto(int id_categoria, String nombre) {
        return this.addCatObjeto(new CategoriaObjeto(id_categoria, nombre));
    }

    @Override
    public void deleteCatObjeto(int id_categoria) {
        this.categorias.removeIf(c -> c.getId_categoria() == id_categoria);
        logger.info("deleteCatObjeto con id=" + id_categoria);
    }

    @Override
    public int sizeCatObjeto() {
        logger.info(categorias.size());
        return this.categorias.size();
    }

    @Override
    public void clear() {
        this.categorias.clear();
        logger.info("categorias limpiadas");
    }
}

