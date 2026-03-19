package dam.code;

import dam.code.service.RegistroService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppPelicula extends Application {

    private static Stage primaryStage;
    private static RegistroService registroService;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        registroService = new RegistroService();
        registroService.cargar();

        if (registroService.existenUsuarios()) {
            cargarVista("/view/incio_view.fxml");
        } else {
            cargarVista("/view/registro_view.fxml");
        }

        primaryStage.setTitle("Gestión de Películas");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void cargarVista(String ruta) throws Exception {
        FXMLLoader loader = new FXMLLoader(AppPelicula.class.getResource(ruta));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
    }

    public static RegistroService getRegistroService() {
        return registroService;
    }

    public static void main(String[] args) {
        launch(args);
    }
}