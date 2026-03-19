package dam.code.persistence;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import dam.code.model.Pelicula;
import dam.code.model.Persona;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JsonManager {

    private static final String DIR_DATA            = "data/";
    private static final String DIR_VISUALIZACIONES = "data/visualizaciones/";
    private static final String ARCHIVO_PELICULAS   = "data/peliculas.json";
    private static final String ARCHIVO_USUARIOS    = "data/usuarios.dat";

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();

    // ── Películas ─────────────────────────────────────

    public static List<Pelicula> cargarPeliculas() {
        File file = new File(ARCHIVO_PELICULAS);
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            Type tipo = new TypeToken<List<Map<String, Object>>>(){}.getType();
            List<Map<String, Object>> lista = GSON.fromJson(reader, tipo);
            if (lista == null) return new ArrayList<>();
            List<Pelicula> resultado = new ArrayList<>();
            for (Map<String, Object> item : lista) {
                resultado.add(new Pelicula(
                        (String) item.get("id"),
                        (String) item.get("titulo"),
                        (String) item.get("director"),
                        ((Number) item.get("duracion")).intValue(),
                        LocalDate.parse((String) item.get("fechaPublicacion"))
                ));
            }
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void guardarPeliculas(List<Pelicula> peliculas) throws IOException {
        crearDirectorio(DIR_DATA);
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Pelicula p : peliculas) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id",               p.getid());
            item.put("titulo",           p.getitulo());
            item.put("director",         p.getdirector());
            item.put("duracion",         p.getduracion());
            item.put("fechaPublicacion", p.getfechaPublicacion().toString());
            lista.add(item);
        }
        try (Writer w = new FileWriter(ARCHIVO_PELICULAS)) {
            GSON.toJson(lista, w);
        }
    }

    // ── Visualizaciones por usuario ───────────────────
    // Un fichero JSON por usuario: data/visualizaciones/<dni>.json
    // Estructura: Map<idPelicula, Integer>

    public static Map<String, Integer> cargarVisualizacionesUsuario(Persona usuario) {
        String ruta = DIR_VISUALIZACIONES + usuario.getDni() + ".json";
        File file = new File(ruta);
        if (!file.exists()) return new LinkedHashMap<>();
        try (Reader reader = new FileReader(file)) {
            Type tipo = new TypeToken<Map<String, Integer>>(){}.getType();
            Map<String, Integer> mapa = GSON.fromJson(reader, tipo);
            return mapa != null ? mapa : new LinkedHashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new LinkedHashMap<>();
        }
    }

    public static void guardarVisualizacionesUsuario(Persona usuario,
                                                     Map<String, Integer> visualizaciones)
            throws IOException {
        crearDirectorio(DIR_VISUALIZACIONES);
        String ruta = DIR_VISUALIZACIONES + usuario.getDni() + ".json";
        try (Writer w = new FileWriter(ruta)) {
            GSON.toJson(visualizaciones, w);
        }
    }

    // ── Usuarios ──────────────────────────────────────
    // Map<Persona, String> serializado en .dat

    @SuppressWarnings("unchecked")
    public static Map<Persona, String> cargarUsuarios() {
        File file = new File(ARCHIVO_USUARIOS);
        if (!file.exists()) return new LinkedHashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<Persona, String>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new LinkedHashMap<>();
        }
    }

    public static void guardarUsuarios(Map<Persona, String> registros) throws IOException {
        crearDirectorio(DIR_DATA);
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARCHIVO_USUARIOS))) {
            oos.writeObject(registros);
        }
    }

    // ── Utilidad ──────────────────────────────────────

    private static void crearDirectorio(String ruta) {
        File dir = new File(ruta);
        if (!dir.exists()) dir.mkdirs();
    }
}