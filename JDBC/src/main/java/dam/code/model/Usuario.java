package dam.code.model;

/*
Usuario es simplemente el modelo de
datos, no hace nada con la base de datos. Solo representa cómo es
un usuario dentro del programa: su id, nombre y email.
* */

public class Usuario {

    private Integer id;
    private String nombre;
    private String email;

    //// Sin id → cuando vas a CREAR un usuario nuevo (aún no tiene id, se lo asigna la BD)
    public Usuario(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    //// Con id → cuando lo RECUPERAS de la base de datos (ya tiene id asignado)
    public Usuario(Integer id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
