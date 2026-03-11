package dam.code.model;

import dam.code.dto.PeliculaDTO;
import javafx.beans.property.*;/*

Id (String - 3 letras y 2 números)
Título (String)
Director (String)
Duración (Double)
Fecha de publicación (LocalDate)

*/
public class Pelicula {


private final StringProperty id;
private final StringProperty titulo;
private final StringProperty director;
private final DoubleProperty duracion;
private final StringProperty fecha_publicacion;

    public Pelicula(String id, String titulo, String director, Double duracion, String fecha_publicacion) {
        this.id = new SimpleStringProperty(id);
        this.titulo = new SimpleStringProperty(titulo);
        this.director = new SimpleStringProperty(director);
        this.duracion = new SimpleDoubleProperty(duracion);
        this.fecha_publicacion = new SimpleStringProperty(fecha_publicacion);
    }

    public String getId() {
        return id.get();
    }

    public String getTitulo() {
        return titulo.get();
    }

    public String getDirector() {
        return director.get();
    }

    public Double getDuracion() {
        return duracion.get();
    }

    public String getFecha_publicacion() {
        return fecha_publicacion.get();
    }

    //getter properties
    public StringProperty idProperty() {
        return id;
    }
    public StringProperty tituloProperty() {
        return titulo;
    }
    public StringProperty directorProperty() {
        return director;
    }
    public DoubleProperty duracionProperty() {
        return duracion;
    }
    public StringProperty fecha_publicacionProperty() {
        return fecha_publicacion;
    }


    public void setDuracion(double duracion) {
        this.duracion.set(duracion);
    }

    public void setFecha_publicacion(String fecha_publicacion) {
    this.fecha_publicacion.set(fecha_publicacion);
    }

    public PeliculaDTO toDTO() {
        return new PeliculaDTO(
          getId(),
          getTitulo(),
          getDirector(),
          getDuracion(),
          getFecha_publicacion()
        );
    }

    public static Pelicula fromDTO(PeliculaDTO dto) {
        return new Pelicula(
            dto.getId(),
            dto.getTitulo(),
            dto.getDirector(),
            dto.getDuracion(),
            dto.getFecha_publicacion()
        );
    }

}

