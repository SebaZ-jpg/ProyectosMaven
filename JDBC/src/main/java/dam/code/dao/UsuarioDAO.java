package dam.code.dao;

import dam.code.model.Usuario;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UsuarioDAO {

    void save(Usuario usuario); // Guardar/crear un usuario
    List<Usuario> findAll(); // Obtener TODOS los usuarios

    /*
    Optional es un contenedor que significa "puede que haya un usuario,
    o puede que no". Si buscas el id 99 y no existe, en vez de devolver null (lo que podría causar errores),
    devuelve un Optional vacío. Es más seguro.
    * */
    Optional<Usuario> findById(int id); // Buscar UN usuario por su id
    void delete(int id); // Eliminar un usuario por su id
}
