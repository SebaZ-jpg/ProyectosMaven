package dam.code.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class PeliculaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final String titulo;
    private final String director;
    private final Integer duracion;
    private final LocalDate fechaPublicacion;
    private final Integer visualizaciones;

    public PeliculaDTO(String id, String titulo, String director,
                       Integer duracion, LocalDate fechaPublicacion) {

        this(id, titulo, director, duracion, fechaPublicacion, 0);
    }

    public PeliculaDTO(String id, String titulo, String director, Integer duracion, LocalDate fechaPublicacion, Integer visualizaciones) {
        this.id = id;
        this.titulo = titulo;
        this.director = director;
        this.duracion = duracion;
        this.fechaPublicacion = fechaPublicacion;
        this.visualizaciones = visualizaciones;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDirector() { return director; }
    public Integer getDuracion() { return duracion; }
    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public Integer getVisualizaciones() { return visualizaciones; }
}