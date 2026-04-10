package dam.code.controller;

import dam.code.exceptions.PeliculaException;
import dam.code.models.Pelicula;
import dam.code.models.Usuario;
import dam.code.service.PeliculaService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class PeliculasController {

    private Usuario usuario;
    private PeliculaService service;

    @FXML private Label lblUsuario;
    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtDirector;
    @FXML
    private TextField txtDuracion;
    @FXML
    private DatePicker txtFecha;

    @FXML private TableView<Pelicula> tablaPeliculas;
    @FXML private TableColumn<Pelicula, Integer> colId;
    @FXML private TableColumn<Pelicula, String> colTitulo;
    @FXML private TableColumn<Pelicula, String> colDirector;
    @FXML private TableColumn<Pelicula, Integer> colDuracion;
    @FXML private TableColumn<Pelicula, LocalDate> colFecha;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        lblUsuario.setText("Usuario" + usuario.getNombre());
    }

    public void setPeliculaService(PeliculaService service) throws PeliculaException {
        this.service = service;
        tablaPeliculas.setItems(service.obtenerPeliculas());
    }

    @FXML
    private void initialize() {
        prefWidthColumns();
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colTitulo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitulo()));
        colDirector.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDirector()));
        colDuracion.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDuracion()).asObject());
        colFecha.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFecha_publicacion()));

        txtFecha.setEditable(false);
        validarPicker();
    }

    private void validarPicker() {
        txtFecha.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d0d0d0;");
                }
            }
        });
    }

    private void prefWidthColumns() {
        tablaPeliculas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        colId.prefWidthProperty().bind(tablaPeliculas.widthProperty().multiply(0.05));
        colTitulo.prefWidthProperty().bind(tablaPeliculas.widthProperty().multiply(0.35));
        colDirector.prefWidthProperty().bind(tablaPeliculas.widthProperty().multiply(0.30));
        colDuracion.prefWidthProperty().bind(tablaPeliculas.widthProperty().multiply(0.15));
        colFecha.prefWidthProperty().bind(tablaPeliculas.widthProperty().multiply(0.15));

        setVisualizacion();
    }

    private void setVisualizacion() {
        tablaPeliculas.setRowFactory(tv -> {
            TableRow<Pelicula> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Pelicula pelicula = row.getItem();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Visualizacion");
                    alert.setHeaderText("Añadir Visualizacion");
                    alert.setContentText("¿Quieres añadir una visualizacion a " + pelicula.getTitulo() + "?");
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK); {
                            try {
                                service.addVisualizacion(usuario.getId(), pelicula);
                                tablaPeliculas.setItems(service.obtenerPeliculas());
                            } catch (PeliculaException e) {
                                mostrarError(e.getMessage());
                            }
                        }
                    });
                }
            });
            return row;
        });
    }

    private void mostrarError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}


