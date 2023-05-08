package com.najocar.autoescuela.controllers;

import com.najocar.autoescuela.App;
import com.najocar.autoescuela.model.dao.AlumnoDAO;
import com.najocar.autoescuela.model.dao.ClaseDAO;
import com.najocar.autoescuela.model.domain.Alumno;
import com.najocar.autoescuela.model.domain.Clase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AlumnInfoController {
    @FXML
    private Pane navbar;
    @FXML
    private Button closeButton;
    @FXML
    private Button addToB;
    @FXML
    private TableView<Clase> tabla;
    @FXML
    private TableColumn colClase;
    @FXML
    private TableColumn colPrecio;
    @FXML
    private Label totalPrice;
    @FXML
    private Label errorLabel;

    private ObservableList<Clase> clases;

    ControlDni controlDni = ControlDni.getInstance();

    private ClaseDAO cdao = new ClaseDAO();

    private AlumnoDAO adao = new AlumnoDAO();

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void minimizeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void returnMain() throws IOException {
        App.setRoot("index");
    }

    public void initialize() throws SQLException {

        navbar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        navbar.setOnMouseDragged(event -> {
            Stage stage = (Stage) navbar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        clases = FXCollections.observableArrayList();
        this.colClase.setCellValueFactory(new PropertyValueFactory("name"));
        this.colPrecio.setCellValueFactory(new PropertyValueFactory("price"));

        generateTable();

    }

    @FXML
    public void generateTable() throws SQLException {
        List<Clase> aux = adao.alumnoAllClases(controlDni.getDni());
        double precioTotal = 0;
        clases.setAll(aux);
        for (Clase clase : clases) {
            precioTotal += clase.getPrice();
        }
        totalPrice.setText(String.valueOf(precioTotal) + "€");
        this.tabla.setItems(clases);
    }

    @FXML
    public void addToB() throws SQLException {
        if (!clases.contains(cdao.findById(2))) {
            cdao.insertAlumno(2, controlDni.getDni());
            generateTable();
        } else {
            error(1);
        }
    }

    @FXML
    public void removeFromB() throws SQLException {
        if (clases.contains(cdao.findById(2))) {
            quitError();
            cdao.removeAlumno(2, controlDni.getDni());
            generateTable();
        } else {
            //
        }
    }

    public void error(int n) {
        switch (n) {
            case 1:
                errorLabel.setText("Ya está inscrito a esta clase");
                errorLabel.setVisible(true);
                setTimeout(() -> errorLabel.setVisible(false), 1000);
                break;
        }
    }

    public void quitError(){
        errorLabel.setVisible(false);
    }

    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }

}
