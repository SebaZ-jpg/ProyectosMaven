package dam.code.service;

import dam.code.exceptions.PersonaException;
import dam.code.model.Persona;
import dam.code.persistence.JsonManager;

import java.util.Map;

public class RegistroService {

    public void registrar(Persona persona, String password) throws PersonaException {
        validar(persona, password);
        try {
            Map<Persona, String> usuarios = JsonManager.cargarUsuarios();
            boolean existe = usuarios.keySet().stream()
                    .anyMatch(p -> p.getDni().equalsIgnoreCase(persona.getDni()));
            if (existe) {
                throw new PersonaException("Ya existe un usuario con el DNI: " + persona.getDni());
            }
            usuarios.put(persona, password);
            JsonManager.guardarUsuarios(usuarios);
        } catch (PersonaException e) {
            throw e;
        } catch (Exception e) {
            throw new PersonaException("Error al guardar el usuario: " + e.getMessage());
        }
    }

    public boolean existenUsuarios() {
        return !JsonManager.cargarUsuarios().isEmpty();
    }

    private void validar(Persona persona, String password) throws PersonaException {
        if (persona.getDni() == null || !persona.getDni().matches("\\d{8}[A-Za-z]")) {
            throw new PersonaException("El DNI debe tener 8 números seguidos de 1 letra.");
        }
        if (persona.getNombre() == null || persona.getNombre().isBlank()) {
            throw new PersonaException("El nombre no puede estar vacío.");
        }
        if (persona.getApellido() == null || persona.getApellido().isBlank()) {
            throw new PersonaException("El apellido no puede estar vacío.");
        }
        if (persona.getEmail() == null || !persona.getEmail().contains("@")) {
            throw new PersonaException("El email no es válido.");
        }
        if (password == null || password.length() < 4) {
            throw new PersonaException("La contraseña debe tener al menos 4 caracteres.");
        }
    }
}