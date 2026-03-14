package dam.code.service;

public class RegistroService {

    private Map<Persona, String> registros;

    public RegistroService() {
        // carga el mapa desde RegistroDAO
    }

    public void registrar(Persona persona, String password) throws PersonaException {
        // validaciones + RegistroDAO.guardarRegistros()
    }

    public Persona login(String dni, String password) throws PersonaException {
        // buscar + verificar + devolver Persona
    }

    public boolean existenUsuarios() {
        // devuelve si el mapa no está vacío
    }
}
