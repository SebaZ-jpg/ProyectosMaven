package dam.code.exceptions;

public class PeliculaException extends Exception {

    public PeliculaException(String mensaje) {
        super(mensaje);
    }

    public PeliculaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}