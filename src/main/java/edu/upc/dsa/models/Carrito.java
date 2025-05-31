package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;
import edu.upc.dsa.util.annotations.*;

@Table(name = "carrito")
public class Carrito {

    @Id
    @Column(name = "id_carrito")
    private String id_carrito;

    @Column(name = "id_partida")
    private String id_partida;

    @Column(name="id_objeto")
    private String id_objeto;

    public Carrito() {
        setId_carrito(RandomUtils.getId());
    }

    public Carrito(String id_carrito, String id_partida, String id_objeto) {
        this(); // Llama al constructor sin parámetros (asigna un id aleatorio).
        if (id_carrito != null) setId_carrito(id_carrito);
        setId_partida(id_partida);
        setId_objeto(id_objeto);
    }

    public String getId_carrito() { return id_carrito; }
    public void setId_carrito(String id_carrito) { this.id_carrito = id_carrito; }

    public String getId_partida() { return id_partida; }
    public void setId_partida(String id_partida) { this.id_partida = id_partida; }

    public String getId_objeto() { return id_objeto; }
    public void setId_objeto(String id_objeto) { this.id_objeto = id_objeto; }

    @Override
    public String toString() {
        return "Carrito [id_carrito=" + id_carrito + ", id_partida=" + id_partida + ", id_objeto=" + id_objeto+"]";
    }

}
