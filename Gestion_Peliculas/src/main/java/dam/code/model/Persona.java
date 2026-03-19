package dam.code.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Modelo que representa un usuario registrado en la aplicación.
 * Se serializa en un archivo .dat junto con su contraseña.
 */

public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String dni;       // 8 números + 1 letra
    private final String nombre;
    private final String apellido;
    private final String email;


    /**
     * Constructor completo de Persona.
     * El DNI debe tener formato de 8 números y 1 letra.
     */

    public Persona(String dni, String nombre, String apellido, String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Dos personas son iguales si coinciden DNI, nombre, apellido y email.
     */

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return Objects.equals(dni, persona.dni) && Objects.equals(nombre, persona.nombre) && Objects.equals(apellido, persona.apellido) && Objects.equals(email, persona.email);
    }

    /**
     * Genera el hash basado en DNI, nombre, apellido y email.
     */
    @Override
    public int hashCode() {
        return Objects.hash(dni,  nombre, apellido, email);
    }
}