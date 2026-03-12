package dam.code.service;

import dam.code.exceptions.PeliculasException;
import dam.code.model.Pelicula;
import dam.code.repository.PeliculaRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PeliculaService {

    private final ObservableList<Pelicula> peliculas;
    private final PeliculaRepository repository;

    public PeliculaService(PeliculaRepository repository) {
        this.repository = repository;

        peliculas = FXCollections.observableArrayList(repository.cargar());
    }

    public ObservableList<Pelicula> getPeliculas() {
        return peliculas;
    }

    public void agregarPelicula(Pelicula pelicula) {
        validarPelicula(pelicula);
        peliculas.add(pelicula);
        guardar();
    }

    private void validarPelicula(Pelicula pelicula) throws PeliculasException {

        if(pelicula.getTitulo() == null || pelicula.getId().isBlank()) {
            throw new PeliculasException("El titulo es obligatorio");

        }

        if (pelicula.getDirector() == null || pelicula.getId().isBlank()) {
            throw new PeliculasException("El director es obligatorio");
        }

    }

}
