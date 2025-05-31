package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;
import edu.upc.dsa.util.annotations.*;

@Table(name = "inventario")
public class Inventario {

    @Id
    @Column(name = "id_inventario")
    private String id_inventario;

    @Column(name = "id_partida")
    private String id_partida;

    @Column(name = "id_objeto")
    private String id_objeto;

    public Inventario() {
        setId_inventario(RandomUtils.getId());
    }

    public Inventario(String id_inventario, String id_partida, String id_objeto) {
        this(); // genera id aleatorio
        if (id_inventario != null) setId_inventario(id_inventario);
        setId_partida(id_partida);
        setId_objeto(id_objeto);
    }

    public String getId_inventario() { return id_inventario; }
    public void setId_inventario(String id_inventario) { this.id_inventario = id_inventario; }

    public String getId_partida() { return id_partida; }
    public void setId_partida(String id_partida) { this.id_partida = id_partida; }

    public String getId_objeto() { return id_objeto; }
    public void setId_objeto(String id_objeto) { this.id_objeto = id_objeto; }

    @Override
    public String toString() {
        return "Inventario [id_inventario=" + id_inventario + ", id_partida=" + id_partida + ", id_objeto=" + id_objeto + "]";
    }
}
