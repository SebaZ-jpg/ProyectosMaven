package dam.code.exceptions;

/**
 * Excepción personalizada para errores relacionados con la gestión de usuarios.
 */

public class PersonaException extends Exception {
    public PersonaException(String mensaje) {
        super(mensaje);
    }
}
