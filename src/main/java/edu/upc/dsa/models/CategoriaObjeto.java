package edu.upc.dsa.models;

import edu.upc.dsa.util.annotations.*;

@Table(name="categoria_objeto")
public class CategoriaObjeto {

    @Id
    @Column(name="id_categoria")
    private int id_categoria;

    @Column(name="nombre")
    private String nombre;

    public CategoriaObjeto() {}

    public CategoriaObjeto(int id_categoria, String nombre) {
        setId_categoria(id_categoria);
        setNombre(nombre);
    }

    public int getId_categoria() {return id_categoria;}
    public void setId_categoria(int id_categoria) {this.id_categoria = id_categoria;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    @Override
    public String toString() {
        return "CategoriaObjeto [id_categoria=" + id_categoria + ", nombre=" + nombre + "]";
    }
}