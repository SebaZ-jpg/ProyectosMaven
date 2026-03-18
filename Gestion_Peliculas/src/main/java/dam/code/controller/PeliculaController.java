package dam.code.controller;

import dam.code.AppPelicula;
import dam.code.exceptions.PeliculaException;
import dam.code.model.Pelicula;
import dam.code.model.Persona;
import dam.code.service.PeliculaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class PeliculaController implements Initializable {

    // ── Tabla ──────────────────────────────────────────
    @FXML private TableView<Pelicula> tablaPeliculas;
    @FXML private TableColumn<Pelicula, String> colId;
    @FXML private TableColumn<Pelicula, String> colTitulo;
    @FXML private TableColumn<Pelicula, String> colDirector;
    @FXML private TableColumn<Pelicula, Integer> colDuracion;
    @FXML private TableColumn<Pelicula, LocalDate> colFecha;
    @FXML private TableColumn<Pelicula, Integer> colVisualizaciones;

    // ── Formulario agregar ─────────────────────────────
    @FXML private TextField  tfId;
    @FXML private TextField  tfTitulo;
    @FXML private TextField  tfDirector;
    @FXML private TextField  tfDuracion;
    @FXML private DatePicker dpFecha;

    // ── Edición ────────────────────────────────────────
    @FXML private TextField  tfNuevoTitulo;
    @FXML private DatePicker dpNuevaFecha;

    @FXML private Label lblUsuario;

    // ── Estado interno ─────────────────────────────────
    private final PeliculaService service = new PeliculaService();
    private final ObservableList<Pelicula> filas = FXCollections.observableArrayList();
    private Map<Pelicula, Integer> visualizacionesUsuario = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Persona usuario = InicioController.getUsuarioActual();
        lblUsuario.setText("Sesión: " + usuario.getNombre() + " " + usuario.getApellido());

        // Enlazar columnas usando JavaFX Properties del modelo
        colId.setCellValueFactory(c -> c.getValue().idProperty());
        colTitulo.setCellValueFactory(c -> c.getValue().tituloProperty());
        colDirector.setCellValueFactory(c -> c.getValue().directorProperty());
        colDuracion.setCellValueFactory(c -> c.getValue().duracionProperty().asObject());
        colFecha.setCellValueFactory(c -> c.getValue().fechaProperty());

        // Columna visualizaciones calculada desde el mapa
        colVisualizaciones.setCellValueFactory(c -> {
            int veces = visualizacionesUsuario.getOrDefault(c.getValue(), 0);
            return new javafx.beans.property.SimpleIntegerProperty(veces).asObject();
        });

        tablaPeliculas.setItems(filas);
        tablaPeliculas.setOnMouseClicked(this::onDobleClic);

        recargarTabla();
    }

    private void recargarTabla() {
        Persona usuario = InicioController.getUsuarioActual();
        visualizacionesUsuario = service.getVisualizaciones(usuario);
        filas.setAll(visualizacionesUsuario.keySet());
    }

    @FXML
    private void onAgregar() {
        try {
            String id       = tfId.getText().trim();
            String titulo   = tfTitulo.getText().trim();
            String director = tfDirector.getText().trim();
            String durStr   = tfDuracion.getText().trim();
            LocalDate fecha = dpFecha.getValue();

            if (id.isBlank() || titulo.isBlank() || director.isBlank() || durStr.isBlank() || fecha == null) {
                throw new PeliculaException("Todos los campos son obligatorios.");
            }

            int duracion = Integer.parseInt(durStr);
            Pelicula p = new Pelicula(id, titulo, director, duracion, fecha);
            service.agregar(p);
            limpiarFormulario();
            recargarTabla();
            mostrarInfo("Película agregada correctamente.");

        } catch (NumberFormatException e) {
            mostrarError("La duración debe ser un número entero.");
        } catch (PeliculaException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onEditarTitulo() {
        try {
            Pelicula seleccionada = getSeleccionada();
            String nuevoTitulo = tfNuevoTitulo.getText().trim();
            service.editarTitulo(seleccionada, nuevoTitulo);
            recargarTabla();
            mostrarInfo("Título actualizado.");
        } catch (PeliculaException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onEditarFecha() {
        try {
            Pelicula seleccionada = getSeleccionada();
            service.editarFecha(seleccionada, dpNuevaFecha.getValue());
            recargarTabla();
            mostrarInfo("Fecha actualizada.");
        } catch (PeliculaException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onEliminar() {
        try {
            Pelicula seleccionada = getSeleccionada();
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar eliminación");
            confirm.setContentText("¿Eliminar \"" + seleccionada.getitulo() + "\"?");
            Optional<ButtonType> res = confirm.showAndWait();
            if (res.isPresent() && res.get() == ButtonType.OK) {
                service.eliminar(seleccionada);
                recargarTabla();
            }
        } catch (PeliculaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void onDobleClic(MouseEvent event) {
        if (event.getClickCount() != 2) return;
        Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        try {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Registrar visualización");
            confirm.setContentText("¿Añadir una visualización a \"" + seleccionada.getitulo() + "\"?");
            Optional<ButtonType> res = confirm.showAndWait();
            if (res.isPresent() && res.get() == ButtonType.OK) {
                service.agregarVisualizacion(seleccionada, InicioController.getUsuarioActual());
                recargarTabla();
                mostrarInfo("Visualización registrada.");
            }
        } catch (PeliculaException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onCerrarSesion() {
        InicioController.cerrarSesion();
        AppPelicula.mostrarVista("/dam/code/view/login_view.fxml");
    }

    // ── Helpers ────────────────────────────────────────

    private Pelicula getSeleccionada() throws PeliculaException {
        Pelicula p = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (p == null) throw new PeliculaException("Selecciona una película de la tabla.");
        return p;
    }

    private void limpiarFormulario() {
        tfId.clear(); tfTitulo.clear(); tfDirector.clear();
        tfDuracion.clear(); dpFecha.setValue(null);
    }

    private void mostrarError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error"); a.setHeaderText(null);
        a.setContentText(msg); a.showAndWait();
    }

    private void mostrarInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Info"); a.setHeaderText(null);
        a.setContentText(msg); a.showAndWait();
    }
}