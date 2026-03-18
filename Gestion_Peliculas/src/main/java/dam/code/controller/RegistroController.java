package dam.code.controller;

import dam.code.AppPelicula;
import dam.code.exceptions.PersonaException;
import dam.code.model.Persona;
import dam.code.persistence.JsonManager;
import dam.code.service.RegistroService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Map;

public class RegistroController {

    @FXML private TextField tfDni;
    @FXML private TextField tfNombre;
    @FXML private TextField tfApellido;
    @FXML private TextField tfEmail;
    @FXML private TextField tfPassword;

    private final RegistroService registroService = new RegistroService();

    @FXML
    private void onRegistrar() {
        try {
            String dni      = tfDni.getText().trim();
            String nombre   = tfNombre.getText().trim();
            String apellido = tfApellido.getText().trim();
            String email    = tfEmail.getText().trim();
            String password = tfPassword.getText();

            // Validaciones
            if (nombre.isBlank() || apellido.isBlank() || email.isBlank() || password.isBlank()) {
                throw new PersonaException("Todos los campos son obligatorios.");
            }

            Persona nueva = new Persona(dni, nombre, apellido, email);
            registroService.registrar(nueva, password);

            mostrarInfo("Usuario registrado correctamente.");
            AppPelicula.mostrarVista("/dam/code/view/inicio_view.fxml");

        } catch (PersonaException e) {
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