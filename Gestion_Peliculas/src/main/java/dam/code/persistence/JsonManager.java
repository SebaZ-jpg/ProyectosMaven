package dam.code.persistence;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import dam.code.dto.PeliculaDTO;
import dam.code.model.Pelicula;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;

public class JsonManager {

    private final Gson gson;

    public JsonManager() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(java.time.LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }

    public void guardar(Map<Pelicula, Integer> visualizaciones, String rutaArchivo) throws IOException {
        List<PeliculaDTO> lista = new ArrayList<>();
        for (Map.Entry<Pelicula, Integer> entry : visualizaciones.entrySet()) {
            Pelicula p = entry.getKey();
            lista.add(new PeliculaDTO(
                    p.getid(),
                    p.getitulo(),
                    p.getdirector(),
                    p.getduracion(),
                    p.getfechaPublicacion(),
                    entry.getValue()
            ));
        }
        Path path = Paths.get(rutaArchivo);
        Files.createDirectories(path.getParent());

        try (Writer writer = new FileWriter(rutaArchivo)) {
            gson.toJson(lista, writer);
        }
    }

    public Map<Pelicula, Integer> cargar(String rutaArchivo) throws IOException {
        Map<Pelicula, Integer> resultado = new LinkedHashMap<>();
        File file = new File(rutaArchivo);

        if (!file.exists()) return resultado;

        Type tipo = new TypeToken<List<PeliculaDTO>>() {}.getType();
        try (Reader reader = new FileReader(file)) {
            List<PeliculaDTO> lista = gson.fromJson(reader, tipo);
            if (lista != null) {
                for (PeliculaDTO dto : lista) {
                    resultado.put(Pelicula.fromDTO(dto), dto.getVisualizaciones());
                }
            }
        }
        return resultado;
    }
}