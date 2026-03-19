package dam.code.persistence;

import dam.code.model.Persona;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class RegistroDAO {

    private static final String RUTA = "data/registros.dat";

    public void guardar(Map<Persona, String> registros) throws IOException {
        Path path = Paths.get(RUTA);
        Files.createDirectories(path.getParent());

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA))) {
            oos.writeObject(registros);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<Persona, String> cargar() throws IOException, ClassNotFoundException {
        File file = new File(RUTA);
        if (!file.exists()) return new LinkedHashMap<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<Persona, String>) ois.readObject();
        }
    }

    public boolean existenUsuarios() {
        File file = new File(RUTA);
        if (!file.exists()) return false;
        try {
            Map<Persona, String> registros = cargar();
            return !registros.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}