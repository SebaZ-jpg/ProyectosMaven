package dam.code;

import dam.code.service.RegistroService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppPelicula extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("CineApp");

        // Flujo del enunciado:
        // Si no hay usuarios → registro, si hay → inicio de sesión
        RegistroService registroService = new RegistroService();
        if (!registroService.existenUsuarios()) {
            mostrarVista("view/registro_view.fxml");
        } else {
            mostrarVista("view/inicio_view.fxml");
        }

        primaryStage.show();
    }

    public static void mostrarVista(String fxml) {
        try {
            String ruta = "/" + fxml;
            var url = AppPelicula.class.getResource(ruta);
            System.out.println("Cargando: " + url);
            FXMLLoader loader = new FXMLLoader(url);
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