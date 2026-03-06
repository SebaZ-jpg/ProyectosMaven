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

    @FXML private TableView<Libro> tablaLibros;
    @FXML private TableColumn<Libro,String> colTitulo;
    @FXML private TableColumn<Libro,String> colAutor;
    @FXML private TableColumn<Libro,Double> colPrecio;
    @FXML private TableColumn<Libro,Integer> colStock;

    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;

    private LibroService libroService;

    public void setLibroService(LibroService libroService) {
        this.libroService = libroService;
        tablaLibros.setItems(libroService.getLibros());
    }

    @FXML
    private void initialize() {
        prefWidthColumns();

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

    private void prefWidthColumns() {
        tablaLibros.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS); // hacemos que las columnas sean redimencionables

        colTitulo.prefWidthProperty().bind(tablaLibros.widthProperty().multiply(0.45)); // widthProperty propiedad del ancho de la tabla
        colAutor.prefWidthProperty().bind(tablaLibros.widthProperty().multiply(0.30));
        colPrecio.prefWidthProperty().bind(tablaLibros.widthProperty().multiply(0.15));
        colStock.prefWidthProperty().bind(tablaLibros.widthProperty().multiply(0.10));
    }

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
