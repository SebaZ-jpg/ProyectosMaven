package dam.code;

import dam.code.service.RegistroService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación. Extiende {@code Application} de JavaFX.
 * Decide qué vista mostrar al inicio según si existen usuarios registrados.
 */
public class AppPelicula extends Application {

    private static Stage primaryStage;
    private static RegistroService registroService;

    /**
     * Punto de entrada de JavaFX. Carga los registros y muestra
     * la vista de login o registro según corresponda.
     */
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

    /**
     * Carga y muestra la vista indicada por la ruta del FXML.
     */
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