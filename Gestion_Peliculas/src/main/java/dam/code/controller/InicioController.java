package dam.code.controller;

import dam.code.AppPelicula;
import dam.code.model.Persona;
import dam.code.persistence.JsonManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Map;

public class InicioController {

    @FXML private TextField     tfDni;
    @FXML private PasswordField pfPassword;

    private static Persona usuarioActual;

    @FXML
    private void onLogin() {
        try {
            String dni      = tfDni.getText().trim();
            String password = pfPassword.getText();

            if (dni.isBlank() || password.isBlank()) {
                throw new Exception("Rellena todos los campos.");
            }

            Map<Persona, String> usuarios = JsonManager.cargarUsuarios();
            Persona encontrado = null;

            for (Map.Entry<Persona, String> entry : usuarios.entrySet()) {
                if (entry.getKey().getDni().equalsIgnoreCase(dni)) {
                    if (!entry.getValue().equals(password)) {
                        throw new Exception("Contraseña incorrecta.");
                    }
                    encontrado = entry.getKey();
                    break;
                }
            }

            if (encontrado == null) {
                throw new Exception("No existe ningún usuario con ese DNI.");
            }

            usuarioActual = encontrado;
            AppPelicula.mostrarVista("/dam/code/view/dashboard.fxml");

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onIrARegistro() {
        // ✅ barras en vez de puntos
        AppPelicula.mostrarVista("/dam/code/view/registro_view.fxml");
    }

    public static Persona getUsuarioActual() { return usuarioActual; }
    public static void cerrarSesion() { usuarioActual = null; }

    private void mostrarError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error"); a.setHeaderText(null);
        a.setContentText(msg); a.showAndWait();
    }
}