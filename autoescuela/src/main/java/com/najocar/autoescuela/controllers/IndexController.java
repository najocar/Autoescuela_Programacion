package com.najocar.autoescuela.controllers;

import com.najocar.autoescuela.model.dao.AlumnoDAO;
import com.najocar.autoescuela.model.domain.Alumno;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class IndexController {
    @FXML
    private TableView<Alumno> tabla;
    @FXML
    private TableColumn <Alumno, String> Dni;
    @FXML
    private TableColumn <Alumno, String> Nombre;
    @FXML
    private Pane navbar;
    @FXML
    private Button closeButton;


    private double xOffset = 0;
    private double yOffset = 0;


    @FXML
    private void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void initialize() {
        // Agregar controlador de eventos onMousePressed al nodo raíz
        navbar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        // Agregar controlador de eventos onMouseDragged al nodo raíz
        navbar.setOnMouseDragged(event -> {
            Stage stage = (Stage) navbar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

    }

    /*
    public void initialize(URL url, ResourceBundle resourceBundle) throws SQLException {

        navbar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        navbar.setOnMouseDragged(event -> {
            Stage stage = (Stage) navbar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        this.Dni.setCellValueFactory(new PropertyValueFactory("dni"));
        this.Nombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        System.out.println("hola");
        AlumnoDAO adao = new AlumnoDAO();
        List<Alumno> items = adao.findAll();
        this.tabla.setItems((ObservableList<Alumno>) items);
    }

     */





}
