package dam.code.model;

/*
 DNI (String - 8 números y 1 letra)
 Nombre
 Apellido
 Email
*/

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Persona {

    private final  StringProperty dni;
    private final StringProperty nombre;
    private final StringProperty apellido;
    private final StringProperty email;

    public Persona(String dni, String nombre, String apellido, String email) {
        this.dni = new SimpleStringProperty(dni);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        this.email = new SimpleStringProperty(email);
    }

    public String getDni() {
        return dni.get();
    }

    public String getNombre() {
        return nombre.get();
    }

    public String getApellido() {
        return apellido.get();
    }

    public String getEmail() {
        return email.get();
    }

    //getters properties
    public StringProperty dniProperty() {
        return dni;
    }
    public StringProperty nombreProperty() {
        return nombre;
    }
    public StringProperty apellidoProperty() {
        return apellido;
    }
    public StringProperty emailProperty() {
        return email;
    }

    public PersonaDTO toDTO(){
        return new PersonaDTO(
                getDni(),
                getNombre(),
                getApellido(),
                getEmail()
        );
    }

    public static Persona fromFTO(PersonaDTO dto){
        return new Persona(
                dto.getdni(),
                dto.getnombre(),
                dto.getapellido(),
                dto.getemail()
        );
    }
}
