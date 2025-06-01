package edu.upc.dsa.models;

public class Video {
    private String video;
    private String titulo;

    public Video() {}

    public Video(String video, String titulo) {
        setVideo(video);
        setTitulo(titulo);
    }

    public String getVideo() {return video;}
    public void setVideo(String video) {this.video = video;}

    public String getTitulo() {return titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}

    @Override
    public String toString() {
        return "Video [video=" + video + ", titulo=" + titulo + "]";
    }
}