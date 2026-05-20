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

    // stores the pet animation states
    private final animStates petStates;

    // stores the image view used to show the pet
    private final ImageView petImage;

    // check if moving
    private boolean isMoving = false;
    private long lastInteractionTime = System.currentTimeMillis();
    private static final long SAD_IDLE_DELAY = 10_000; // 10 seconds

    // creates the pet object
    public Pet(animStates petStates, ImageView petImage) {
        this.petStates = petStates;
        this.petImage = petImage;

        // added by nate new start for single animation loop
        new AnimationTimer(){
            @Override
            public void handle(long now){
                checkIdleState(now);
                petStates.update(now);
                petImage.setImage(petStates.getCurrentFrame());
            }
        }.start();
    }

    // added by nate track interactions

    private void markInteraction() {
        lastInteractionTime = System.currentTimeMillis();
    }

    // added by nate switch between idles

    private void checkIdleState(long now) {

        long elapsed = System.currentTimeMillis() - lastInteractionTime;

        if (isMoving) return;

        if (elapsed > SAD_IDLE_DELAY) {

            if (!petStates.isSadIdle()) {
                petStates.setSadIdle();
            }

        } else {

            if (!petStates.isIdle()) {
                petStates.setIdle();
            }
        }
    }


    // added by Nate replacing enums now we use he state method directly
    public void setIdle()     { petStates.setIdle(); }
    public void setWalkLeft() { petStates.setWalkLeft(); }
    public void setWalkRight(){ petStates.setWalkRight(); }
    public void setShock()    { petStates.setShock(); markInteraction(); }
    public void setSadIdle()  { petStates.setSadIdle(); }


    // added by nate moves pet randomly left or right
    public static void movePet(Pet desktopPet,
                               TranslateTransition move,
                               long then,
                               long now,
                               Rectangle2D bounds,
                               StackPane imagebox) {

        desktopPet.isMoving = true;

        Random random = new Random();
        double x = random.nextInt(600) - 300;

        if ((imagebox.getTranslateX() + x) < bounds.getMinX()) {
            x = -x;
        } else if ((imagebox.getTranslateX() + x) > bounds.getMaxX() - imagebox.getWidth() * 1.5) {
            x = -x;
        }

        move.setDuration(Duration.seconds(2));
        move.setByX(x);

        if (x > 0) {
            desktopPet.setWalkLeft();
        } else {
            desktopPet.setWalkRight();
        }

        move.setOnFinished(e -> {
            desktopPet.isMoving = false;
        });

        move.play();
    }
}

//    // changes the current pet animation
//    public static void setPet(Pet desktopPet, animStates.PetState pStat) {
//        desktopPet.petStates.setState(pStat);
//
//        // updates the image frame for the pet animation
//        new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                desktopPet.petStates.update();
//                desktopPet.petImage.setImage(desktopPet.petStates.getCurrentFrame());
//            }
//        }.start();
//    }
//
//    // moves the pet randomly left or right
//    public static void movePet(Pet desktopPet,
//                               TranslateTransition move,
//                               long then,
//                               long now,
//                               Rectangle2D bounds,
//                               StackPane imagebox) {
//
//        // make a random x movement between -300 and 300
//        Random random = new Random();
//        double x = random.nextInt(600) - 300;
//
//        // stop the pet from moving off the left side
//        if ((imagebox.getTranslateX() + x) < bounds.getMinX()) {
//            x = -x;
//        }
//
//        // stop the pet from moving off the right side
//        else if ((imagebox.getTranslateX() + x) > bounds.getMaxX() - imagebox.getWidth() * 1.5) {
//            x = -x;
//        }
//
//        // set how long the movement takes
//        move.setDuration(Duration.seconds(2));
//
//        // move horizontally by the random amount
//        move.setByX(x);
//
//        // set walk direction animation
//        if (x > 0) {
//            Pet.setPet(desktopPet, animStates.PetState.WALKLEFT);
//        } else {
//            Pet.setPet(desktopPet, animStates.PetState.WALKRIGHT);
//        }
//
//        // play the movement
//        move.play();
//    }
