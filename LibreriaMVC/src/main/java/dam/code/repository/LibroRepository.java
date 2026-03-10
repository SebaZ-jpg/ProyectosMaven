package dam.code.repository;

import dam.code.model.Libro;

import java.util.List;

    /*
    Define el contrato de qué operaciones de persistencia existen,
    pero no dice cómo. Esto permite que mañana puedas cambiar JSON por una base de datos SQL
    sin tocar nada más que crear una nueva clase que implemente esta interfaz.
    */

public interface LibroRepository {

    List<Libro> cargar();

    void guardar(List<Libro> libros);
}
