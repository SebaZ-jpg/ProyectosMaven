package dam.code.service;

import dam.code.exceptions.PersonaException;
import dam.code.model.Persona;

import java.util.Map;

public class RegistroService {

    private Map<Persona, String> registros;

    public RegistroService() {
        // carga el mapa desde RegistroDAO
    }

    public void registrar(Persona persona, String password) throws PersonaException {
        // validaciones + RegistroDAO.guardarRegistros()
    }

    public Persona login(String dni, String password) throws PersonaException {
        return null;
    }
        // buscar + verificar + devolver Persona


    public boolean existenUsuarios() {

        return false;
    }
        // devuelve si el mapa no está vacío

}
