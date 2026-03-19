package dam.code.controller;

import dam.code.AppPelicula;
import dam.code.exceptions.PersonaException;
import dam.code.model.Persona;
import dam.code.service.PeliculaService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class InicioController {

    @FXML private TextField txtDni;
    @FXML private PasswordField txtPassword;

    @FXML
    private void onLogin() {
        try {
            Persona usuario = AppPelicula.getRegistroService().login(
                    txtDni.getText().trim(),
                    txtPassword.getText().trim()
            );

            PeliculaService peliculaService = new PeliculaService();
            peliculaService.inicializar(usuario);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/peliculas_view.fxml"));
            Parent root = loader.load();

            PeliculaController peliculaController = loader.getController();
            peliculaController.inicializar(peliculaService);

            Stage stage = (Stage) txtDni.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (PersonaException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            mostrarError("Error inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void onIrRegistro() {
        try {
            AppPelicula.cargarVista("/view/registro_view.fxml");
        } catch (Exception e) {
            mostrarError("Error al cargar el registro: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}