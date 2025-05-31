package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;
import edu.upc.dsa.util.annotations.*;

@Table(name="insignia")
public class Insignia {

    @Id
    @Column(name="id_insignia")
    private String id_insignia;

    @Column(name="nombre")
    private String nombre;

    @Column(name="avatar")
    private String avatar;

    public Insignia(){
        setId_insignia(RandomUtils.getId());
    }

    public Insignia(String id_insignia, String nombre, String avatar){
        this(); // Llama al constructor sin parámetros (asigna un id aleatorio).
        if (id_insignia!=null) setId_insignia(id_insignia);
        setNombre(nombre);
        setAvatar(avatar);
    }

    public String getId_insignia() {return id_insignia;}
    public void setId_insignia(String id_insignia) {this.id_insignia=id_insignia;}

    public String getNombre() {return  nombre;}
    public void setNombre(String nombre) {this.nombre=nombre;}

    public String getAvatar() {return avatar;}
    public void setAvatar(String avatar) {this.avatar=avatar;}

    @Override
    public String toString(){
        return "Insignia [id_insignia="+id_insignia+", nombre="+nombre+", avatar="+avatar+"]";
    }
}
