package com.najocar.autoescuela.controllers;

import com.najocar.autoescuela.App;
import com.najocar.autoescuela.model.dao.AlumnoDAO;
import com.najocar.autoescuela.model.domain.Alumno;
import com.najocar.autoescuela.utils.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
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
    private Button infoAlumno;
    @FXML
    private TextField dniField;
    @FXML
    private TextField nombreField;
    @FXML
    private Label labelDni;
    @FXML
    private Label labelNombre;


    private ObservableList<Alumno> alumnos;

    private int positionInTable;

    private AlumnoDAO adao = new AlumnoDAO();

    ControlDni controlDni = ControlDni.getInstance();


    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Se inicia al comienzo de la aplicación
     * @throws SQLException
     */
    public void initialize() throws SQLException {

        removeButton.setDisable(true);
        updateButton.setDisable(true);
        infoAlumno.setDisable(true);

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

    /**
     * Close window
     * @param event
     */
    @FXML
    private void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * minimize window
     * @param event
     */
    @FXML
    private void minimizeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void generateTable() throws SQLException {
        List<Alumno> aux = adao.findAll();
        alumnos.setAll(aux);

        this.tabla.setItems(alumnos);
    }

    public void changeView() throws IOException {
        controlDni.setDni(alumnos.get(positionInTable).getDni());
        App.setRoot("alumnInfo");
    }

    @FXML
    public void insertAlumno(ActionEvent event) throws SQLException {
        Alumno alumno = new Alumno();
        if(!dniField.getText().isEmpty() && !nombreField.getText().isEmpty()) {
            if(validateDNI(dniField.getText())){
                alumno.setDni(dniField.getText().toUpperCase());
                alumno.setName(nombreField.getText());
                if (adao.findById(alumno.getDni()) == null){
                    alumnos.add(alumno);
                    adao.save(alumno);
                    Logger.info("Alumno registrado: " + alumno.toString());
                    clearFields();
                }else {
                    labelDni.setText("El DNI ya a sido registrado");
                    labelDni.setTextFill(Color.RED);
                }
            }else {
                labelDni.setText("DNI no válido");
                labelDni.setTextFill(Color.RED);
            }
        }
    }

    @FXML
    public void updateAlumno(ActionEvent event) throws SQLException {
        Alumno alumno = new Alumno();
        alumno.setDni(dniField.getText());
        alumno.setName(nombreField.getText());
        adao.save(alumno);
        Logger.info("Alumno actualizado: " + alumno.toString());
        alumnos.set(positionInTable, alumno);
        tabla.getSelectionModel().clearSelection();
        clearFields();

    }


    @FXML
    public void removeAlumno(ActionEvent event) throws SQLException {

        adao.delete(adao.findById(alumnos.get(positionInTable).getDni()));
        Logger.info("Alumno eliminado: " + alumnos.get(positionInTable).toString());
        alumnos.remove(alumnos.get(positionInTable));
        tabla.getSelectionModel().clearSelection();
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
        labelDni.setTextFill(Color.BLACK);

        if (alumno != null) {
            dniField.setText(alumno.getDni());
            nombreField.setText(alumno.getName());
            removeButton.setDisable(false);
            updateButton.setDisable(false);
            infoAlumno.setDisable(false);
            addButton.setDisable(true);
            dniField.setDisable(true);
            labelDni.setText("Dni");
            labelNombre.setText("Introduce un nuevo nombre");
        }
    }

    public void clearFields() {
        dniField.clear();
        nombreField.clear();
        removeButton.setDisable(true);
        updateButton.setDisable(true);
        infoAlumno.setDisable(true);
        addButton.setDisable(false);
        dniField.setDisable(false);
        labelDni.setText("Introduce un DNI");
        labelNombre.setText("Introduce un Nombre");
        labelDni.setTextFill(Color.BLACK);
    }

    public static boolean validateDNI(String dni) {
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        boolean valido = true;

        if (dni.length() != 9) {
            valido = false;
        } else {
            char letra = dni.charAt(8);
            int num = Integer.parseInt(dni.substring(0, 8));
            int resto = num % 23;
            char letraCalculada = letras.charAt(resto);
            if (Character.toUpperCase(letra) != letraCalculada) {
                valido = false;
            }
        }

        return valido;
    }

}
