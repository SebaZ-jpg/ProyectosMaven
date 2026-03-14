package dam.code.service;

import dam.code.exceptions.PeliculaException;
import dam.code.model.Pelicula;
import dam.code.model.Persona;
import dam.code.persistence.JsonManager;
import dam.code.repository.PeliculaRepository;

import java.time.LocalDate;
import java.util.*;

public class PeliculaService implements PeliculaRepository {

    private Map<Pelicula, Integer> visualizaciones;

    public PeliculaService() {
        this.visualizaciones = JsonManager.cargarPeliculas();
    }

    @Override
    public void agregar(Pelicula p) throws PeliculaException {
        if (!p.getId().matches("[A-Za-z]{3}\\d{2}")) {
            throw new PeliculaException("ID inválido. Formato: 3 letras + 2 números (ej: ABC12).");
        }
        for (Pelicula existente : visualizaciones.keySet()) {
            if (existente.getId().equalsIgnoreCase(p.getId())) {
                throw new PeliculaException("Ya existe una película con el ID: " + p.getId());
            }
        }
        if (p.getTitulo() == null || p.getTitulo().isBlank()) {
            throw new PeliculaException("El título no puede estar vacío.");
        }
        visualizaciones.put(p, 0);
        try {
            JsonManager.guardarPeliculas(visualizaciones);
        } catch (Exception e) {
            throw new PeliculaException("Error al guardar la película.", e);
        }
    }

    @Override
    public void editarTitulo(Pelicula p, String nuevoTitulo) throws PeliculaException {
        if (nuevoTitulo == null || nuevoTitulo.isBlank()) {
            throw new PeliculaException("El título no puede estar vacío.");
        }
        p.setTitulo(nuevoTitulo);
        try {
            JsonManager.guardarPeliculas(visualizaciones);
        } catch (Exception e) {
            throw new PeliculaException("Error al actualizar el título.", e);
        }
    }

    @Override
    public void editarFecha(Pelicula p, LocalDate nuevaFecha) throws PeliculaException {
        if (nuevaFecha == null) {
            throw new PeliculaException("La fecha no puede ser nula.");
        }
        p.setFechaPublicacion(nuevaFecha);
        try {
            JsonManager.guardarPeliculas(visualizaciones);
        } catch (Exception e) {
            throw new PeliculaException("Error al actualizar la fecha.", e);
        }
    }

    @Override
    public void eliminar(Pelicula p) throws PeliculaException {
        if (!visualizaciones.containsKey(p)) {
            throw new PeliculaException("La película no existe en el registro.");
        }
        visualizaciones.remove(p);
        try {
            JsonManager.guardarPeliculas(visualizaciones);
        } catch (Exception e) {
            throw new PeliculaException("Error al eliminar la película.", e);
        }
    }

    @Override
    public void agregarVisualizacion(Pelicula p, Persona usuario) throws PeliculaException {
        if (!visualizaciones.containsKey(p)) {
            throw new PeliculaException("La película no existe.");
        }
        visualizaciones.put(p, visualizaciones.get(p) + 1);
        try {
            JsonManager.guardarPeliculas(visualizaciones);
            JsonManager.guardarVisualizacionUsuario(p, usuario);
        } catch (Exception e) {
            throw new PeliculaException("Error al registrar la visualización.", e);
        }
    }

    @Override
    public Map<Pelicula, Integer> getVisualizaciones() {
        return visualizaciones;
    }

    @Override
    public List<Pelicula> getPeliculas() {
        return new ArrayList<>(visualizaciones.keySet());
    }
}