package dam.code.controller;

import dam.code.AppPelicula;
import dam.code.exceptions.PersonaException;
import dam.code.model.Persona;
import dam.code.service.RegistroService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegistroController {

    @FXML private TextField     txtDni;
    @FXML private TextField     txtNombre;
    @FXML private TextField     txtApellido;
    @FXML private TextField     txtEmail;
    @FXML private PasswordField txtContrasena;

    private final RegistroService registroService = new RegistroService();

    @FXML
    private void onRegistrar() {
        try {
            String dni      = txtDni.getText().trim();
            String nombre   = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String email    = txtEmail.getText().trim();
            String password = txtContrasena.getText();

            Persona nueva = new Persona(dni, nombre, apellido, email);
            registroService.registrar(nueva, password);

            mostrarInfo("Usuario registrado correctamente.");
            AppPelicula.mostrarVista("view/inicio_view.fxml");

        } catch (PersonaException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onIrAInicio() {
        AppPelicula.mostrarVista("view/inicio_view.fxml");
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