package dam.code.dto;

public class PeliculaDTO {

    private String id;
    private String titulo;
    private String director;
    private double duracion;
    private String fecha_publicacion;

    public PeliculaDTO(String id, String titulo, String director, double duracion, String fecha_publicacion) {
        this.id = id;
        this.titulo = titulo;
        this.director = director;
        this.duracion = duracion;
        this.fecha_publicacion = fecha_publicacion;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDirector() {
        return director;
    }

    public double getDuracion() {
        return duracion;

    }

    public String getFecha_publicacion() {
        return fecha_publicacion;
    }

}
