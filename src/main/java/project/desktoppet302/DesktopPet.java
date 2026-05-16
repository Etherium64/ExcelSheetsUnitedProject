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

    // gets the usable screen area
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

    @Override
    public void start(Stage stage) throws IOException {

        // load the desktop pet fxml
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/project.desktoppet302/pet-view.fxml")
        );

        // create a transparent scene
        Scene scene = new Scene(fxmlLoader.load(), Color.TRANSPARENT);

        // set the scene on the stage
        stage.setScene(scene);
        stage.setTitle("Desktop Pet");

        // make the transparent window screen width
        stage.setWidth(bounds.getWidth());

        // make it tall enough so the menu above the pet can be seen
        stage.setHeight(bounds.getHeight() / 3);

        // lock the window to the bottom of the screen
        stage.setY(bounds.getMaxY() - stage.getHeight());
        stage.setX(bounds.getMinX());

        // remove normal window borders
        stage.initStyle(StageStyle.TRANSPARENT);

        // keep the pet above other windows
        stage.setAlwaysOnTop(true);

        // show the window
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}