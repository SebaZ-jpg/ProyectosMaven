package dam.code.service;

import dam.code.exceptions.PeliculaException;
import dam.code.model.Pelicula;
import dam.code.model.Persona;
import dam.code.persistence.JsonManager;

import java.time.LocalDate;
import java.util.*;

public class PeliculaService {

    // ── Operaciones de películas ───────────────────────

    public Map<Pelicula, Integer> getPeliculas() {
        return JsonManager.cargarPeliculas();
    }

    public void agregar(Pelicula pelicula) throws PeliculaException {
        if (pelicula.getid() == null || pelicula.getid().isBlank()) {
            throw new PeliculaException("El ID no puede estar vacío.");
        }
        try {
            Map<Pelicula, Integer> mapa = JsonManager.cargarPeliculas();
            boolean existe = mapa.keySet().stream()
                    .anyMatch(p -> p.getid().equalsIgnoreCase(pelicula.getid()));
            if (existe) {
                throw new PeliculaException("Ya existe una película con el ID: " + pelicula.getid());
            }
            mapa.put(pelicula, 0);
            JsonManager.guardarPeliculas(mapa);
        } catch (PeliculaException e) {
            throw e;
        } catch (Exception e) {
            throw new PeliculaException("Error al guardar la película: " + e.getMessage());
        }
    }

    public void eliminar(Pelicula pelicula) throws PeliculaException {
        try {
            Map<Pelicula, Integer> mapa = JsonManager.cargarPeliculas();
            Pelicula clave = mapa.keySet().stream()
                    .filter(p -> p.getid().equalsIgnoreCase(pelicula.getid()))
                    .findFirst()
                    .orElseThrow(() -> new PeliculaException("Película no encontrada."));
            mapa.remove(clave);
            JsonManager.guardarPeliculas(mapa);
        } catch (PeliculaException e) {
            throw e;
        } catch (Exception e) {
            throw new PeliculaException("Error al eliminar: " + e.getMessage());
        }
    }

    public void editarTitulo(Pelicula pelicula, String nuevoTitulo) throws PeliculaException {
        if (nuevoTitulo == null || nuevoTitulo.isBlank()) {
            throw new PeliculaException("El título no puede estar vacío.");
        }
        try {
            Map<Pelicula, Integer> mapa = JsonManager.cargarPeliculas();
            Pelicula clave = mapa.keySet().stream()
                    .filter(p -> p.getid().equalsIgnoreCase(pelicula.getid()))
                    .findFirst()
                    .orElseThrow(() -> new PeliculaException("Película no encontrada."));
            int vis = mapa.remove(clave);
            Pelicula actualizada = new Pelicula(
                    clave.getid(), nuevoTitulo, clave.getdirector(),
                    clave.getduracion(), clave.getfechaPublicacion()
            );
            mapa.put(actualizada, vis);
            JsonManager.guardarPeliculas(mapa);
        } catch (PeliculaException e) {
            throw e;
        } catch (Exception e) {
            throw new PeliculaException("Error al editar título: " + e.getMessage());
        }
    }

    public void editarFecha(Pelicula pelicula, LocalDate nuevaFecha) throws PeliculaException {
        if (nuevaFecha == null) {
            throw new PeliculaException("La fecha no puede estar vacía.");
        }
        try {
            Map<Pelicula, Integer> mapa = JsonManager.cargarPeliculas();
            Pelicula clave = mapa.keySet().stream()
                    .filter(p -> p.getid().equalsIgnoreCase(pelicula.getid()))
                    .findFirst()
                    .orElseThrow(() -> new PeliculaException("Película no encontrada."));
            int vis = mapa.remove(clave);
            Pelicula actualizada = new Pelicula(
                    clave.getid(), clave.getitulo(), clave.getdirector(),
                    clave.getduracion(), nuevaFecha
            );
            mapa.put(actualizada, vis);
            JsonManager.guardarPeliculas(mapa);
        } catch (PeliculaException e) {
            throw e;
        } catch (Exception e) {
            throw new PeliculaException("Error al editar fecha: " + e.getMessage());
        }
    }

    // ── Operaciones de visualizaciones ────────────────

    public void agregarVisualizacion(Pelicula pelicula, Persona usuario) throws PeliculaException {
        try {
            JsonManager.guardarVisualizacionUsuario(pelicula, usuario);
            Map<Pelicula, Integer> mapa = JsonManager.cargarPeliculas();
            Pelicula clave = mapa.keySet().stream()
                    .filter(p -> p.getid().equalsIgnoreCase(pelicula.getid()))
                    .findFirst()
                    .orElseThrow(() -> new PeliculaException("Película no encontrada."));
            mapa.put(clave, mapa.get(clave) + 1);
            JsonManager.guardarPeliculas(mapa);
        } catch (PeliculaException e) {
            throw e;
        } catch (Exception e) {
            throw new PeliculaException("Error al registrar visualización: " + e.getMessage());
        }
    }

    public Map<Pelicula, Integer> getVisualizaciones(Persona usuario) {
        return JsonManager.cargarPeliculas();
    }
}