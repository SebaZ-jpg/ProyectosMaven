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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/libreria_view.fxml"));

        Parent root = fxmlLoader.load(); // cargo mi vista
        LibroController controller = fxmlLoader.getController(); //incializo el controlador lo del view

        LibroRepository repository = new JsonManager();
        LibroService service = new LibroService(repository); //service al mismo nivel del controller y manda al respository(guarda los datos)

        controller.setLibroService(service);

        stage.setScene(new Scene(root, 800, 600)); //tamaño del cuadro que vemos cuando se ejecuta
        stage.setResizable(false); // no se puede modificar
        stage.show(); // lo enseño
    }

    public static void main(String[] args) {
        launch(args);
    }

}