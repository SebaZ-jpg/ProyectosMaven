package dam.code.service;

import dam.code.exceptions.PeliculaException;
import dam.code.model.Pelicula;
import dam.code.model.Persona;
import dam.code.persistence.JsonManager;
import dam.code.repository.PeliculaRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.*;

public class PeliculaService {

    private final ObservableList<Pelicula> visualizaciones;
    private final PeliculaRepository repository;

    public PeliculaService(PeliculaRepository repository) {
        this.repository = repository;

        visualizaciones = FXCollections.observableArrayList(repository.cargar());
    }

    public ObservableList<Pelicula> getPeliculas() {
        return visualizaciones;
    }

