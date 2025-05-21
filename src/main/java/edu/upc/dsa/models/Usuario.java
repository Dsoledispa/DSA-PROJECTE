package edu.upc.dsa.models;

import edu.upc.dsa.util.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Table(name="usuario")
public class Usuario {

    @Id
    @Column(name="nombreUsu")
    private String nombreUsu;

    @Column(name="password")
    private String password;

    @Ignore
    private List<Partida> partidas;

    public Usuario() {
        partidas = new ArrayList<>();
    }

    public Usuario(String nombreUsu, String password) {
        this(); // llama al constructor vacío y así inicializa partidas
        setNombreUsu(nombreUsu);
        setPassword(password);
    }

    public String getNombreUsu() { return nombreUsu; }
    public void setNombreUsu(String nombreUsu) { this.nombreUsu = nombreUsu; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<Partida> getPartidas() { return partidas; }
    public void setPartidas(List<Partida> partidas) { this.partidas = partidas; }

    @Override
    public String toString() {
        return "Usuario [nombreUsu=" + nombreUsu + ", partidas=" + partidas + "]";
    }
}

