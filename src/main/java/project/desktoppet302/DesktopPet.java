package project.desktoppet302;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DesktopPet extends Application {

    // gets the usable screen bounds
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

    @Override
    public void start(Stage stage) throws IOException {

        // loads the desktop pet fxml
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/project.desktoppet302/pet-view.fxml")
        );

        // creates the transparent scene
        Scene scene = new Scene(fxmlLoader.load(), Color.TRANSPARENT);

        // sets up the stage
        stage.setScene(scene);
        stage.setTitle("Desktop Pet");

        // make the transparent window wide enough for the whole screen
        stage.setWidth(bounds.getWidth());

        // make the transparent window taller so the buttons above the pet are visible
        stage.setHeight(bounds.getHeight() / 3);

        // lock the transparent window to the bottom of the screen
        stage.setY(bounds.getMaxY() - stage.getHeight());
        stage.setX(bounds.getMinX());

        // make the window transparent and always above other windows
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);

        // show the pet
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}