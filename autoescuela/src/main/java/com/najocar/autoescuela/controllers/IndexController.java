package com.najocar.autoescuela.controllers;

import com.najocar.autoescuela.model.dao.AlumnoDAO;
import com.najocar.autoescuela.model.domain.Alumno;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private TableColumn colDni;
    @FXML
    private TableColumn colNombre;
    @FXML
    private Pane navbar;
    @FXML
    private Button closeButton;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button updateButton;
    @FXML
    private TextField dniField;
    @FXML
    private TextField nombreField;

    private ObservableList<Alumno> alumnos;

    private int positionInTable;

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

    public void initialize() throws SQLException {

        removeButton.setDisable(true);
        updateButton.setDisable(true);

        navbar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        navbar.setOnMouseDragged(event -> {
            Stage stage = (Stage) navbar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        alumnos = FXCollections.observableArrayList();
        this.colDni.setCellValueFactory(new PropertyValueFactory("dni"));
        this.colNombre.setCellValueFactory(new PropertyValueFactory("name"));

        generateTable();

        final ObservableList<Alumno> tablaAlumnos = tabla.getSelectionModel().getSelectedItems();
        tablaAlumnos.addListener(selectAlumno);
    }

    @FXML
    public void insertAlumno(ActionEvent event) throws SQLException {
        Alumno alumno = new Alumno();
        alumno.setDni(dniField.getText());
        alumno.setName(nombreField.getText());
        alumnos.add(alumno);
        adao.save(alumno);
        clearFields();
    }

    @FXML
    public void updateAlumno(ActionEvent event) throws SQLException {
        Alumno alumno = new Alumno();
        alumno.setDni(dniField.getText());
        alumno.setName(nombreField.getText());
        adao.save(alumno);
        alumnos.set(positionInTable, alumno);
        clearFields();
        updateButton.setDisable(true);
    }

    @FXML
    public void generateTable() throws SQLException {
        List<Alumno> aux = adao.findAll();
        alumnos.setAll(aux);

        this.tabla.setItems(alumnos);
    }


    @FXML
    public void removeAlumno(ActionEvent event) throws SQLException {

        adao.delete(adao.findById(alumnos.get(positionInTable).getDni()));
        alumnos.remove(alumnos.get(positionInTable));
        clearFields();
    }

    private final ListChangeListener<Alumno> selectAlumno =
            new ListChangeListener<Alumno>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends Alumno> c) {
                    takeAlumnoSelected();
                }
            };

    public Alumno getAlumnoSelected() {
        if (tabla != null) {
            List<Alumno> selected = tabla.getSelectionModel().getSelectedItems();
            if (selected.size() == 1) {
                final Alumno alumnoSelected = selected.get(0);
                return alumnoSelected;
            }
        }
        return null;
    }

    private void takeAlumnoSelected() {
        final Alumno alumno = getAlumnoSelected();
        positionInTable = alumnos.indexOf(alumno);

        if (alumno != null) {
            dniField.setText(alumno.getDni());
            nombreField.setText(alumno.getName());
            removeButton.setDisable(false);
            updateButton.setDisable(false);
        }
    }

    public void clearFields() {
        dniField.clear();
        nombreField.clear();
    }


}
