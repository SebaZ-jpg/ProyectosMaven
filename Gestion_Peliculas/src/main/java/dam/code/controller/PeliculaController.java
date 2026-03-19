package dam.code.controller;

import dam.code.AppPelicula;
import dam.code.exceptions.PeliculaException;
import dam.code.model.Pelicula;
import dam.code.service.PeliculaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

/**
 * Controlador de la vista de gestión de visualizaciones.
 * Gestiona las operaciones sobre la tabla de películas y la navegación.
 */
public class PeliculaController {

    @FXML private TableView<Pelicula> tablaPeliculas;
    @FXML private TableColumn<Pelicula, String> colId;
    @FXML private TableColumn<Pelicula, String> colTitulo;
    @FXML private TableColumn<Pelicula, String> colDirector;
    @FXML private TableColumn<Pelicula, Integer> colDuracion;
    @FXML private TableColumn<Pelicula, LocalDate> colFecha;
    @FXML private TableColumn<Pelicula, Integer> colVisualizaciones;

    @FXML private TextField txtId;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtDirector;
    @FXML private TextField txtDuracion;
    @FXML private DatePicker dpFecha;

    private PeliculaService peliculaService;
    private ObservableList<Pelicula> listaPeliculas;

    /**
     * Inicializa el controlador con el servicio del usuario actual.
     * Configura la tabla y el doble clic.
     */
    public void inicializar(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
        configurarTabla();
        cargarTabla();
        configurarDobleClick();
    }

    /**
     * Configura las columnas de la tabla enlazándolas con las propiedades del modelo.
     */
    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colVisualizaciones.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        peliculaService.getVisualizaciones().get(data.getValue())
                ).asObject()
        );
    }

    /**
     * Carga los datos del mapa de visualizaciones en la tabla.
     */
    private void cargarTabla() {
        listaPeliculas = FXCollections.observableArrayList(
                peliculaService.getVisualizaciones().keySet()
        );
        tablaPeliculas.setItems(listaPeliculas);
    }

    /**
     * Configura el doble clic sobre una fila para añadir una visualización.
     */
    private void configurarDobleClick() {
        tablaPeliculas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
                if (seleccionada != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Visualización");
                    alert.setHeaderText(null);
                    alert.setContentText("¿Deseas añadir una visualización a \"" + seleccionada.getitulo() + "\"?");
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            try {
                                peliculaService.agregarVisualizacion(seleccionada.getid());
                                cargarTabla();
                            } catch (PeliculaException e) {
                                mostrarError(e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    /** Agrega una nueva película con los datos del formulario. */
    @FXML
    private void onAgregar() {
        try {
            Pelicula nueva = new Pelicula(
                    txtId.getText().trim(),
                    txtTitulo.getText().trim(),
                    txtDirector.getText().trim(),
                    Integer.parseInt(txtDuracion.getText().trim()),
                    dpFecha.getValue()
            );
            peliculaService.agregarPelicula(nueva);
            cargarTabla();
            limpiarCampos();
        } catch (PeliculaException e) {
            mostrarError(e.getMessage());
        } catch (NumberFormatException e) {
            mostrarError("La duración debe ser un número entero.");
        }
    }

    /** Edita el título de la película seleccionada. */
    @FXML
    private void onEditarTitulo() {
        Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) { mostrarError("Selecciona una película."); return; }
        try {
            peliculaService.editarTitulo(seleccionada.getid(), txtTitulo.getText().trim());
            cargarTabla();
        } catch (PeliculaException e) {
            mostrarError(e.getMessage());
        }
    }

    /** Edita la fecha de publicación de la película seleccionada. */

    @FXML
    private void onEditarFecha() {
        Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) { mostrarError("Selecciona una película."); return; }
        try {
            peliculaService.editarFecha(seleccionada.getid(), dpFecha.getValue());
            cargarTabla();
        } catch (PeliculaException e) {
            mostrarError(e.getMessage());
        }
    }

    /** Elimina la película seleccionada. */
    @FXML
    private void onEliminar() {
        Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) { mostrarError("Selecciona una película."); return; }
        try {
            peliculaService.eliminarPelicula(seleccionada.getid());
            cargarTabla();
        } catch (PeliculaException e) {
            mostrarError(e.getMessage());
        }
    }

    /** Cierra la sesión y vuelve a la vista de login. */
    @FXML
    private void onCerrarSesion() {
        try {
            AppPelicula.cargarVista("/view/incio_view.fxml");
        } catch (Exception e) {
            mostrarError("Error al cerrar sesión: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtId.clear();
        txtTitulo.clear();
        txtDirector.clear();
        txtDuracion.clear();
        dpFecha.setValue(null);
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}