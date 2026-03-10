package dam.code.service;

import dam.code.exceptions.LibroException;
import dam.code.model.Libro;
import dam.code.repository.LibroRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// El cerebro de la aplicación

public class LibroService {

    private final ObservableList<Libro> libros; // lista dinamica, soo vigila(guardia de seguridad), con el nombre libros
    private final LibroRepository repository;

    public LibroService(LibroRepository repository) {
        this.repository = repository; //lista de acciones

        libros = FXCollections.observableArrayList(repository.cargar()); // inicilizar el libro, esto deriva del repository
        //       ↑ carga desde JSON y mete los libros en un ObservableList
        //esto es un constructor
    }

    public ObservableList<Libro> getLibros() {
        return libros; // lista de libro que se mostrara
    }

    public void agregarLibro(Libro libro) {
        validarLibro(libro); // lanza LibroException si algo falla
        libros.add(libro); // añade libro a la lista // la tabla se actualiza sola (ObservableList)
        guardar(); // persiste en JSON
    }

    public void eliminarLibro(Libro libro) throws LibroException {

        if (libro == null) {
            throw new LibroException("debe seleeccionar un loibro");
        }
        libros.remove(libro);
    guardar();
    }

    private void validarLibro(Libro libro) throws LibroException {

        if(libro.getTitulo() == null || libro.getTitulo().isBlank()) {
            throw new LibroException("El titulo es obligatorio");
        }
        if(libro.getAutor() == null || libro.getAutor().isBlank()) {
            throw new LibroException("El autor es obligatorio");
        }
        if (libro.getPrecio() <= 0) {
            throw new LibroException("El precio debe ser mayo que 0");
        }
        if (libro.getStock() < 0) {
            throw new LibroException("El stock no puede ser negativo");
        }

        boolean existe = libros.stream() // pregunto si esto ya exsite, stream()anyMatch (coincidencia dentoer de mi lista)
                .anyMatch(l -> l.getTitulo().equalsIgnoreCase(libro.getTitulo()));
        if (existe) {
            throw new LibroException("Ya existe un libro con ese titulo");
        }
    }

    private void guardar () {
        repository.guardar(libros);
    } //guarda los libros

    public void actualizarPrecio(Libro libro, double nuevoPrecio)  throws LibroException {
        if (nuevoPrecio <= 0) {
            throw new LibroException("El precio debe ser mayo que 0");
        }
        libro.setPrecio(nuevoPrecio);
        guardar();
    }

    public void actualizarStock(Libro libro, int nuevoStock)  throws LibroException {
        if (nuevoStock < 0) {
            throw new LibroException("El stock no puede ser negativo");
        }
        libro.setStock(nuevoStock);
        guardar();
    }
}
