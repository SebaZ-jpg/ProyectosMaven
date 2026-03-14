package dam.code;

import dam.code.service.PeliculaService;
import dam.code.persistence.JsonManager;
import dam.code.model.Persona;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Map;

public class AppPelicula extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("CineApp");

        Map<Persona, String> usuarios = JsonManager.cargarUsuarios();
        if (usuarios.isEmpty()) {
            mostrarVista("/dam/code/view/signup_view.fxml");
        } else {
            mostrarVista("/dam/code/view/login_view.fxml");
        }

        primaryStage.show();
    }

    public static void mostrarVista(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(AppPelicula.class.getResource(fxml));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.sizeToScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}