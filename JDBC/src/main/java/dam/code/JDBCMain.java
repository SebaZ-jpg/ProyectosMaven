package dam.code;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JDBCMain extends Application {

    //start() - Monta la ventana
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/UsuarioView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 500);

        stage.setScene(scene);
        stage.setTitle("Gestion de Usuarios");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

/*
FXMLLoader → carga el archivo .fxml que tiene el diseño visual de la pantalla
Scene → es la "escena", el contenido de la ventana (400px ancho, 500px alto)
Stage → es la ventana en sí, el marco exterior
setTitle() → el título de la ventana
setResizable(false) → la ventana no se puede redimensionar
show() → muestra la ventana
* */