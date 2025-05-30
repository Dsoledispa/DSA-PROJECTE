package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;
import edu.upc.dsa.util.annotations.*;

@Table(name="usuario")
public class Usuario {

    @Id
    @Column(name="id_usuario")
    private String id_usuario;

    @Column(name="nombre")
    private String nombre;

    @Column(name="password")
    private String password;

    public Usuario() {
        setId_usuario(RandomUtils.getId());
    }

    public Usuario(String id_usuario, String nombre, String password) {
        this(); // Llama al constructor sin parámetros (asigna un id aleatorio).
        if (id_usuario != null) setId_usuario(id_usuario);
        setNombre(nombre);
        setPassword(password);
    }

    public String getId_usuario() { return id_usuario; }
    public void setId_usuario(String id_usuario) { this.id_usuario = id_usuario; }

    public String getNombre() { return nombre;}
    public void setNombre(String nombre) { this.nombre = nombre;}

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "Usuario [id_usuario=" + id_usuario + ", nombre="+nombre+"]";
    }
}

