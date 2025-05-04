package edu.upc.dsa.models;

public class Producto {
    private String id;
    private String nombre;
    private double precio;
    //private String foto; // ruta de foto, pensando en BBDD
    //private String descripcion;
    //private int stock;

    public Producto(){

    }

    public Producto(String id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Producto [id="+id+" , nombre="+ nombre +" , precio="+precio+"]";
    }
}
