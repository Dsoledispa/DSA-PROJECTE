package edu.upc.dsa.manager;

import edu.upc.dsa.db.orm.dao.CarritoDAO;
import edu.upc.dsa.db.orm.dao.CarritoDAOImpl;
import edu.upc.dsa.db.orm.dao.ObjetoDAO;
import edu.upc.dsa.db.orm.dao.ObjetoDAOImpl;
import edu.upc.dsa.db.orm.dao.PartidaDAO;
import edu.upc.dsa.db.orm.dao.PartidaDAOImpl;
import edu.upc.dsa.models.Carrito;
import edu.upc.dsa.models.Objeto;
import edu.upc.dsa.models.Partida;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CarritoManagerImpl implements CarritoManager {

    final static Logger logger = Logger.getLogger(CarritoManagerImpl.class);
    private final CarritoDAO carritoDAO;
    private final ObjetoDAO objetoDAO;
    private final PartidaDAO partidaDAO;

    public CarritoManagerImpl() {
        this.carritoDAO = new CarritoDAOImpl();
        this.objetoDAO = new ObjetoDAOImpl();
        this.partidaDAO = new PartidaDAOImpl();
    }

    @Override
    public Carrito agregarObjetoACarrito(String id_partida, String id_objeto) {
        Carrito carrito = new Carrito(null, id_partida, id_objeto);
        int result = carritoDAO.addCarrito(carrito);
        if (result == 1) {
            logger.info("Objeto " + id_objeto + " añadido al carrito de partida " + id_partida);
            return carrito;
        } else {
            logger.warn("No se pudo añadir el objeto " + id_objeto + " al carrito de partida " + id_partida);
            return null;
        }
    }

    @Override
    public boolean eliminarObjetoDeCarrito(String id_partida, String id_objeto) {
        boolean success = carritoDAO.deleteByPartidaAndObjeto(id_partida, id_objeto);
        if (success) {
            logger.info("Objeto " + id_objeto + " eliminado del carrito de partida " + id_partida);
        } else {
            logger.warn("No se pudo eliminar el objeto " + id_objeto + " del carrito de partida " + id_partida);
        }
        return success;
    }

    @Override
    public List<Objeto> getObjetosDelCarrito(String id_partida) {
        List<Carrito> registros = carritoDAO.getCarritoByPartida(id_partida);
        List<Objeto> objetos = new ArrayList<>();
        for (Carrito c : registros) {
            Objeto obj = objetoDAO.getObjeto(c.getId_objeto());
            if (obj != null) {
                objetos.add(obj);
            }
        }
        logger.info("Objetos del carrito de partida " + id_partida + ": " + objetos);
        return objetos;
    }

    @Override
    public int getTotalCarrito(String id_partida) {
        List<Objeto> objetos = getObjetosDelCarrito(id_partida);
        int total = objetos.stream().mapToInt(Objeto::getPrecio).sum();
        logger.info("Total del carrito de partida " + id_partida + ": " + total);
        return total;
    }

    @Override
    public boolean realizarCompra(String id_partida) {
        Partida partida = partidaDAO.getPartida(id_partida);
        if (partida == null) {
            logger.warn("Partida no encontrada: " + id_partida);
            return false;
        }

        List<Objeto> objetos = getObjetosDelCarrito(id_partida);
        int total = objetos.stream().mapToInt(Objeto::getPrecio).sum();

        if (partida.getMonedas() < total) {
            logger.info("Fondos insuficientes para compra en partida " + id_partida + ": " + partida.getMonedas() + " < " + total);
            return false;
        }

        // Realizar la compra
        partida.setMonedas(partida.getMonedas() - total);
        partida.setInventario(objetos);
        logger.info("Que hay en partida: "+partida);
        partidaDAO.updatePartida(partida);
        carritoDAO.deleteAllFromPartida(id_partida);

        logger.info("Compra realizada para partida " + id_partida + ". Monedas restantes: " + partida.getMonedas());
        return true;
    }

    @Override
    public void vaciarCarrito(String id_partida) {
        carritoDAO.deleteAllFromPartida(id_partida);
        logger.info("Carrito vaciado para partida " + id_partida);
    }
}
