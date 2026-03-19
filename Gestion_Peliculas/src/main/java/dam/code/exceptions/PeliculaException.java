package dam.code.exceptions;

/**
 * Excepción personalizada para errores relacionados con la gestión de películas.
 */

public class PeliculaException extends Exception {

    public PeliculaException(String mensaje) {
        super(mensaje);
    }
}