package dam.code.controller;

import dam.code.AppPelicula;
import dam.code.exceptions.PersonaException;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegistroController {

    @FXML private TextField txtDni;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;

    @FXML
    private void onRegistrar() {
        try {
            AppPelicula.getRegistroService().registrarUsuario(
                    txtDni.getText().trim(),
                    txtNombre.getText().trim(),
                    txtApellido.getText().trim(),
                    txtEmail.getText().trim(),
                    txtPassword.getText().trim()
            );

            mostrarInfo("Usuario registrado correctamente.");
            AppPelicula.cargarVista("/view/incio_view.fxml");

        } catch (PersonaException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            mostrarError("Error inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void onIrLogin() {
        try {
            AppPelicula.cargarVista("/view/incio_view.fxml");
        } catch (Exception e) {
            mostrarError("Error al cargar el login: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}