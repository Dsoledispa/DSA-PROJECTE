package edu.upc.dsa.manager;

import edu.upc.dsa.db.orm.dao.IPartidaDAO;
import edu.upc.dsa.db.orm.dao.PartidaDAOImpl;
import edu.upc.dsa.exceptions.partida.PartidaYaExisteException;
import edu.upc.dsa.exceptions.usuario.UsuarioNotFoundException;
import edu.upc.dsa.models.CategoriaObjeto;
import edu.upc.dsa.models.Objeto;
import edu.upc.dsa.models.Partida;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class PartidaManagerImpl implements PartidaManager {
    private static PartidaManager instance;
    private final IPartidaDAO partidaDAO;
    final static Logger logger = Logger.getLogger(PartidaManagerImpl.class);
    private String mensajeResultado;

    private PartidaManagerImpl() {
        this.partidaDAO = new PartidaDAOImpl();
    }

    // Patrón singleton
    public static PartidaManager getInstance() {
        if (instance == null) instance = new PartidaManagerImpl();
        return instance;
    }

    // Método auxiliar para convertir string a enum CategoriaObjeto
    private CategoriaObjeto stringToCategoria(String categoriaStr) {
        if (categoriaStr == null) return null;
        try {
            return CategoriaObjeto.valueOf(categoriaStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Categoría no reconocida: " + categoriaStr);
            return null;
        }
    }

    // Método auxiliar para convertir modelos de DB a modelos de negocio
    private Objeto convertirObjetoDBaObjeto(edu.upc.dsa.models.Objeto dbObjeto) {
        if (dbObjeto == null) return null;

        Objeto o = new Objeto();
        o.setId_objeto(dbObjeto.getObjeto()); // Usando objeto como ID
        o.setObjeto(dbObjeto.getObjeto());
        o.setPrecio(dbObjeto.getPrecio());
        o.setImagen(dbObjeto.getImagen());
        o.setDescripcion(dbObjeto.getDescripcion());

        // Convertir categoría string a enum
        o.setCategoria(stringToCategoria(dbObjeto.getCategoria()));

        return o;
    }

    @Override
    public Partida addPartida(Partida p) {
        try {
            // Transformamos nuestra Partida a lo que espera el DAO
            int idPartida = partidaDAO.crearPartida(
                    p.getId_usuario(),
                    p.getVidas(),
                    p.getMonedas(),
                    p.getPuntuacion()
            );

            if (idPartida > 0) {
                p.setId_partida(String.valueOf(idPartida));
                this.mensajeResultado = partidaDAO.getMensajeResultado();
                logger.info("Nueva partida creada con ID: " + idPartida);
                return p;
            } else {
                this.mensajeResultado = partidaDAO.getMensajeResultado();
                logger.error("Error al crear partida");
                throw new RuntimeException("Error al crear partida");
            }
        } catch (Exception e) {
            this.mensajeResultado = "Error al crear partida: " + e.getMessage();
            logger.error("Error al crear partida", e);
            throw new RuntimeException("Error al crear partida", e);
        }
    }

    @Override
    public Partida addPartida(String id_partida, String id_usuario, Integer vidas, Integer monedas, Integer puntuacion) {
        Partida p = new Partida(id_partida, id_usuario, vidas, monedas, puntuacion);
        return this.addPartida(p);
    }

    @Override
    public Partida addPartida(String id_usuario) {
        Partida partida = new Partida(null, id_usuario, 3, 100, 0);
        return this.addPartida(partida);
    }

    @Override
    public List<Partida> getPartidas(String id_usuario) {
        try {
            List<Partida> resultPartidas = new ArrayList<>();
            List<edu.upc.dsa.models.Partida> dbPartidas = partidaDAO.obtenerPartidasUsuario(id_usuario);

            for (edu.upc.dsa.models.Partida dbPartida : dbPartidas) {
                Partida p = new Partida();
                p.setId_partida(String.valueOf(dbPartida.getId_partida()));
                p.setId_usuario(dbPartida.getId_usuario());
                p.setVidas(dbPartida.getVidas());
                p.setMonedas(dbPartida.getMonedas());
                p.setPuntuacion(dbPartida.getPuntuacion());

                // Cargar inventario
                List<Objeto> inventario = new ArrayList<>();
                for (edu.upc.dsa.models.Objeto dbObjeto : partidaDAO.obtenerInventario(dbPartida.getId_partida())) {
                    inventario.add(convertirObjetoDBaObjeto(dbObjeto));
                }
                p.setInventario(inventario);

                resultPartidas.add(p);
            }

            this.mensajeResultado = "Partidas obtenidas correctamente";
            return resultPartidas;
        } catch (Exception e) {
            this.mensajeResultado = "Error al obtener partidas: " + e.getMessage();
            logger.error("Error al obtener partidas", e);
            throw new RuntimeException("Error al obtener partidas", e);
        }
    }

    @Override
    public Partida getPartida(String id_usuario, String id_partida) {
        try {
            edu.upc.dsa.models.Partida dbPartida = partidaDAO.obtenerPartida(Integer.parseInt(id_partida));

            if (dbPartida == null || !dbPartida.getId_usuario().equals(id_usuario)) {
                this.mensajeResultado = "Partida no encontrada o no pertenece al usuario";
                throw new RuntimeException("Partida no encontrada o no pertenece al usuario");
            }

            Partida p = new Partida();
            p.setId_partida(String.valueOf(dbPartida.getId_partida()));
            p.setId_usuario(dbPartida.getId_usuario());
            p.setVidas(dbPartida.getVidas());
            p.setMonedas(dbPartida.getMonedas());
            p.setPuntuacion(dbPartida.getPuntuacion());

            // Cargar inventario
            List<Objeto> inventario = new ArrayList<>();
            for (edu.upc.dsa.models.Objeto dbObjeto : partidaDAO.obtenerInventario(dbPartida.getId_partida())) {
                inventario.add(convertirObjetoDBaObjeto(dbObjeto));
            }
            p.setInventario(inventario);

            this.mensajeResultado = "Partida obtenida correctamente";
            return p;
        } catch (Exception e) {
            this.mensajeResultado = "Error al obtener partida: " + e.getMessage();
            logger.error("Error al obtener partida", e);
            throw new RuntimeException("Error al obtener partida", e);
        }
    }

    @Override
    public void deletePartida(String id_usuario, String id_partida) {
        try {
            boolean eliminado = partidaDAO.eliminarPartida(Integer.parseInt(id_partida));
            if (eliminado) {
                this.mensajeResultado = "Partida eliminada correctamente";
                logger.info("Partida eliminada: " + id_partida);
            } else {
                this.mensajeResultado = partidaDAO.getMensajeResultado();
                logger.warn("No se pudo eliminar la partida " + id_partida);
            }
        } catch (Exception e) {
            this.mensajeResultado = "Error al eliminar partida: " + e.getMessage();
            logger.error("Error al eliminar partida", e);
            throw new RuntimeException("Error al eliminar partida", e);
        }
    }

    @Override
    public int getMonedasDePartida(String id_usuario, String id_partida) {
        try {
            edu.upc.dsa.models.Partida dbPartida = partidaDAO.obtenerPartida(Integer.parseInt(id_partida));

            if (dbPartida == null || !dbPartida.getId_usuario().equals(id_usuario)) {
                this.mensajeResultado = "Partida no encontrada o no pertenece al usuario";
                throw new RuntimeException("Partida no encontrada o no pertenece al usuario");
            }

            this.mensajeResultado = "Monedas obtenidas correctamente";
            return dbPartida.getMonedas();
        } catch (Exception e) {
            this.mensajeResultado = "Error al obtener monedas de partida: " + e.getMessage();
            logger.error("Error al obtener monedas de partida", e);
            throw new RuntimeException("Error al obtener monedas de partida", e);
        }
    }

    @Override
    public void clear() {
        logger.warn("clear: Funcionalidad no disponible con la integración de base de datos");
        this.mensajeResultado = "Funcionalidad no disponible con la integración de base de datos";
    }

    @Override
    public int sizePartidas(String id_usuario) {
        try {
            List<edu.upc.dsa.models.Partida> partidas = partidaDAO.obtenerPartidasUsuario(id_usuario);
            this.mensajeResultado = "Número de partidas obtenido correctamente";
            return partidas.size();
        } catch (Exception e) {
            this.mensajeResultado = "Error al obtener número de partidas: " + e.getMessage();
            logger.error("Error al obtener número de partidas", e);
            return 0;
        }
    }

    @Override
    public boolean actualizarPartida(Partida partida) {
        try {
            // Convertir de modelo de negocio a modelo de DB
            edu.upc.dsa.models.Partida dbPartida = new edu.upc.dsa.models.Partida();
            dbPartida.setId_partida(Integer.parseInt(partida.getId_partida()));
            dbPartida.setId_usuario(partida.getId_usuario());
            dbPartida.setVidas(partida.getVidas());
            dbPartida.setMonedas(partida.getMonedas());
            dbPartida.setPuntuacion(partida.getPuntuacion());

            boolean resultado = partidaDAO.actualizarPartida(dbPartida);
            this.mensajeResultado = partidaDAO.getMensajeResultado();
            return resultado;
        } catch (Exception e) {
            this.mensajeResultado = "Error al actualizar partida: " + e.getMessage();
            logger.error("Error al actualizar partida", e);
            return false;
        }
    }

    @Override
    public boolean actualizarVidas(String id_partida, int vidas) {
        try {
            boolean resultado = partidaDAO.actualizarVidas(Integer.parseInt(id_partida), vidas);
            this.mensajeResultado = partidaDAO.getMensajeResultado();
            return resultado;
        } catch (Exception e) {
            this.mensajeResultado = "Error al actualizar vidas: " + e.getMessage();
            logger.error("Error al actualizar vidas", e);
            return false;
        }
    }

    @Override
    public boolean actualizarMonedas(String id_partida, int monedas) {
        try {
            boolean resultado = partidaDAO.actualizarMonedas(Integer.parseInt(id_partida), monedas);
            this.mensajeResultado = partidaDAO.getMensajeResultado();
            return resultado;
        } catch (Exception e) {
            this.mensajeResultado = "Error al actualizar monedas: " + e.getMessage();
            logger.error("Error al actualizar monedas", e);
            return false;
        }
    }

    @Override
    public boolean actualizarPuntuacion(String id_partida, int puntuacion) {
        try {
            boolean resultado = partidaDAO.actualizarPuntuacion(Integer.parseInt(id_partida), puntuacion);
            this.mensajeResultado = partidaDAO.getMensajeResultado();
            return resultado;
        } catch (Exception e) {
            this.mensajeResultado = "Error al actualizar puntuación: " + e.getMessage();
            logger.error("Error al actualizar puntuación", e);
            return false;
        }
    }

    @Override
    public List<Objeto> obtenerInventario(String id_partida) {
        try {
            List<Objeto> inventario = new ArrayList<>();
            for (edu.upc.dsa.models.Objeto dbObjeto : partidaDAO.obtenerInventario(Integer.parseInt(id_partida))) {
                inventario.add(convertirObjetoDBaObjeto(dbObjeto));
            }
            this.mensajeResultado = "Inventario obtenido correctamente";
            return inventario;
        } catch (Exception e) {
            this.mensajeResultado = "Error al obtener inventario: " + e.getMessage();
            logger.error("Error al obtener inventario", e);
            return new ArrayList<>();
        }
    }

    @Override
    public boolean añadirObjetoInventario(String id_partida, String objeto, int cantidad) {
        try {
            boolean resultado = partidaDAO.añadirObjetoInventario(Integer.parseInt(id_partida), objeto, cantidad);
            this.mensajeResultado = partidaDAO.getMensajeResultado();
            return resultado;
        } catch (Exception e) {
            this.mensajeResultado = "Error al añadir objeto al inventario: " + e.getMessage();
            logger.error("Error al añadir objeto al inventario", e);
            return false;
        }
    }

    @Override
    public boolean eliminarObjetoInventario(String id_partida, String objeto, int cantidad) {
        try {
            boolean resultado = partidaDAO.eliminarObjetoInventario(Integer.parseInt(id_partida), objeto, cantidad);
            this.mensajeResultado = partidaDAO.getMensajeResultado();
            return resultado;
        } catch (Exception e) {
            this.mensajeResultado = "Error al eliminar objeto del inventario: " + e.getMessage();
            logger.error("Error al eliminar objeto del inventario", e);
            return false;
        }
    }

    @Override
    public boolean comprarObjeto(String id_partida, String objeto) {
        try {
            boolean resultado = partidaDAO.comprarObjeto(Integer.parseInt(id_partida), objeto);
            this.mensajeResultado = partidaDAO.getMensajeResultado();
            return resultado;
        } catch (Exception e) {
            this.mensajeResultado = "Error al comprar objeto: " + e.getMessage();
            logger.error("Error al comprar objeto", e);
            return false;
        }
    }

    @Override
    public boolean tieneMonedasSuficientes(String id_partida, String objeto) {
        try {
            boolean resultado = partidaDAO.tieneMonedasSuficientes(Integer.parseInt(id_partida), objeto);
            this.mensajeResultado = partidaDAO.getMensajeResultado();
            return resultado;
        } catch (Exception e) {
            this.mensajeResultado = "Error al verificar monedas: " + e.getMessage();
            logger.error("Error al verificar monedas", e);
            return false;
        }
    }

    @Override
    public boolean usarObjeto(String id_partida, String objeto) {
        try {
            boolean resultado = partidaDAO.usarObjeto(Integer.parseInt(id_partida), objeto);
            this.mensajeResultado = partidaDAO.getMensajeResultado();
            return resultado;
        } catch (Exception e) {
            this.mensajeResultado = "Error al usar objeto: " + e.getMessage();
            logger.error("Error al usar objeto", e);
            return false;
        }
    }

    @Override
    public List<Objeto> verInventario(String id_partida) {
        try {
            List<Objeto> inventario = new ArrayList<>();
            for (edu.upc.dsa.models.Objeto dbObjeto : partidaDAO.verInventario(Integer.parseInt(id_partida))) {
                inventario.add(convertirObjetoDBaObjeto(dbObjeto));
            }
            this.mensajeResultado = partidaDAO.getMensajeResultado();
            return inventario;
        } catch (Exception e) {
            this.mensajeResultado = "Error al ver inventario: " + e.getMessage();
            logger.error("Error al ver inventario", e);
            return new ArrayList<>();
        }
    }

    @Override
    public String getMensajeResultado() {
        return this.mensajeResultado;
    }
}