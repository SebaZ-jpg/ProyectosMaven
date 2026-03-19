package dam.code.model;

import dam.code.dto.PeliculaDTO;
import javafx.beans.property.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Modelo que representa una película en la aplicación.
 * Usa JavaFX Properties para enlazarse con la TableView.
 * El ID debe tener formato de 3 letras y 2 números.
 */

public class Pelicula implements Serializable {

    private final StringProperty id;              // 3 letras + 2 números
    private final StringProperty titulo;
    private final StringProperty director;
    private final IntegerProperty duracion;
    private final ObjectProperty<LocalDate> fechaPublicacion;

    /**
     * Constructor completo de Pelicula.
     */
    public Pelicula(String id, String titulo, String director, Integer duracion, LocalDate fechaPublicacion) {
        this.id = new SimpleStringProperty(id);
        this.titulo = new SimpleStringProperty(titulo);
        this.director = new SimpleStringProperty(director);
        this.duracion = new SimpleIntegerProperty(duracion);
        this.fechaPublicacion = new SimpleObjectProperty<>(fechaPublicacion);
    }

    public String getid() {return id.get();}
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

    /**
     * Devuelve la propiedad del ID, titulo, director, duracion y fecha para enlazar con la TableView.
     */
    public StringProperty idProperty() {
        return id;
    }
    public StringProperty tituloProperty() {return titulo;}
    public StringProperty directorProperty() {
        return director;
    }
    public IntegerProperty duracionProperty() {
        return duracion;
    }
    public ObjectProperty<LocalDate> fechaProperty() {
        return fechaPublicacion;
    }

    public void setTitulo(String titulo){ this.titulo.set(titulo); }
    public void setfechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion.set(fechaPublicacion);
    }

    /**
     * Convierte la película a DTO para su transferencia o serialización.
     */
    public PeliculaDTO toDTO() {
        return new PeliculaDTO(
            getid(),
            getitulo(),
            getdirector(),
            getduracion(),
            getfechaPublicacion()
        );
    }

    /**
     * Crea una instancia de Pelicula a partir de un DTO.
     */
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