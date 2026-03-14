package dam.code.controller;

import dam.code.AppPelicula;
import dam.code.model.Persona;
import dam.code.persistence.JsonManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Map;

public class SignupController {

    @FXML private TextField tfDni;
    @FXML private TextField tfNombre;
    @FXML private TextField tfApellido;
    @FXML private TextField tfEmail;
    @FXML private TextField tfPassword;

    @FXML
    private void onRegistrar() {
        try {
            String dni      = tfDni.getText().trim();
            String nombre   = tfNombre.getText().trim();
            String apellido = tfApellido.getText().trim();
            String email    = tfEmail.getText().trim();
            String password = tfPassword.getText();

            // Validaciones
            if (!dni.matches("\\d{8}[A-Za-z]")) {
                throw new Exception("DNI inválido. Formato: 8 números + 1 letra (ej: 12345678A).");
            }
            if (nombre.isBlank() || apellido.isBlank() || email.isBlank()) {
                throw new Exception("Todos los campos son obligatorios.");
            }
            if (password.length() < 4) {
                throw new Exception("La contraseña debe tener al menos 4 caracteres.");
            }

            Map<Persona, String> usuarios = JsonManager.cargarUsuarios();

            // Comprobar DNI duplicado
            for (Persona p : usuarios.keySet()) {
                if (p.getDni().equalsIgnoreCase(dni)) {
                    throw new Exception("Ya existe un usuario registrado con ese DNI.");
                }
            }

            Persona nueva = new Persona(dni, nombre, apellido, email);
            usuarios.put(nueva, password);
            JsonManager.guardarUsuarios(usuarios);

            mostrarInfo("Usuario registrado correctamente.");
            AppPelicula.mostrarVista("/dam/code/view/login_view.fxml");

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onIrALogin() {
        AppPelicula.mostrarVista("/dam/code/view/login_view.fxml");
    }

    private void mostrarError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error"); a.setHeaderText(null);
        a.setContentText(msg); a.showAndWait();
    }

    private void mostrarInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Registro exitoso"); a.setHeaderText(null);
        a.setContentText(msg); a.showAndWait();
    }
}