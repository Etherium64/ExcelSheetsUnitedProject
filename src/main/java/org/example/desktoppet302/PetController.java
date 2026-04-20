package org.example.desktoppet302;

import javafx.fxml.FXML;
import javafx.scene.image.*;
import javafx.scene.layout.*;

import java.util.Objects;

public class PetController {



    @FXML
    private HBox canvas;

    @FXML
    private ImageView petImage;


    @FXML
    protected void onImageClick() {

    }
    @FXML
    public void intitialise() {
        petImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/pet.png")).toExternalForm()));
    }
}
