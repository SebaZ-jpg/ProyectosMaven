package dam.code.persistence;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Adaptador de Gson para serializar y deserializar objetos LocalDate
 * en formato ISO (yyyy-MM-dd) dentro del JSON.
 */
public class LocalDateAdapter extends TypeAdapter<LocalDate> {

    /**
     * Serializa un {@code LocalDate} a String en el JSON.
     */
    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {

        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toString());
        }
    }

    /**
     * Deserializa un String del JSON a LocalDate.
     */
    @Override
    public LocalDate read(JsonReader in) throws IOException {
        String date = in.nextString();
        return LocalDate.parse(date);
    }
}