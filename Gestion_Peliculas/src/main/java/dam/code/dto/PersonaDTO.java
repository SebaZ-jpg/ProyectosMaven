package dam.code.dto;

import dam.code.model.Persona;

public class PersonaDTO {

    private String dni;
    private String nombre;
    private String apellido;
    private String email;

    public PersonaDTO(String dni, String nombre, String apellido, String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    public String getdni() {
        return dni;
    }

    public String getnombre() {
        return nombre;
    }

    public String getapellido() {
        return apellido;
    }

    public String getemail() {
        return email;
    }
}
