package dam.code.controller;

import dam.code.model.Usuario;
import dam.code.service.UsuarioService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class UsuarioController {

    private final UsuarioService service = new UsuarioService();

    //@FXML significa que estos elementos están definidos
    // en un archivo .fxml (el diseño visual de la pantalla) y JavaFX
    // los conecta automáticamente con estas variables.

    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colEmail;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        cargarUsuarios();
    }

    @FXML
    public void guardarUsuario() {
        try {
            service.crearUsuario(txtNombre.getText(), txtEmail.getText());
            limpiarCampos();
            cargarUsuarios();
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    //Pide todos los usuarios al Service y los mete en la tabla. observableArrayList
    // es una lista especial de JavaFX que actualiza la tabla automáticamente cuando cambia.
    private void cargarUsuarios() {
        tablaUsuarios.setItems(FXCollections.observableArrayList(service.listarUsuarios()));
    }

    //limpiarCampos() vacía los TextField tras guardar, y mostrarError() muestra una
    // ventana emergente de error con el mensaje recibido.
    private void limpiarCampos() {
        txtNombre.clear();
        txtEmail.clear();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(mensaje);
        alert.show();
    }
}
