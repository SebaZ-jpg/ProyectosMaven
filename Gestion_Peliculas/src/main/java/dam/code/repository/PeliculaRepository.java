package dam.code.repository;

import dam.code.model.Pelicula;
import dam.code.model.Persona;

import java.util.List;

public interface PeliculaRepository {

        List<Pelicula> cargar();

        void guardar(List<Pelicula> libros);

}
