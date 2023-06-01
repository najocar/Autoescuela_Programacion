package com.najocar.autoescuela.controllers;

import com.najocar.autoescuela.App;
import com.najocar.autoescuela.model.dao.AlumnoDAO;
import com.najocar.autoescuela.model.dao.ClaseDAO;
import com.najocar.autoescuela.model.domain.Clase;
import com.najocar.autoescuela.utils.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

public class ClasesController extends Controller {
    @FXML
    private TableView<Clase> tabla;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colPrecio;
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
    private TextField precioField;
    @FXML
    private TextField nombreField;
    @FXML
    private Label labelDni;
    @FXML
    private Label labelNombre;
    @FXML
    private TextField fieldBuscar;


    private ObservableList<Clase> clases;

    private int positionInTable;

    private AlumnoDAO adao = new AlumnoDAO();

    private ClaseDAO cdao = new ClaseDAO();

    ControlDni controlDni = ControlDni.getInstance();


    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Se inicia al comienzo de la aplicación
     *
     * @throws SQLException
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

        clases = FXCollections.observableArrayList();
        this.colNombre.setCellValueFactory(new PropertyValueFactory("name"));
        this.colPrecio.setCellValueFactory(new PropertyValueFactory("price"));

        generateTable();

        final ObservableList<Clase> tablaClases = tabla.getSelectionModel().getSelectedItems();
        tablaClases.addListener(selectClase);
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
        List<Clase> aux = null;
        try {
            aux = cdao.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clases.setAll(aux);

        this.tabla.setItems(clases);
    }

    public void changeViewToIndex() {
        try {
            App.setRoot("index");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void insertClase(ActionEvent event) {
        Clase clase = new Clase();
        if (!precioField.getText().isEmpty() && !nombreField.getText().isEmpty()) {
            if (nombreField.getText().length() > 50) {
                labelNombre.setText("El nombre es demasiado largo");
                labelNombre.setTextFill(Color.RED);
            } else {
                clase.setPrice(Double.parseDouble(precioField.getText()));
                clase.setName(nombreField.getText());
                try {
                    if (cdao.findById(clase.getName()) == null) {
                        clases.add(clase);
                        cdao.save(clase);
                        Logger.info("Clase registrada: " + clase.toString());
                        clearFields();
                    } else {
                        labelDni.setText("Clase ya existente");
                        labelDni.setTextFill(Color.RED);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @FXML
    public void updateClase(ActionEvent event) {
        if (nombreField.getText().length() > 50) {
            labelNombre.setText("El nombre es demasiado largo");
            labelNombre.setTextFill(Color.RED);
        } else {
            Clase clase = new Clase();
            clase.setPrice(Double.parseDouble(precioField.getText()));
            clase.setName(nombreField.getText());
            try {
                cdao.save(clase);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Logger.info("Clase actualizada: " + clase.toString());
            clases.set(positionInTable, clase);
            tabla.getSelectionModel().clearSelection();
            clearFields();
        }
    }


    @FXML
    public void removeClase(ActionEvent event) {

        try {
            cdao.delete(cdao.findById(clases.get(positionInTable).getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Logger.info("clase eliminada: " + clases.get(positionInTable).toString());
        clases.remove(clases.get(positionInTable));
        tabla.getSelectionModel().clearSelection();
        clearFields();

    }

    @FXML
    public void keyPressed() {
        this.tabla.getSelectionModel().clearSelection();
        clearFields();
    }

    private final ListChangeListener<Clase> selectClase =
            new ListChangeListener<Clase>() {
                @Override
                public void onChanged(Change<? extends Clase> c) {
                    takeClaseSelected();
                }
            };

    public Clase getClaseSelected() {
        if (tabla != null) {
            List<Clase> selected = tabla.getSelectionModel().getSelectedItems();
            if (selected.size() == 1) {
                final Clase claseSelected = selected.get(0);
                return claseSelected;
            }
        }
        return null;
    }

    private void takeClaseSelected() {
        final Clase clase = getClaseSelected();
        positionInTable = clases.indexOf(clase);
        labelDni.setTextFill(Color.BLACK);
        labelNombre.setTextFill(Color.BLACK);

        if (clase != null) {
            precioField.setText(String.valueOf(clase.getPrice()));
            nombreField.setText(clase.getName());
            removeButton.setDisable(false);
            updateButton.setDisable(false);
            addButton.setDisable(true);
            nombreField.setDisable(true);
            labelDni.setText("Nombre");
            labelNombre.setText("Introduce un nuevo Precio");
        }
    }

    public void clearFields() {
        precioField.clear();
        nombreField.clear();
        removeButton.setDisable(true);
        updateButton.setDisable(true);
        addButton.setDisable(false);
        nombreField.setDisable(false);
        labelDni.setText("Introduce un Nombre");
        labelNombre.setText("Introduce un Precio");
        labelDni.setTextFill(Color.BLACK);
        labelNombre.setTextFill(Color.BLACK);
    }

}
