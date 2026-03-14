package dam.code.persistence;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    @Override
    public JsonElement serialize(LocalDate src, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
        // convierte el LocalDate a un String con .toString()
        // devuélvelo como new JsonPrimitive(...)
    }

    @Override
    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
        return LocalDate.parse(json.getAsString());
        // obtén el texto con json.getAsString()
        // conviértelo con LocalDate.parse(...)
    }
}
