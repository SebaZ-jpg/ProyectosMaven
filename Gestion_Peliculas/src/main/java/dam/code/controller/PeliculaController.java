package dam.code.controller;

import dam.code.AppPelicula;
import dam.code.exceptions.PeliculaException;
import dam.code.model.Pelicula;
import dam.code.model.Persona;
import dam.code.service.PeliculaService;
import javafx.beans.property.*;
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
    @FXML private TableView<FilaPelicula> tablaPeliculas;
    @FXML private TableColumn<FilaPelicula,String>colId;
    @FXML private TableColumn<FilaPelicula,String>colTitulo;
    @FXML private TableColumn<FilaPelicula,String>colDirector;
    @FXML private TableColumn<FilaPelicula,Double>colDuracion;
    @FXML private TableColumn<FilaPelicula,LocalDate>colFecha;
    @FXML private TableColumn<FilaPelicula,Integer>colVisualizaciones;

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
    private final ObservableList<FilaPelicula> filas = FXCollections.observableArrayList();

    // ── Clase auxiliar de fila ─────────────────────────
    public static class FilaPelicula {
        private final SimpleStringProperty id;
        private final SimpleStringProperty titulo;
        private final SimpleStringProperty director;
        private final SimpleDoubleProperty duracion;
        private final SimpleObjectProperty<LocalDate> fecha;
        private final SimpleIntegerProperty visualizaciones;
        private final Pelicula pelicula;

        public FilaPelicula(Pelicula p, int v) {
            this.pelicula = p;
            this.id = new SimpleStringProperty(p.getId());
            this.titulo = new SimpleStringProperty(p.getTitulo());
            this.director = new SimpleStringProperty(p.getDirector());
            this.duracion = new SimpleDoubleProperty(p.getDuracion());
            this.fecha = new SimpleObjectProperty<>(p.getFechaPublicacion());
            this.visualizaciones = new SimpleIntegerProperty(v);
        }

        public String getId(){
            return id.get();
        }

        public String getTitulo(){
            return titulo.get();
        }

        public String getDirector(){
            return director.get();
        }

        public Double getDuracion(){
            return duracion.get();
        }

        public LocalDate getFecha(){
            return fecha.get();
        }

        public int getVisualizaciones(){
            return visualizaciones.get();
        }

        public Pelicula getPelicula(){
            return pelicula;
        }

        public SimpleStringProperty idProperty(){
            return id;
        }

        public SimpleStringProperty tituloProperty(){
            return titulo;
        }

        public SimpleStringProperty directorProperty(){
            return director;
        }

        public SimpleDoubleProperty duracionProperty(){
            return duracion;
        }

        public SimpleObjectProperty<LocalDate>fechaProperty(){
            return fecha;
        }

        public SimpleIntegerProperty visualizacionesProperty(){
            return visualizaciones;
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Persona usuario = InicioController.getUsuarioActual();
        lblUsuario.setText("Sesión: " + usuario.getNombre() + " " + usuario.getApellido());

        // Enlazar columnas con propiedades
        colId.setCellValueFactory(c -> c.getValue().idProperty());
        colTitulo.setCellValueFactory(c -> c.getValue().tituloProperty());
        colDirector.setCellValueFactory(c -> c.getValue().directorProperty());
        colDuracion.setCellValueFactory(c -> c.getValue().duracionProperty().asObject());
        colFecha.setCellValueFactory(c -> c.getValue().fechaProperty());
        colVisualizaciones.setCellValueFactory(c -> c.getValue().visualizacionesProperty().asObject());

        tablaPeliculas.setItems(filas);
        tablaPeliculas.setOnMouseClicked(this::onDobleClic);

        recargarTabla();
    }

    private void recargarTabla() {
        filas.clear();
        service.getVisualizaciones().forEach((p, v) -> filas.add(new FilaPelicula(p, v)));
    }

    @FXML
    private void onAgregar() {
        try {
            String id = tfId.getText().trim();
            String titulo = tfTitulo.getText().trim();
            String director = tfDirector.getText().trim();
            String durStr = tfDuracion.getText().trim();
            LocalDate fecha = dpFecha.getValue();

            if (titulo.isBlank() || director.isBlank() || durStr.isBlank() || fecha == null) {
                throw new PeliculaException("Todos los campos son obligatorios.");
            }

            int duracion = Integer.parseInt(durStr);
            Pelicula p = new Pelicula(id, titulo, director, duracion, fecha);
            service.agregar(p);
            limpiarFormulario();
            recargarTabla();
            mostrarInfo("Película agregada correctamente.");

        } catch (NumberFormatException e) {
            mostrarError("La duración debe ser un número válido.");
        } catch (PeliculaException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onEditarTitulo() {
        try {
            FilaPelicula fila = getSeleccionada();
            service.editarTitulo(fila.getPelicula(), tfNuevoTitulo.getText().trim());
            recargarTabla();
            mostrarInfo("Título actualizado.");
        } catch (PeliculaException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onEditarFecha() {
        try {
            FilaPelicula fila = getSeleccionada();
            service.editarFecha(fila.getPelicula(), dpNuevaFecha.getValue());
            recargarTabla();
            mostrarInfo("Fecha actualizada.");
        } catch (PeliculaException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onEliminar() {
        try {
            FilaPelicula fila = getSeleccionada();
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar eliminación");
            confirm.setContentText("¿Eliminar \"" + fila.getTitulo() + "\"?");
            Optional<ButtonType> res = confirm.showAndWait();
            if (res.isPresent() && res.get() == ButtonType.OK) {
                service.eliminar(fila.getPelicula());
                recargarTabla();
            }
        } catch (PeliculaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void onDobleClic(MouseEvent event) {
        if (event.getClickCount() != 2) return;
        FilaPelicula fila = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (fila == null) return;
        try {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Registrar visualización");
            confirm.setContentText("¿Añadir una visualización a \"" + fila.getTitulo() + "\"?");
            Optional<ButtonType> res = confirm.showAndWait();
            if (res.isPresent() && res.get() == ButtonType.OK) {
                service.agregarVisualizacion(fila.getPelicula(), InicioController.getUsuarioActual());
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

    private FilaPelicula getSeleccionada() throws PeliculaException {
        FilaPelicula fila = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (fila == null) throw new PeliculaException("Selecciona una película de la tabla.");
        return fila;
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