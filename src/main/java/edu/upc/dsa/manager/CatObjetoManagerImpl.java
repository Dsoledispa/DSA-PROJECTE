package edu.upc.dsa.manager;

import edu.upc.dsa.db.orm.dao.CatObjetoDAO;
import edu.upc.dsa.db.orm.dao.CatObjetoDAOImpl;
import edu.upc.dsa.exceptions.categoriaObjeto.CatObjetoYaExisteException;
import edu.upc.dsa.models.CategoriaObjeto;
import org.apache.log4j.Logger;

import java.util.List;


public class CatObjetoManagerImpl implements CatObjetoManager {

    final static Logger logger = Logger.getLogger(CatObjetoManagerImpl.class);

    private CatObjetoDAO catObjetoDAO;

    public CatObjetoManagerImpl() {
        this.catObjetoDAO = new CatObjetoDAOImpl(); // o puedes inyectarlo si deseas
    }

    @Override
    public CategoriaObjeto getCatObjeto(String id_categoria) {
        CategoriaObjeto cat = catObjetoDAO.getCategoriaObjeto(id_categoria);
        logger.info("getCatObjeto con id=" + id_categoria + ": " + cat);
        return cat;
    }

    @Override
    public List<CategoriaObjeto> getAllCatObjeto() {
        List<CategoriaObjeto> categorias = catObjetoDAO.getCategoriasObjeto();
        logger.info("getAllCatObjeto: " + categorias);
        return categorias;
    }

    @Override
    public CategoriaObjeto addCatObjeto(CategoriaObjeto categoria) {
        boolean yaExiste = catObjetoDAO.getCategoriasObjeto().stream()
                .anyMatch(c -> c.getNombre().equalsIgnoreCase(categoria.getNombre()));

        if (yaExiste) {
            logger.warn("Ya existe la categoría con nombre='" + categoria.getNombre() + "'");
            throw new CatObjetoYaExisteException(categoria.getNombre());
        }

        int result = catObjetoDAO.addCategoriaObjeto(categoria.getId_categoria(), categoria.getNombre());
        if (result == 1) {
            logger.info("addCatObjeto: " + categoria);
            return categoria;
        } else {
            logger.error("Error al añadir la categoría: " + categoria);
            return null;
        }
    }

    @Override
    public CategoriaObjeto addCatObjeto(String id_categoria, String nombre) {
        return this.addCatObjeto(new CategoriaObjeto(id_categoria, nombre));
    }

    public void updateCatObjeto(String id_categoria, String nuevoNombre) {
        CategoriaObjeto categoria = catObjetoDAO.getCategoriaObjeto(id_categoria);

        if (categoria == null) {
            logger.warn("No se encontró la categoría con id='" + id_categoria + "' para actualizar");
            return;
        }

        // Verificamos que no haya otra categoría con el mismo nombre
        boolean yaExiste = catObjetoDAO.getCategoriasObjeto().stream()
                .anyMatch(c -> !c.getId_categoria().equals(id_categoria) && c.getNombre().equalsIgnoreCase(nuevoNombre));

        if (yaExiste) {
            logger.warn("Ya existe otra categoría con nombre='" + nuevoNombre + "', no se puede actualizar");
            throw new CatObjetoYaExisteException(nuevoNombre);
        }

        catObjetoDAO.updateCategoriaObjeto(id_categoria, nuevoNombre);
        logger.info("updateCatObjeto: categoría con id='" + id_categoria + "' actualizada a nombre='" + nuevoNombre + "'");
    }

    @Override
    public void deleteCatObjeto(String id_categoria) {
        catObjetoDAO.deleteCategoriaObjeto(id_categoria);
        logger.info("deleteCatObjeto con id=" + id_categoria);
    }

    @Override
    public int sizeCatObjeto() {
        int size = catObjetoDAO.getCategoriasObjeto().size();
        logger.info("sizeCatObjeto: " + size);
        return size;
    }
}

