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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class IndexController extends Controller{
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
    @FXML
    private TextField fieldBuscar;


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
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
    public void generateTable() {
        List<Alumno> aux = null;
        try {
            aux = adao.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        alumnos.setAll(aux);

        this.tabla.setItems(alumnos);
    }

    public void changeView() {
        controlDni.setDni(alumnos.get(positionInTable).getDni());
        try {
            App.setRoot("alumnInfo");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeViewToClases() {
        try {
            App.setRoot("clases");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void insertAlumno(ActionEvent event){
        Alumno alumno = new Alumno();
        if(!dniField.getText().isEmpty() && !nombreField.getText().isEmpty()) {
            if(validateDNI(dniField.getText())){
                if (nombreField.getText().length() > 50){
                    labelNombre.setText("El nombre es demasiado largo");
                    labelNombre.setTextFill(Color.RED);
                }else{
                    alumno.setDni(dniField.getText().toUpperCase());
                    alumno.setName(nombreField.getText());
                    try {
                        if (adao.findById(alumno.getDni()) == null){
                            alumnos.add(alumno);
                            adao.save(alumno);
                            Logger.info("Alumno registrado: " + alumno.toString());
                            clearFields();
                        }else {
                            labelDni.setText("El DNI ya a sido registrado");
                            labelDni.setTextFill(Color.RED);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }else {
                labelDni.setText("DNI no válido");
                labelDni.setTextFill(Color.RED);
            }
        }
    }

    @FXML
    public void updateAlumno(ActionEvent event) {
        if (nombreField.getText().length() > 50){
            labelNombre.setText("El nombre es demasiado largo");
            labelNombre.setTextFill(Color.RED);
        }else{
            Alumno alumno = new Alumno();
            alumno.setDni(dniField.getText());
            alumno.setName(nombreField.getText());
            try {
                adao.save(alumno);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Logger.info("Alumno actualizado: " + alumno.toString());
            alumnos.set(positionInTable, alumno);
            tabla.getSelectionModel().clearSelection();
            clearFields();
        }
    }


    @FXML
    public void removeAlumno(ActionEvent event) {

        try {
            adao.delete(adao.findById(alumnos.get(positionInTable).getDni()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Logger.info("Alumno eliminado: " + alumnos.get(positionInTable).toString());
        alumnos.remove(alumnos.get(positionInTable));
        tabla.getSelectionModel().clearSelection();
        clearFields();

    }
    @FXML
    public void keyPressed() {
        this.tabla.getSelectionModel().clearSelection();
        clearFields();
    }

    @FXML
    public void buscar () throws SQLException {
        if (fieldBuscar.getText().isEmpty()) {
            generateTable();
        } else {
            ObservableList<Alumno> aux = FXCollections.observableArrayList();
            aux.setAll(alumnos);

            for (Alumno alumno : alumnos) {
                for (int i = 0; i < fieldBuscar.getText().length(); i++) {
                    if (alumno.getDni().charAt(i) != fieldBuscar.getText().charAt(i)) {
                        if (aux.contains(alumno)){
                            aux.remove(alumno);
                        }
                    }
                    /*else{
                        //System.out.println("no coincide");
                        //System.out.println(alumnos.contains(alumno));
                        if (alumnos.contains(alumno) == false){
                            System.out.println("no tiene alumno");
                            alumnos.add(alumno);
                        }
                    }

                     */
                }
            }

            this.tabla.setItems(aux);
        }
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
        labelNombre.setTextFill(Color.BLACK);

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
        labelNombre.setTextFill(Color.BLACK);
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
