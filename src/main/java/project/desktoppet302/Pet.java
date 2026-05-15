package project.desktoppet302;

import AnimationStates.animStates;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;

public class Pet {

    //
    private final animStates petStates;
    private final ImageView petImage;

    public Pet(animStates petStates, ImageView petImage) {
        this.petStates = petStates;
        this.petImage = petImage;
    }

    public static void setPet(Pet desktopPet, animStates.PetState pStat) {
        desktopPet.petStates.setState(pStat);
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                desktopPet.petStates.update();
                desktopPet.petImage.setImage(desktopPet.petStates.getCurrentFrame());
            }
        }.start();
    }

    public static void movePet(Pet desktopPet, TranslateTransition move, long then, long now, Rectangle2D bounds, VBox imagebox) {
        // Generate  random number between -100 and 100.
        Random z = new Random();
        double x = (double) z.nextInt(600) - 300;
        // If the number will cause the pet to drift past the left or right side bounds, set it to negative or vice versa.
        if ((imagebox.getTranslateX() + x) < (bounds.getMinX()))
        {
            x = - x;
        }
        else if ((imagebox.getTranslateX() + x) > (bounds.getMaxX() - imagebox.getWidth() * 1.5)) {
            x = - x;
        }
        // Set animation movement time.
        move.setDuration(Duration.seconds(2));
        // Set horizontal translation base on the random numbers generated.
        move.setByX(x);
        // Based on whether the pet moves left or right, update animation state to show the pet walking in said direction.
        if (x > 0) {
            Pet.setPet(desktopPet, animStates.PetState.WALKLEFT);
        } else {
            Pet.setPet(desktopPet, animStates.PetState.WALKRIGHT);
        }
        // Play animation.
        move.play();
    }
}
