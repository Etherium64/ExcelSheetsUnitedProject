package org.example.desktoppet302;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import java.io.IOException;
import java.net.URL;

public class DesktopPet extends Application {

    // Stores x coordinate of where the mouse first clicked on the pet
    double dragOffsetX;

    // Sets size of window which holds the pet
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    double sceneSizeX = bounds.getWidth()/2;
    double sceneSizeY = bounds.getHeight()/2;
    @Override
    public void start(Stage stage) throws IOException {

        //Load FXML page for desktop pet.
        FXMLLoader fxmlLoader = new FXMLLoader(DesktopPet.class.getResource("pet-view.fxml"));
        // Create window to hold canvas.
        Scene scene = new Scene(fxmlLoader.load(), sceneSizeX, sceneSizeY);
        stage.setScene(scene);
        stage.setTitle("Desktop Pet");
        stage.setAlwaysOnTop(true);
        // Show window.
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}