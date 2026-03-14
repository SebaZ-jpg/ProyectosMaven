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

    private static final String ARCHIVO_PELICULAS = "data/visualizaciones/peliculas.json";
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();

    // ─── PELÍCULAS ────────────────────────────────────────────────

    public static Map<Pelicula, Integer> cargarPeliculas() {
        File file = new File(ARCHIVO_PELICULAS);
        if (!file.exists()) return new LinkedHashMap<>();
        try (Reader reader = new FileReader(file)) {
            Type tipo = new TypeToken<List<Map<String, Object>>>(){}.getType();
            List<Map<String, Object>> lista = GSON.fromJson(reader, tipo);
            if (lista == null) return new LinkedHashMap<>();
            Map<Pelicula, Integer> mapa = new LinkedHashMap<>();
            for (Map<String, Object> item : lista) {
                Pelicula p = new Pelicula(
                        (String) item.get("id"),
                        (String) item.get("titulo"),
                        (String) item.get("director"),
                        ((Number) item.get("duracion")).intValue(),
                        LocalDate.parse((String) item.get("fechaPublicacion"))
                );

                int vis = ((Number) item.get("visualizaciones")).intValue();
                mapa.put(p, vis);
            }
            return mapa;
        } catch (Exception e) {
            return new LinkedHashMap<>();
        }
    }

    public static void guardarPeliculas(Map<Pelicula, Integer> visualizaciones) throws IOException {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Map.Entry<Pelicula, Integer> entry : visualizaciones.entrySet()) {
            Pelicula p = entry.getKey();
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", p.getId());
            item.put("titulo", p.getTitulo());
            item.put("director", p.getDirector());
            item.put("duracion", p.getDuracion());
            item.put("fechaPublicacion", p.getFechaPublicacion().toString());
            item.put("visualizaciones", entry.getValue());
            lista.add(item);
        }
        try (Writer w = new FileWriter(ARCHIVO_PELICULAS)) {
            GSON.toJson(lista, w);
        }
    }

    // ─── VISUALIZACIONES POR USUARIO ─────────────────────────────

    public static void guardarVisualizacionUsuario(Pelicula p, Persona usuario) throws IOException {
        String nombreArchivo = "data/visualizaciones/" + usuario.getDni() + ".json";
        File file = new File(nombreArchivo);

        List<Map<String, String>> lista = new ArrayList<>();
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type tipo = new TypeToken<List<Map<String, String>>>(){}.getType();
                List<Map<String, String>> existentes = GSON.fromJson(reader, tipo);
                if (existentes != null) lista.addAll(existentes);
            }
        }

        Map<String, String> entrada = new LinkedHashMap<>();
        entrada.put("peliculaId", p.getId());
        entrada.put("titulo", p.getTitulo());
        entrada.put("fechaVisualizacion",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        lista.add(entrada);

        try (Writer w = new FileWriter(file)) {
            GSON.toJson(lista, w);
        }
    }

    // ─── USUARIOS (.dat) ─────────────────────────────────────────

    private static final String ARCHIVO_USUARIOS = "usuarios.dat";

    @SuppressWarnings("unchecked")
    public static Map<Persona, String> cargarUsuarios() {
        File file = new File(ARCHIVO_USUARIOS);
        if (!file.exists()) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<Persona, String>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public static void guardarUsuarios(Map<Persona, String> registros) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_USUARIOS))) {
            oos.writeObject(registros);
        }
    }
}