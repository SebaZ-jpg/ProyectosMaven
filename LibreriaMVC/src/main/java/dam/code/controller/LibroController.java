package dam.code.controller;

import dam.code.exceptions.LibroException;
import dam.code.model.Libro;
import dam.code.persistence.JsonManager;
import dam.code.service.LibroService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;


public class LibroController {

    /*
    @FXML le dice a JavaFX que esa variable está vinculada a un elemento del archivo .fxml (el diseño visual).
    Sin esta anotación, el campo sería null en tiempo de ejecución.

    TableView<Libro> → la tabla completa que muestra libros
    TableColumn<Libro, String> → columna cuyo tipo de objeto es Libro y cuyo valor mostrado es String
    TableColumn<Libro, Double> / <Libro, Integer> → columnas numéricas
    TextField → campos de texto del formulario donde el usuario escribe datos
    */

    @FXML private TableView<Libro> tablaLibros;
    @FXML private TableColumn<Libro,String> colTitulo;
    @FXML private TableColumn<Libro,String> colAutor;
    @FXML private TableColumn<Libro,Double> colPrecio;
    @FXML private TableColumn<Libro,Integer> colStock;

    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;

    /*
    LibroService contiene la lógica de negocio (agregar, eliminar, validar...). El controlador no hace esa lógica
    directamente, la delega al servicio. Esto es buena práctica.
    setItems(libroService.getLibros()) conecta la tabla con la lista de libros. Si esa lista es un ObservableList,
    la tabla se actualiza automáticamente cuando cambia la lista.
    */

    private LibroService libroService;

    public void setLibroService(LibroService libroService) {
        this.libroService = libroService;
        tablaLibros.setItems(libroService.getLibros());
    }

    /*
    JavaFX llama a este método automáticamente justo después de cargar el .fxml. Es el equivalente a un
    constructor para el controlador.
    */

    @FXML
    private void initialize() {
        prefWidthColumns();

        /*
        cellData representa cada celda/fila
        cellData.getValue() devuelve el objeto Libro de esa fila
        .tituloProperty() devuelve una StringProperty (tipo especial de JavaFX que permite binding)
        .asObject() es necesario para convertir DoubleProperty/IntegerProperty a sus tipos genéricos boxed (Double, Integer)
        * */

        //sirve pa rellenar, segun los valores
        colTitulo.setCellValueFactory(cellData -> cellData.getValue().tituloProperty());
        colAutor.setCellValueFactory(cellData -> cellData.getValue().autorProperty());
        colPrecio.setCellValueFactory(cellData -> cellData.getValue().precioProperty().asObject()); // asobject si es que no es tipo string
        colStock.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());

        tablaLibros.setEditable(true);

        //Precio Editable
        colPrecio.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colPrecio.setOnEditCommit(event -> { // setOnEditCommitme crea un evento
            Libro libro = event.getRowValue();
            double nuevoPrecio = event.getNewValue(); //event.getNewValue voy a introducir el numero valor quue sera double
            try {
                libroService.actualizarPrecio(libro, nuevoPrecio);
            } catch (LibroException e) {
                mostrarError(e.getMessage());
            }
        });

        /*
        setCellFactory(TextFieldTableCell...)Convierte la celda en un campo de texto al hacer doble clic
        DoubleStringConverterConvierte entre String (lo que se escribe) y Double (lo que necesita el modelo)
        setOnEditCommitSe ejecuta cuando el usuario confirma la edición (pulsa Enter)
        event.getRowValue()Obtiene el Libro de la fila editada
        event.getNewValue()Obtiene el nuevo valor introducido
        */

        //Stock editable
        colStock.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colStock.setOnEditCommit(event -> {
            Libro libro = event.getRowValue();
            int nuevoStock = event.getNewValue();
            try {
                libroService.actualizarStock(libro, nuevoStock);
            } catch (LibroException e) {
                mostrarError(e.getMessage());
            }
        });
    }

    /*
    CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS → las columnas llenan todo el ancho de la tabla
    .bind(...) → vinculación reactiva: si la ventana cambia de tamaño, las columnas se reajustan automáticamente
    Los porcentajes suman 1.0 (45% + 30% + 15% + 10% = 100%)
    */

    private void prefWidthColumns() {
        tablaLibros.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS); // hacemos que las columnas sean redimencionables

        colTitulo.prefWidthProperty().bind(tablaLibros.widthProperty().multiply(0.45)); // widthProperty propiedad del ancho de la tabla
        colAutor.prefWidthProperty().bind(tablaLibros.widthProperty().multiply(0.30));
        colPrecio.prefWidthProperty().bind(tablaLibros.widthProperty().multiply(0.15));
        colStock.prefWidthProperty().bind(tablaLibros.widthProperty().multiply(0.10));
    }

    /*
    Lee los textos de los TextField
    Double.parseDouble / Integer.parseInt convierten String → número. Si el texto no es un número, lanza NumberFormatException
    Crea un objeto Libro con esos datos
    Lo pasa al servicio para que lo valide y agregue
    Si todo va bien, limpia los campos
    Si hay error de formato → muestra alerta. Si hay error de negocio (LibroException) → muestra su mensaje
    */

    @FXML
    private void addLibro(){
        try {
            Libro libro = new Libro(
                    txtTitulo.getText(),
                    txtAutor.getText(),
                    Double.parseDouble(txtPrecio.getText()), // guardame en souble todo lo que puedas transofmra de este texto
                    Integer.parseInt(txtStock.getText())
            );

            libroService.agregarLibro(libro); // si todo esta bien se lo pasa al service y si no muestra el error que debe tener el service

            limpiarCampos();

        } catch (NumberFormatException e){ // unica validacion que dara el controlador
            mostrarError("Precio y stock deben ser numeros validos");
        } catch (LibroException e) {
            mostrarError(e.getMessage());
        }
    }

    /*
    Crea un diálogo modal de error de JavaFX. showAndWait() es importante:
    pausa la ejecución hasta que el usuario cierra la ventana.
    */

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText("Datos incorrectos");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void limpiarCampos(){
        txtTitulo.clear();
        txtAutor.clear();
        txtPrecio.clear();
        txtStock.clear();
    }

    /*
    - `getSelectionModel().getSelectedItem()` → obtiene el libro que el usuario tiene seleccionado en la tabla
    - El `if (seleccionado != null)` evita errores si no hay nada seleccionado
    - Delega la eliminación al servicio, que puede lanzar `LibroException` si hay alguna restricción
    */

    @FXML
    private void eliminarLibro(){ // acction del view

        Libro seleccionado = tablaLibros.getSelectionModel().getSelectedItem(); // seleccion del libro,

        if (seleccionado != null){
            try {
                libroService.eliminarLibro(seleccionado);
            } catch (LibroException e) {
                mostrarError(e.getMessage());
            }
        }
    }
}
