package dam.code.service;

import dam.code.exceptions.PeliculaException;
import dam.code.model.Pelicula;
import dam.code.model.Persona;
import dam.code.persistence.JsonManager;

import java.time.LocalDate;
import java.util.*;

public class PeliculaService {

    public List<Pelicula> getPeliculas() {
        return JsonManager.cargarPeliculas();
    }

    public void agregar(Pelicula pelicula) throws PeliculaException {
        validar(pelicula);
        try {
            List<Pelicula> lista = JsonManager.cargarPeliculas();
            boolean existe = lista.stream()
                    .anyMatch(p -> p.getid().equalsIgnoreCase(pelicula.getid()));
            if (existe) {
                throw new PeliculaException("Ya existe una película con el ID: " + pelicula.getid());
            }
            lista.add(pelicula);
            JsonManager.guardarPeliculas(lista);
        } catch (PeliculaException e) {
            throw e;
        } catch (Exception e) {
            throw new PeliculaException("Error al guardar: " + e.getMessage());
        }
    }

    public void eliminar(Pelicula pelicula) throws PeliculaException {
        try {
            List<Pelicula> lista = JsonManager.cargarPeliculas();
            boolean eliminada = lista.removeIf(
                    p -> p.getid().equalsIgnoreCase(pelicula.getid()));
            if (!eliminada) throw new PeliculaException("Película no encontrada.");
            JsonManager.guardarPeliculas(lista);
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
            List<Pelicula> lista = JsonManager.cargarPeliculas();
            Pelicula encontrada = lista.stream()
                    .filter(p -> p.getid().equalsIgnoreCase(pelicula.getid()))
                    .findFirst()
                    .orElseThrow(() -> new PeliculaException("Película no encontrada."));
            encontrada.setTitulo(nuevoTitulo);
            JsonManager.guardarPeliculas(lista);
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
            List<Pelicula> lista = JsonManager.cargarPeliculas();
            Pelicula encontrada = lista.stream()
                    .filter(p -> p.getid().equalsIgnoreCase(pelicula.getid()))
                    .findFirst()
                    .orElseThrow(() -> new PeliculaException("Película no encontrada."));
            encontrada.setfechaPublicacion(nuevaFecha);
            JsonManager.guardarPeliculas(lista);
        } catch (PeliculaException e) {
            throw e;
        } catch (Exception e) {
            throw new PeliculaException("Error al editar fecha: " + e.getMessage());
        }
    }

    // Añade una visualización a la película para el usuario actual
    public void agregarVisualizacion(Pelicula pelicula, Persona usuario) throws PeliculaException {
        try {
            Map<String, Integer> visualizaciones =
                    JsonManager.cargarVisualizacionesUsuario(usuario);
            visualizaciones.merge(pelicula.getid(), 1, Integer::sum);
            JsonManager.guardarVisualizacionesUsuario(usuario, visualizaciones);
        } catch (Exception e) {
            throw new PeliculaException("Error al registrar visualización: " + e.getMessage());
        }
    }

    // Devuelve Map<Pelicula, Integer> con las visualizaciones del usuario actual
    public Map<Pelicula, Integer> getVisualizaciones(Persona usuario) {
        List<Pelicula> peliculas = JsonManager.cargarPeliculas();
        Map<String, Integer> visUsuario = JsonManager.cargarVisualizacionesUsuario(usuario);
        Map<Pelicula, Integer> resultado = new LinkedHashMap<>();
        for (Pelicula p : peliculas) {
            resultado.put(p, visUsuario.getOrDefault(p.getid(), 0));
        }
        return resultado;
    }

    private void validar(Pelicula p) throws PeliculaException {
        if (p.getid() == null || !p.getid().matches("[A-Za-z]{3}\\d{2}")) {
            throw new PeliculaException("El ID debe tener 3 letras y 2 números. Ej: ABC01");
        }
        if (p.getitulo() == null || p.getitulo().isBlank()) {
            throw new PeliculaException("El título no puede estar vacío.");
        }
        if (p.getdirector() == null || p.getdirector().isBlank()) {
            throw new PeliculaException("El director no puede estar vacío.");
        }
        if (p.getduracion() == null || p.getduracion() <= 0) {
            throw new PeliculaException("La duración debe ser mayor que 0.");
        }
        if (p.getfechaPublicacion() == null) {
            throw new PeliculaException("La fecha no puede estar vacía.");
        }
    }
}