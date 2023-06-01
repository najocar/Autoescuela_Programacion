package com.najocar.autoescuela.controllers;

import com.najocar.autoescuela.App;
import com.najocar.autoescuela.model.dao.ClaseDAO;
import com.najocar.autoescuela.model.domain.Alumno;
import com.najocar.autoescuela.model.domain.Clase;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PrecioClasesController extends Controller {
    @FXML
    private Pane navbar;
    @FXML
    private Button closeButton;
    @FXML
    private Button saveChanges;
    @FXML
    private Label labelB;
    @FXML
    private Label labelA;
    @FXML
    private Label labelA2;
    @FXML
    private Label labelA1;
    @FXML
    private TextField inputB;
    @FXML
    private TextField inputA;
    @FXML
    private TextField inputA2;
    @FXML
    private TextField inputA1;
    @FXML
    private Label errorB;
    @FXML
    private Label errorA;
    @FXML
    private Label errorA2;
    @FXML
    private Label errorA1;
    private double xOffset = 0;
    private double yOffset = 0;

    ClaseDAO cdao = new ClaseDAO();

    @FXML
    public void returnMain() throws IOException {
        App.setRoot("index");
    }

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

        try {
            setOldPrice();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public void saveChanges() throws SQLException {
        Clase clase;
        if (checkFields()) {
            if (!inputB.getText().isEmpty()){
                clase = cdao.findById(1);
                clase.setPrice(Double.parseDouble(inputB.getText()));
                cdao.save(clase);
            }
            if (!inputA.getText().isEmpty()){
                clase = cdao.findById(2);
                clase.setPrice(Double.parseDouble(inputB.getText()));
                cdao.save(clase);
            }
            if (!inputA2.getText().isEmpty()){
                clase = cdao.findById(3);
                clase.setPrice(Double.parseDouble(inputB.getText()));
                cdao.save(clase);
            }
            if (!inputA1.getText().isEmpty()){
                clase = cdao.findById(4);
                clase.setPrice(Double.parseDouble(inputB.getText()));
                cdao.save(clase);
            }
            clearFields();
            setOldPrice();
            disableError();
        }
    }

    public Boolean checkFields() {
        if (checkB() && checkA() && checkA2() && checkA1()) {
            return true;
        }
        return false;
    }

    public Boolean checkB() {
        if (inputB.getText().isEmpty()) {
            return true;
        }
        try {
            if (Double.parseDouble(inputB.getText()) < 0) {
                inputError(inputB);
                return false;
            }
            Double value = Double.parseDouble(inputB.getText());
            return true;
        } catch (NumberFormatException e) {
            inputError(inputB);
            return false;
        }
    }

    public Boolean checkA() {
        if (inputA.getText().isEmpty()) {
            return true;
        }
        try {
            if (Double.parseDouble(inputA.getText()) < 0) {
                inputError(inputA);
                return false;
            }
            Double value = Double.parseDouble(inputA.getText());
            return true;
        } catch (NumberFormatException e) {
            inputError(inputA);
            return false;
        }
    }

    public Boolean checkA2() {
        if (inputA2.getText().isEmpty()) {
            return true;
        }
        try {
            if (Double.parseDouble(inputA2.getText()) < 0) {
                inputError(inputA2);
                return false;
            }
            Double value = Double.parseDouble(inputA2.getText());
            return true;
        } catch (NumberFormatException e) {
            inputError(inputA2);
            return false;
        }
    }

    public Boolean checkA1() {
        if (inputA1.getText().isEmpty()) {
            return true;
        }
        try {
            if (Double.parseDouble(inputA1.getText()) < 0) {
                inputError(inputA1);
                return false;
            }
            Double value = Double.parseDouble(inputA1.getText());
            return true;
        } catch (NumberFormatException e) {
            inputError(inputA1);
            return false;
        }
    }

    @FXML
    public void inputError(TextField field) {
        switch (field.getId()) {
            case "inputB":
                errorB.setVisible(true);
                break;
            case "inputA":
                errorA.setVisible(true);
                break;
            case "inputA2":
                errorA2.setVisible(true);
                break;
            case "inputA1":
                errorA1.setVisible(true);
                break;
        }
    }

    public void disableError() {
        if (errorB.isVisible()){
            errorB.setVisible(false);
        }
        if (errorA.isVisible()){
            errorA.setVisible(false);
        }
        if (errorA2.isVisible()){
            errorA2.setVisible(false);
        }
        if (errorA1.isVisible()){
            errorA1.setVisible(false);
        }
    }

    public void clearFields() {
        inputB.clear();
        inputA.clear();
        inputA2.clear();
        inputA1.clear();
    }

    public void setOldPrice() throws SQLException {
        labelB.setText(String.valueOf(cdao.findById(1).getPrice()));
        labelA.setText(String.valueOf(cdao.findById(2).getPrice()));
        labelA2.setText(String.valueOf(cdao.findById(3).getPrice()));
        labelA1.setText(String.valueOf(cdao.findById(4).getPrice()));
    }
}
