package dam.code.controller;

import dam.code.AppPelicula;
import dam.code.exceptions.PeliculaException;
import dam.code.model.Pelicula;
import dam.code.model.Persona;
import dam.code.service.PeliculaService;
import javafx.beans.property.SimpleIntegerProperty;
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

    @FXML private TableView<Pelicula>              tablaPeliculas;
    @FXML private TableColumn<Pelicula, String>    colId;
    @FXML private TableColumn<Pelicula, String>    colTitulo;
    @FXML private TableColumn<Pelicula, String>    colDirector;
    @FXML private TableColumn<Pelicula, Integer>   colDuracion;
    @FXML private TableColumn<Pelicula, LocalDate> colFecha;
    @FXML private TableColumn<Pelicula, Integer>   colVisualizaciones;

    @FXML private TextField  txtId;
    @FXML private TextField  txtTitulo;
    @FXML private TextField  txtDirector;
    @FXML private TextField  txtDuracion;
    @FXML private DatePicker dpFecha;

    @FXML private TextField  txtNuevoTitulo;
    @FXML private DatePicker dpNuevaFecha;

    @FXML private Label lblUsuario;

    private final PeliculaService service = new PeliculaService();
    private final ObservableList<Pelicula> filas = FXCollections.observableArrayList();
    private Map<Pelicula, Integer> visualizacionesUsuario = new LinkedHashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Persona usuario = InicioController.getUsuarioActual();
        lblUsuario.setText("Sesión: " + usuario.getNombre() + " " + usuario.getApellido());

        colId.setCellValueFactory(c -> c.getValue().idProperty());
        colTitulo.setCellValueFactory(c -> c.getValue().tituloProperty());
        colDirector.setCellValueFactory(c -> c.getValue().directorProperty());
        colDuracion.setCellValueFactory(c -> c.getValue().duracionProperty().asObject());
        colFecha.setCellValueFactory(c -> c.getValue().fechaProperty());
        colVisualizaciones.setCellValueFactory(c -> {
            int veces = visualizacionesUsuario.getOrDefault(c.getValue(), 0);
            return new SimpleIntegerProperty(veces).asObject();
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
            String id       = txtId.getText().trim();
            String titulo   = txtTitulo.getText().trim();
            String director = txtDirector.getText().trim();
            String durStr   = txtDuracion.getText().trim();
            LocalDate fecha = dpFecha.getValue();

            if (id.isBlank() || titulo.isBlank() || director.isBlank()
                    || durStr.isBlank() || fecha == null) {
                throw new PeliculaException("Todos los campos son obligatorios.");
            }

            int duracion = Integer.parseInt(durStr);
            service.agregar(new Pelicula(id, titulo, director, duracion, fecha));
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
            Pelicula sel = getSeleccionada();
            service.editarTitulo(sel, txtNuevoTitulo.getText().trim());
            recargarTabla();
            mostrarInfo("Título actualizado.");
        } catch (PeliculaException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onEditarFecha() {
        try {
            Pelicula sel = getSeleccionada();
            service.editarFecha(sel, dpNuevaFecha.getValue());
            recargarTabla();
            mostrarInfo("Fecha actualizada.");
        } catch (PeliculaException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onEliminar() {
        try {
            Pelicula sel = getSeleccionada();
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar eliminación");
            confirm.setContentText("¿Eliminar \"" + sel.getitulo() + "\"?");
            Optional<ButtonType> res = confirm.showAndWait();
            if (res.isPresent() && res.get() == ButtonType.OK) {
                service.eliminar(sel);
                recargarTabla();
            }
        } catch (PeliculaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void onDobleClic(MouseEvent event) {
        if (event.getClickCount() != 2) return;
        Pelicula sel = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        try {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Registrar visualización");
            confirm.setContentText("¿Deseas añadir una visualización a \""
                    + sel.getitulo() + "\"?");
            Optional<ButtonType> res = confirm.showAndWait();
            if (res.isPresent() && res.get() == ButtonType.OK) {
                service.agregarVisualizacion(sel, InicioController.getUsuarioActual());
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
        AppPelicula.mostrarVista("view/inicio_view.fxml");
    }

    private Pelicula getSeleccionada() throws PeliculaException {
        Pelicula p = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (p == null) throw new PeliculaException("Selecciona una película de la tabla.");
        return p;
    }

    private void limpiarFormulario() {
        txtId.clear(); txtTitulo.clear(); txtDirector.clear();
        txtDuracion.clear(); dpFecha.setValue(null);
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