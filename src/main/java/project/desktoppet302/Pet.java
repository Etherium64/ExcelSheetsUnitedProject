package project.desktoppet302;

import AnimationStates.animStates;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Random;

public class Pet {

    // stores the current animation states
    private final animStates petStates;

    // stores the image view that displays the pet
    private final ImageView petImage;

    // creates a pet using animation states and an image view
    public Pet(animStates petStates, ImageView petImage) {
        this.petStates = petStates;
        this.petImage = petImage;
    }

    // changes the pet animation state
    public static void setPet(Pet desktopPet, animStates.PetState pStat) {
        desktopPet.petStates.setState(pStat);

        // updates the pet image every frame
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                desktopPet.petStates.update();
                desktopPet.petImage.setImage(desktopPet.petStates.getCurrentFrame());
            }
        }.start();
    }

    // moves the pet randomly left or right
    public static void movePet(Pet desktopPet,
                               TranslateTransition move,
                               long then,
                               long now,
                               Rectangle2D bounds,
                               StackPane imagebox) {

        // make a random x movement between -300 and 300
        Random random = new Random();
        double x = random.nextInt(600) - 300;

        // stop the pet from moving too far left
        if ((imagebox.getTranslateX() + x) < bounds.getMinX()) {
            x = -x;
        }

        // stop the pet from moving too far right
        else if ((imagebox.getTranslateX() + x) > bounds.getMaxX() - imagebox.getWidth() * 1.5) {
            x = -x;
        }

        // set how long the movement takes
        move.setDuration(Duration.seconds(2));

        // move the pet horizontally
        move.setByX(x);

        // choose the walking animation based on direction
        if (x > 0) {
            Pet.setPet(desktopPet, animStates.PetState.WALKLEFT);
        } else {
            Pet.setPet(desktopPet, animStates.PetState.WALKRIGHT);
        }

        // play the movement
        move.play();
    }
}