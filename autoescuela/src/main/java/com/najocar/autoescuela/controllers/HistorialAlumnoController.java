package com.najocar.autoescuela.controllers;

import com.najocar.autoescuela.App;
import com.najocar.autoescuela.model.dao.AlumnoDAO;
import com.najocar.autoescuela.model.dao.ClaseDAO;
import com.najocar.autoescuela.model.domain.Clase;
import com.najocar.autoescuela.model.domain.Inscripcion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class HistorialAlumnoController extends Controller {
    @FXML
    private Pane navbar;
    @FXML
    private Button closeButton;
    @FXML
    private Button addToB;
    @FXML
    private TableView<Inscripcion> tabla;
    @FXML
    private TableColumn colClase;
    @FXML
    private TableColumn colPrecio;
    @FXML
    private TableColumn colFecha;
    @FXML
    private Label totalPrice;
    @FXML
    private Label errorLabel;
    @FXML
    private Label labelNombre;
    @FXML
    private Label labelDni;
    @FXML
    private ChoiceBox choice;
    @FXML
    private Button choiceButton;

    private ObservableList<Inscripcion> clases;

    ControlDni controlDni = ControlDni.getInstance();

    private ClaseDAO cdao = new ClaseDAO();

    private AlumnoDAO adao = new AlumnoDAO();

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void goBack() throws IOException {
        App.setRoot("alumnInfo");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        navbar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        navbar.setOnMouseDragged(event -> {
            Stage stage = (Stage) navbar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        labelDni.setText(controlDni.getDni());
        try {
            labelNombre.setText(adao.findById(controlDni.getDni()).getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        clases = FXCollections.observableArrayList();
        this.colClase.setCellValueFactory(new PropertyValueFactory("name"));
        this.colPrecio.setCellValueFactory(new PropertyValueFactory("price"));
        this.colFecha.setCellValueFactory(new PropertyValueFactory("date"));

        try {
            generateTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // choicebox
        //generateChoiceBox();
    }

    @Override
    public void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @Override
    public void minimizeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void generateTable() throws SQLException {
        List<Inscripcion> aux = adao.alumnoAllClases(controlDni.getDni());
        double precioTotal = 0;
        clases.setAll(aux);
        for (Inscripcion clase : clases) {
            precioTotal += clase.getPrice();
        }
        totalPrice.setText(String.valueOf(precioTotal) + "€");
        this.tabla.setItems(clases);

    }

}
