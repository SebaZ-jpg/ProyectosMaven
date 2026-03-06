package dam.code.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dam.code.dto.LibroDTO;
import dam.code.model.Libro;
import dam.code.repository.LibroRepository;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonManager implements LibroRepository {

    private static final String FILE_PATH = "libros.json";
    private static final Gson GSON = new Gson();

    public void guardar(List<Libro> libros) { // recibo lista de libros
        List<LibroDTO> dtoList = libros.stream() // lo comvierto a una lista DTO invidualmente de libro a DTO
                .map(Libro::toDTO)
                .toList();

        try (Writer writer = new FileWriter(FILE_PATH)) {
            GSON.toJson(dtoList, writer); //te doy una lista y conviertelos a json
        } catch (IOException e) {
            e.printStackTrace(); // muestra error pero sigue ejecutandose
        }
    }

    public List<Libro> cargar() {
        try (Reader reader = new FileReader(FILE_PATH)) {

            Type listType = new TypeToken<List<LibroDTO>>(){}.getType(); //tengo mi tipo de lista de libros DTO

            List<LibroDTO> dtoList = GSON.fromJson(reader, listType); // la lista de tipo ahora Json

            if(dtoList == null) return new ArrayList<>(); // si no retorno nada obtengo un arraylist

            return dtoList.stream() //convertir libro DTO a libro
                    .map(Libro::fromDTO)
                    .toList();

        } catch (IOException e) { // lanza la exception de una nueva arraylist
            return new ArrayList<>();
        }
    }
}