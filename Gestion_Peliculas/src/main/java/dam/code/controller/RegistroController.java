package dam.code.controller;

import dam.code.AppPelicula;
import dam.code.exceptions.PersonaException;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controlador de la vista de registro de usuarios.
 * Gestiona el alta de nuevos usuarios y la navegación hacia el login.
 */
public class RegistroController {

    @FXML private TextField txtDni;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;

    /**
     * Registra un nuevo usuario con los datos del formulario.
     * Si el registro es exitoso navega al login.
     */
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

    /**
     * Navega a la vista de inicio de sesión.
     */
    @FXML
    private void onIrLogin() {
        try {
            AppPelicula.cargarVista("/view/incio_view.fxml");
        } catch (Exception e) {
            mostrarError("Error al cargar el login: " + e.getMessage());
        }
    }

    /**
     * Muestra un Alert de error con el mensaje indicado.
     * @param mensaje texto a mostrar
     */
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un Alert informativo con el mensaje indicado.
     * @param mensaje texto a mostrar
     */
    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}