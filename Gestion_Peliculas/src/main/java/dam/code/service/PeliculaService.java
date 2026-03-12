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

        if (pelicula.getId() == null || pelicula.getId().isBlank()) {
            throw new PeliculasException("El titulo es obligatorio");
        }

        if(pelicula.getTitulo() == null || pelicula.getId().isBlank()) {
            throw new PeliculasException("El titulo es obligatorio");

        }

        if (pelicula.getDirector() == null || pelicula.getDirector().isBlank()) {
            throw new PeliculasException("El director es obligatorio");
        }

        if (pelicula.getDuracion() == null || pelicula.getDuracion() {
            throw new PeliculasException("El duracion es obligatoria");
        }

        if (pelicula.getFecha_publicacion() == null || pelicula.getFecha_publicacion().isBlank()) {
            throw new PeliculasException("La fecha publicacion es obligatoria");
        }

        private void guardar() {
            repository.guardar(peliculas);
        }


    }

}
