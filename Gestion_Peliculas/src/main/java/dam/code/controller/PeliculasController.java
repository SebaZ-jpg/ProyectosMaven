package dam.code.controller;

import dam.code.model.Pelicula;
import dam.code.service.PeliculaService;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javax.swing.table.TableColumn;

public class PeliculasController {

    @FXML private TableView<Pelicula> tablaPeliculas;
    @FXML private TableColumn<Pelicula,String> colId;
    @FXML private TableColumn<Pelicula, String> colTitulo;
    @FXML private TableColumn<Pelicula, String> colDirector;
    @FXML private TableColumn<Pelicula, Double> colDuracion;
    @FXML private TableColumn<Pelicula, String> colFecha;

    @FXML private TextField txtId;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtDirector;
    @FXML private TextField txtDuracion;
    @FXML private TextField txtFecha;

    private PeliculaService peliculaService;

    public void setPeliculaService(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
        tablaPeliculas.setItems(peliculaService.getPeliculas);
    }
}
