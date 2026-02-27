package dam.code;

import com.sun.javafx.tk.TKStageListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DashboarController {

    @FXML
    private Label lblBienvenido;

    @FXML
    private Button btnCerrar;

    public void setMensajeBienvenida(String mensajeBienvenida) {
        lblBienvenido.setText(mensajeBienvenida);
    }

    @FXML
    private void cerrarSesion(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar Sesion");
        alert.setHeaderText("¿Seguro que quieres salir?");
        alert.setContentText("Se cerrará la sesion actual.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/form.fxml"));

                    Scene scene = new Scene(loader.load());

                    Stage stage = (Stage) btnCerrar.getScene().getWindow();
                    stage.setScene(scene);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
