package project.desktoppet302;

import AnimationStates.animStates;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    public static void movePet(Pet desktopPet, TranslateTransition move, long then, long now) {
        // Generate two random numbers between -100 and 100 after eight seconds from previous generation or System Start.
        Random z = new Random();
        double x = (double) z.nextInt(200) - 100;
        //double y = (double) z.nextInt(200) - 100;
        // Set animation movement time.
        move.setDuration(Duration.seconds(2));
        // Set horizontal and vertical translation base on the random numbers generated.
        move.setByX(x);
        //move.setByY(y);
        // Based on whether the pet moves left or right, update animation state to show the pet walking in said direction.
        if (x > 0) {
            Pet.setPet(desktopPet, animStates.PetState.WALKLEFT);
        } else {
            Pet.setPet(desktopPet, animStates.PetState.WALKRIGHT);
        }
        // Play animation.
        move.play();
        // Set the old system time to the current time, allowing for repetition of animation timer.

    }
}
