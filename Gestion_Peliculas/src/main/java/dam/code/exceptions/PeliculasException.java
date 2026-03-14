package dam.code.exceptions;

public class PeliculasException extends Exception {

    public PeliculasException(String mensaje) {
        super(mensaje);
    }

    public PeliculasException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}