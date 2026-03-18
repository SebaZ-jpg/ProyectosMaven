package dam.code.model;

import dam.code.dto.PeliculaDTO;
import javafx.beans.property.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Pelicula implements Serializable {

    private final StringProperty id;              // 3 letras + 2 números
    private final StringProperty titulo;
    private final StringProperty director;
    private final IntegerProperty duracion;
    private final ObjectProperty<LocalDate> fechaPublicacion;

    public Pelicula(String id, String titulo, String director, Integer duracion, LocalDate fechaPublicacion) {
        this.id = new SimpleStringProperty(id);
        this.titulo = new SimpleStringProperty(titulo);
        this.director = new SimpleStringProperty(director);
        this.duracion = new SimpleIntegerProperty(duracion);
        this.fechaPublicacion = new SimpleObjectProperty<>(fechaPublicacion);
    }

    public String getid() {
        return id.get();
    }

    public String getitulo(){
        return titulo.get();
    }

    public String getdirector(){
        return director.get();
    }

    public Integer getduracion(){
        return duracion.get();
    }

    public LocalDate getfechaPublicacion(){
        return fechaPublicacion.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public StringProperty tituloProperty() {
        return titulo;
    }

    public StringProperty directorProperty() {
        return director;
    }

    public IntegerProperty duracionProperty() {
        return duracion;
    }

    public ObjectProperty<LocalDate> fechaProperty() {
        return fechaPublicacion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion.set(duracion);
    }

    public void setfechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion.set(fechaPublicacion);
    }

    public PeliculaDTO toDTO() {
        return new PeliculaDTO(
            getid(),
            getitulo(),
            getdirector(),
            getduracion(),
            getfechaPublicacion()
        );
    }

    public static Pelicula fromDTO(PeliculaDTO dto) {
        return new Pelicula(
                dto.getId(),
                dto.getTitulo(),
                dto.getDirector(),
                dto.getDuracion(),
                dto.getFechaPublicacion()
        );
    }
}