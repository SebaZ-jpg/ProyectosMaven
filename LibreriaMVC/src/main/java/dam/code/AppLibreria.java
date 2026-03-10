package dam.code;

import dam.code.controller.LibroController;
import dam.code.model.Libro;
import dam.code.persistence.JsonManager;
import dam.code.repository.LibroRepository;
import dam.code.service.LibroService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// clase principal para correr el programa con interfaz incluida
public class AppLibreria extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // 1. Carga el archivo FXML (la vista)
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/libreria_view.fxml"));

        Parent root = fxmlLoader.load(); // cargo mi vista // // aquí se ejecuta initialize() del controller

        // 2. Obtiene el controller que el FXMLLoader creó automáticamente
        LibroController controller = fxmlLoader.getController(); //incializo el controlador lo del view


        // 3. Crea las capas de abajo hacia arriba
        LibroRepository repository = new JsonManager();
        LibroService service = new LibroService(repository); //service al mismo nivel del controller y manda al respository(guarda los datos)

        // 4. Inyecta el servicio en el controller
        controller.setLibroService(service);

        // 5. Muestra la ventana
        stage.setScene(new Scene(root, 800, 600)); //tamaño del cuadro que vemos cuando se ejecuta
        stage.setResizable(false); // no se puede modificar
        stage.show(); // lo enseño
    }

    public static void main(String[] args) {
        launch(args);
    }

}