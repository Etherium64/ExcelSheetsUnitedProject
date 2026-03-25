package com.esuproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PetApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PetApplication.class.getResource("Menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 720);
        stage.setScene(scene);
        stage.show();
    }


}