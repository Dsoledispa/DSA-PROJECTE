package edu.upc.dsa.manager;

import edu.upc.dsa.db.orm.dao.*;
import edu.upc.dsa.models.CategoriaObjeto;
import edu.upc.dsa.models.Inventario;
import edu.upc.dsa.models.Objeto;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class InventarioManagerImpl implements InventarioManager {

    final static Logger logger = Logger.getLogger(InventarioManagerImpl.class);

    private CatObjetoDAO catObjetoDAO;
    private final InventarioDAO inventarioDAO;
    private final ObjetoDAO objetoDAO;

    public InventarioManagerImpl() {
        this.inventarioDAO = new InventarioDAOImpl();
        this.objetoDAO = new ObjetoDAOImpl();
        this.catObjetoDAO = new CatObjetoDAOImpl();
    }

    @Override
    public List<Objeto> getInventarioDePartida(String id_partida) {
        List<Inventario> registros = inventarioDAO.getInventarioByPartida(id_partida);
        List<Objeto> objetos = new ArrayList<>();
        for (Inventario inv : registros) {
            logger.info("Que tenemos aqui : " +inv);
            Objeto obj = objetoDAO.getObjeto(inv.getId_objeto());
            if (obj != null) {
                String id_categoria = obj.getCategoria().getId_categoria();
                CategoriaObjeto catObjeto = catObjetoDAO.getCategoriaObjeto(id_categoria);
                obj.setCategoria(catObjeto);
                objetos.add(obj);
            }
        }
        logger.info("Inventario de partida " + id_partida + ": " + objetos);
        return objetos;
    }

    @Override
    public boolean agregarObjetoAInventario(String id_partida, String id_objeto) {
        Inventario inventario = new Inventario(null, id_partida, id_objeto);
        int result = inventarioDAO.addInventario(inventario);
        if (result == 1) {
            logger.info("Objeto " + id_objeto + " añadido al inventario de partida " + id_partida);
            return true;
        } else {
            logger.warn("No se pudo añadir el objeto " + id_objeto + " al inventario de partida " + id_partida);
            return false;
        }
    }

    @Override
    public boolean eliminarObjetoDeInventario(String id_partida, String id_objeto) {
        boolean success = inventarioDAO.deleteByPartidaAndObjeto(id_partida, id_objeto);
        if (success) {
            logger.info("Objeto " + id_objeto + " eliminado del inventario de partida " + id_partida);
        } else {
            logger.warn("No se pudo eliminar el objeto " + id_objeto + " del inventario de partida " + id_partida);
        }
        return success;
    }

    @Override
    public void eliminarAllObjetosDeInventario(String id_partida) {
        inventarioDAO.deleteAllFromPartida(id_partida);
        logger.info("Inventario eliminado correctamente para la partida " + id_partida);
    }
}
