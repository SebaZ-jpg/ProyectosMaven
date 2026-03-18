package dam.code.service;

import dam.code.exceptions.PersonaException;
import dam.code.model.Persona;
import dam.code.persistence.RegistroDAO;

import java.util.Map;

public class RegistroService {

    private Map<Persona, String> registros;

    public RegistroService() {
        this.registros = RegistroDAO.cargarRegistros();
    }

    public void registrar(Persona persona, String password) throws PersonaException {
        if (!persona.getDni().matches("\\d{8}[A-Za-z]")) {
            throw new PersonaException("DNI inválido. Formato: 8 números y 1 letra (ej: 12345678A).");
        }
        for (Persona p : registros.keySet()) {
            if (p.getDni().equalsIgnoreCase(persona.getDni())) {
                throw new PersonaException("Ya existe un usuario con ese DNI.");
            }
        }
        if (password == null || password.length() < 4) {
            throw new PersonaException("La contraseña debe tener mínimo 4 caracteres.");
        }
        registros.put(persona, password);
        try {
            RegistroDAO.guardarRegistros(registros);
        } catch (Exception e) {
            throw new PersonaException("Error al guardar el usuario.", e);
        }
    }

    public Persona login(String dni, String password) throws PersonaException {
        for (Map.Entry<Persona, String> entry : registros.entrySet()) {
            if (entry.getKey().getDni().equalsIgnoreCase(dni)) {
                if (!entry.getValue().equals(password)) {
                    throw new PersonaException("Contraseña incorrecta.");
                }
                return entry.getKey();
            }
        }
        throw new PersonaException("No existe ningún usuario con ese DNI.");
    }

    public boolean existenUsuarios() {
        return !registros.isEmpty();
    }
}
