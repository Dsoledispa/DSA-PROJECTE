package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;
import edu.upc.dsa.util.annotations.*;

@Table(name="categoria_objeto")
public class CategoriaObjeto {

    @Id
    @Column(name="id_categoria")
    private String id_categoria;

    @Column(name="nombre")
    private String nombre;

    public CategoriaObjeto() {
        setId_categoria(RandomUtils.getId());
    }

    public CategoriaObjeto(String id_categoria, String nombre) {
        this(); // Llama al constructor sin parámetros (asigna un id aleatorio).
        if (id_categoria != null) setId_categoria(id_categoria);
        setNombre(nombre);
    }

    public String getId_categoria() {return id_categoria;}
    public void setId_categoria(String id_categoria) {this.id_categoria = id_categoria;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    @Override
    public String toString() {
        return "CategoriaObjeto [id_categoria=" + id_categoria + ", nombre=" + nombre + "]";
    }
}