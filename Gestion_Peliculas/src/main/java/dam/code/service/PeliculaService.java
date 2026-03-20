package dam.code.service;

import dam.code.exceptions.PeliculaException;
import dam.code.model.Pelicula;
import dam.code.model.Persona;
import dam.code.persistence.JsonManager;
import dam.code.repository.PeliculaRepository;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Servicio que gestiona las operaciones sobre el mapa de visualizaciones.
 * Implementa PeliculaRepository y usa JsonManager para la persistencia.
 * Cada usuario tiene su propio archivo JSON.
 */
public class PeliculaService implements PeliculaRepository {

    private final JsonManager jsonManager;
    private Map<Pelicula, Integer> visualizaciones;
    private String rutaArchivo;

    /**
     * Constructor que inicializa el JsonManager y el mapa vacío.
     */
    public PeliculaService() {
        this.jsonManager = new JsonManager();
        this.visualizaciones = new LinkedHashMap<>();
    }

    /**
     * Inicializa el servicio con el usuario actual y carga su JSON.
     */
    public void inicializar(Persona usuario) throws PeliculaException {
        this.rutaArchivo = "data/visualizaciones/" + usuario.getDni() + ".json";
        cargar();
    }

    /**
     * Agrega una nueva película al mapa validando ID, duración y fecha.
     */
    @Override
    public void agregarPelicula(Pelicula pelicula) throws PeliculaException {
        validarId(pelicula.getid());
        if (pelicula.getduracion() <= 30) {
            throw new PeliculaException("La duración debe ser mayor a 30 minutos.");
        }
        if (pelicula.getfechaPublicacion() == null) {
            throw new PeliculaException("La fecha de publicación es obligatoria.");
        }
        if (pelicula.getfechaPublicacion().isAfter(LocalDate.now())) {
            throw new PeliculaException("La fecha no puede ser posterior a la fecha actual.");
        }
        for (Pelicula p : visualizaciones.keySet()) {
            if (p.getid().equals(pelicula.getid())) {
                throw new PeliculaException("Ya existe una película con ese ID.");
            }
        }
        visualizaciones.put(pelicula, 0);
        guardar();
    }

    /**
     * Elimina la película con el ID indicado.
     */
    @Override
    public void eliminarPelicula(String id) throws PeliculaException {
        Pelicula encontrada = buscarPorId(id);
        visualizaciones.remove(encontrada);
        guardar();
    }

    /**
     * Edita el título de la película con el ID indicado.
     */
    @Override
    public void editarTitulo(String id, String nuevoTitulo) throws PeliculaException {
        Pelicula encontrada = buscarPorId(id);
        encontrada.setTitulo(nuevoTitulo);
        guardar();
    }

    /**
     * Edita la fecha de publicación de la película con el ID indicado.
     */
    @Override
    public void editarFecha(String id, LocalDate nuevaFecha) throws PeliculaException {
        Pelicula encontrada = buscarPorId(id);
        encontrada.setfechaPublicacion(nuevaFecha);
        guardar();
    }

    /**
     * Devuelve el mapa completo de películas y sus visualizaciones.
     */
    @Override
    public Map<Pelicula, Integer> getVisualizaciones() {
        return visualizaciones;
    }

    /**
     * Incrementa en 1 la visualización de la película con el ID indicado.
     * @param id identificador de la película
     * @throws PeliculaException si no existe ninguna película con ese ID
     */
    @Override
    public void agregarVisualizacion(String id) throws PeliculaException {
        Pelicula encontrada = buscarPorId(id);
        visualizaciones.put(encontrada, visualizaciones.get(encontrada) + 1);
        guardar();
    }

    /**
     * Incrementa en uno las visualizaciones de la película con el ID indicado.
     */
    @Override
    public void guardar() throws PeliculaException {
        try {
            jsonManager.guardar(visualizaciones, rutaArchivo);
        } catch (Exception e) {
            throw new PeliculaException("Error al guardar: " + e.getMessage());
        }
    }

    /**
     * Persiste el mapa de visualizaciones en el archivo JSON del usuario.
     */
    @Override
    public void cargar() throws PeliculaException {
        try {
            visualizaciones = jsonManager.cargar(rutaArchivo);
        } catch (Exception e) {
            throw new PeliculaException("Error al cargar: " + e.getMessage());
        }
    }

    /**
     * Busca y devuelve la película con el ID indicado.
     * Lanza excepción si no se encuentra.
     */
    private Pelicula buscarPorId(String id) throws PeliculaException {
        for (Pelicula p : visualizaciones.keySet()) {
            if (p.getid().equals(id)) return p;
        }
        throw new PeliculaException("No se encontró una película con ID: " + id);
    }

    /**
     * Valida que el ID tenga el formato correcto: 3 letras y 2 números.
     * Lanza excepción si el formato es incorrecto.
     */
    private void validarId(String id) throws PeliculaException {
        if (!id.matches("[A-Za-z]{3}\\d{2}")) {
            throw new PeliculaException("ID inválido. Formato: 3 letras y 2 números.");
        }
    }
}