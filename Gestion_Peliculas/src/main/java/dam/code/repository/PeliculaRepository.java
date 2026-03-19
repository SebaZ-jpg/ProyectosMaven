package dam.code.repository;

import dam.code.exceptions.PeliculaException;
import dam.code.model.Pelicula;

import java.util.Map;

/**
 * Interfaz que define las operaciones disponibles sobre el mapa de visualizaciones.
 * Implementada por PeliculaService.
 */
public interface PeliculaRepository {
    void agregarPelicula(Pelicula pelicula) throws PeliculaException;
    void eliminarPelicula(String id) throws PeliculaException;
    void editarTitulo(String id, String nuevoTitulo) throws PeliculaException;
    void editarFecha(String id, java.time.LocalDate nuevaFecha) throws PeliculaException;
    Map<Pelicula, Integer> getVisualizaciones();
    void agregarVisualizacion(String id) throws PeliculaException;
    void guardar() throws PeliculaException;
    void cargar() throws PeliculaException;
}