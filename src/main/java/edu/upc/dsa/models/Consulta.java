package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;
import edu.upc.dsa.util.annotations.*;

import java.time.LocalDateTime;

@Table(name = "consulta")
public class Consulta {

    @Id
    @Column(name = "id_consulta")
    private String id_consulta;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "id_usuario")
    private String id_usuario;

    public Consulta() {
        setId_consulta(RandomUtils.getId());
    }

    public Consulta(String id_consulta, LocalDateTime fecha, String titulo, String mensaje, String id_usuario){
        this(); // Llama al constructor sin parámetros (asigna un id aleatorio).
        if (id_consulta != null) setId_consulta(id_consulta);
        setFecha(fecha);
        setTitulo(titulo);
        setMensaje(mensaje);
        setId_usuario(id_usuario);

    }

    public String getId_consulta() {return id_consulta;}
    public void setId_consulta(String id_consulta) {this.id_consulta = id_consulta;}

    public LocalDateTime getFecha() {return fecha;}
    public void setFecha(LocalDateTime fecha) {this.fecha = fecha;}

    public String getTitulo() {return titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}

    public String getMensaje() {return mensaje;}
    public void setMensaje(String mensaje) {this.mensaje = mensaje;}

    public String getId_usuario() {return id_usuario;}
    public void setId_usuario(String id_usuario) {this.id_usuario = id_usuario;}

    @Override
    public String toString() {
        return "Consulta [id_consulta=" + id_consulta + ", fecha=" + fecha + ", titulo=" + titulo +
                ", mensaje=" + mensaje + ", id_usuario=" + id_usuario + "]";
    }
}
