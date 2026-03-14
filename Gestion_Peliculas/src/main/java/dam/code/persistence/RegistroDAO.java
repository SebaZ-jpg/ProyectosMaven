package dam.code.persistence;

import dam.code.model.Persona;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

// Se encarga exclusivamente de leer y escribir el archivo usuarios.dat. Nada más. Separar esto del servicio es parte del patrón MVC.
public class RegistroDAO {

    private static final String RUTA = "data/registros/usuarios.dat";

    public static void guardarRegistros(Map<Persona, String> registros) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(RUTA))) {
            out.writeObject(registros);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<Persona, String> cargarRegistros() {
        File file = new File(RUTA);
        if (!file.exists()) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<Persona, String>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();

        }
    }
}

