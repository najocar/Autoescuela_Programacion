package com.najocar.autoescuela.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {
    @Override
    public abstract void initialize(URL url, ResourceBundle resourceBundle);

    /**
     * Close window
     * @param event
     */
    @FXML
    public abstract void closeWindow(ActionEvent event);

    /**
     * minimize window
     * @param event
     */
    @FXML
    public abstract void minimizeWindow(ActionEvent event);
}
