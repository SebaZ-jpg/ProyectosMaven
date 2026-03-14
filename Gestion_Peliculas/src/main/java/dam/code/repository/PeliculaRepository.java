package dam.code.repository;

import dam.code.exceptions.PeliculasException;
import dam.code.model.Pelicula;
import dam.code.model.Persona;
import dam.code.persistence.JsonManager;

import java.time.LocalDate;
import java.util.*;

public interface PeliculaRepository {

    void agregar(Pelicula pelicula) throws PeliculasException;
    void editarTitulo(Pelicula pelicula, String nuevoTitulo) throws PeliculasException;
    void editarFecha(Pelicula pelicula, LocalDate nuevaFecha) throws PeliculasException;
    void eliminar(Pelicula pelicula) throws PeliculasException;
    void agregarVisualizacion(Pelicula pelicula, Persona usuario) throws PeliculasException;
    Map<Pelicula, Integer> getVisualizaciones();
    List<Pelicula> getPeliculas();
}