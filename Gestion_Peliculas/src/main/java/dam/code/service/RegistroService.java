package dam.code.service;

import dam.code.exceptions.PersonaException;
import dam.code.model.Persona;
import dam.code.persistence.RegistroDAO;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Servicio que gestiona el registro y autenticación de usuarios.
 * Se apoya en RegistroDAO para la persistencia en el archivo .dat.
 */
public class RegistroService {

    private final RegistroDAO registroDAO;
    private Map<Persona, String> registros;
    private Persona usuarioActual;

    /**
     * Constructor que inicializa el DAO y el mapa de registros vacío.
     */
    public RegistroService() {
        this.registroDAO = new RegistroDAO();
        this.registros = new LinkedHashMap<>();
    }

    /**
     * Carga los registros desde el archivo .dat.
     */
    public void cargar() throws PersonaException {
        try {
            registros = registroDAO.cargar();
        } catch (Exception e) {
            throw new PersonaException("Error al cargar los usuarios: " + e.getMessage());
        }
    }

    /**
     * Registra un nuevo usuario validando el DNI, email y duplicados.
     */
    public void registrarUsuario(String dni, String nombre, String apellido, String email, String password) throws PersonaException {
        validarDni(dni);
        validarEmail(email);

        for (Persona p : registros.keySet()) {
            if (p.getDni().equals(dni)) throw new PersonaException("Ya existe un usuario con ese DNI.");
            if (p.getEmail().equals(email)) throw new PersonaException("Ya existe un usuario con ese email.");
        }

        Persona nueva = new Persona(dni, nombre, apellido, email);
        registros.put(nueva, password);

        try {
            registroDAO.guardar(registros);
        } catch (Exception e) {
            throw new PersonaException("Error al guardar el usuario: " + e.getMessage());
        }
    }

    /**
     * Autentica un usuario por DNI y contraseña.
     * Devuelve la Persona si las credenciales son correctas.
     */
    public Persona login(String dni, String password) throws PersonaException {
        for (Map.Entry<Persona, String> entry : registros.entrySet()) {
            if (entry.getKey().getDni().equals(dni) && entry.getValue().equals(password)) {
                usuarioActual = entry.getKey();
                return usuarioActual;
            }
        }
        throw new PersonaException("DNI o contraseña incorrectos.");
    }

    /**
     * Comprueba si existen usuarios registrados en el sistema.
     * @return true si hay al menos un usuario, false si no
     */
    public boolean existenUsuarios() {
        return registroDAO.existenUsuarios();
    }

    /**
     * Devuelve el usuario que ha iniciado sesión actualmente.
     * @return Persona con la sesión activa, o null si no hay sesión
     */
    public Persona getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Valida que el DNI tenga el formato correcto: 8 números y 1 letra.
     * @param dni DNI a validar
     * @throws PersonaException si el formato es incorrecto
     */
    private void validarDni(String dni) throws PersonaException {
        if (!dni.matches("\\d{8}[A-Za-z]")) {
            throw new PersonaException("DNI inválido. Formato: 8 números y 1 letra.");
        }
    }

    /**
     * Valida que el email tenga un formato válido.
     * @param email email a validar
     * @throws PersonaException si el formato es incorrecto
     */
    private void validarEmail(String email) throws PersonaException {
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new PersonaException("Email inválido.");
        }
    }
}