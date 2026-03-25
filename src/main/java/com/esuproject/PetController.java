package com.esuproject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;


public class PetController {

    @FXML
    public Label MenuTitleText;

    @FXML
    public Label menuWelcomeText;

    @FXML
    public Button menuWelcomeButton;


    @FXML
    public void switchToCreateScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CreatePetMenu-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToPickScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CreatePet-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



}
