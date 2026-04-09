package dam.code.dao.impl;

import dam.code.config.DatabaseConfig;
import dam.code.dao.UsuarioDAO;
import dam.code.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public void save(Usuario usuario) {
        String sql = "INSERT INTO usuarios(nombre, email) VALUES (?, ?)";

        try(Connection con = DatabaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, usuario.getNombre()); // rellena el primer ? con el nombre
            ps.setString(2, usuario.getEmail()); // rellena el segundo ? con el email

            ps.executeUpdate(); //  ejecuta el INSERT

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection con = DatabaseConfig.getConnection();
             Statement st = con.createStatement();

             //ResultSet es como una tabla de resultados, fila por fila
             ResultSet rs = st.executeQuery(sql)) {

            //rs.next() avanza a la siguiente fila, cuando no hay más devuelve false
            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email")
                );
                usuarios.add(u);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return usuarios;
    }

    @Override
    public Optional<Usuario> findById(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            //Usa if en vez de while porque solo espera un resultado
            if (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email")
                );

                //Si lo encuentra devuelve Optional.of(usuario), si no Optional.empty()
                return Optional.of(u);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
