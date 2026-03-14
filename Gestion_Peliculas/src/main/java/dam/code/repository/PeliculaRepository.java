package dam.code.repository;

import dam.code.exceptions.PeliculaException;
import dam.code.model.Pelicula;
import dam.code.model.Persona;

import java.time.LocalDate;
import java.util.*;

public interface PeliculaRepository {

    void agregar(Pelicula pelicula) throws PeliculaException;
    void editarTitulo(Pelicula pelicula, String nuevoTitulo) throws PeliculaException;
    void editarFecha(Pelicula pelicula, LocalDate nuevaFecha) throws PeliculaException;
    void eliminar(Pelicula pelicula) throws PeliculaException;
    void agregarVisualizacion(Pelicula pelicula, Persona usuario) throws PeliculaException;
    Map<Pelicula, Integer> getVisualizaciones();
    List<Pelicula> getPeliculas();
}