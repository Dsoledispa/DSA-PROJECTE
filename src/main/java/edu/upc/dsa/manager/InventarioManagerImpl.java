package edu.upc.dsa.manager;

import edu.upc.dsa.db.orm.dao.*;
import edu.upc.dsa.models.CategoriaObjeto;
import edu.upc.dsa.models.Inventario;
import edu.upc.dsa.models.Objeto;
import edu.upc.dsa.models.Partida;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class InventarioManagerImpl implements InventarioManager {

    final static Logger logger = Logger.getLogger(InventarioManagerImpl.class);

    private TiendaManager tm;
    private PartidaManager pm;
    private final InventarioDAO inventarioDAO;

    public InventarioManagerImpl() {
        this.tm = new TiendaManagerImpl();
        this.pm = new PartidaManagerImpl();
        this.inventarioDAO = new InventarioDAOImpl();
    }

    @Override
    public List<Objeto> getInventarioDePartida(String id_partida) {
        List<Inventario> registros = inventarioDAO.getInventarioByPartida(id_partida);
        List<Objeto> objetos = new ArrayList<>();
        for (Inventario inv : registros) {
            logger.info("Que tenemos aqui : " +inv);
            Objeto obj = tm.getProductoPorId(inv.getId_objeto());
            if (obj != null) {
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
    public boolean PagarYGuardarObjetoInventario(String id_usuario, String id_partida, String id_objeto) {
        Partida partida = pm.getPartida(id_usuario, id_partida);
        if (partida == null) {
            logger.warn("Partida no encontrada: " + id_partida);
            return false;
        }

        Objeto objeto = tm.getProductoPorId(id_objeto);
        if (partida.getMonedas() < objeto.getPrecio()) {
            logger.info("Fondos insuficientes para compra en partida " + id_partida + ": " + partida.getMonedas() + " < " + objeto.getPrecio());
            return false;
        }

        // Realizar la compra
        partida.setMonedas(partida.getMonedas() - objeto.getPrecio());
        agregarObjetoAInventario(id_partida, id_objeto);
        pm.updatePartida(partida);

        logger.info("Compra realizada para partida " + id_partida + ". Monedas restantes: " + partida.getMonedas());
        return true;
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
