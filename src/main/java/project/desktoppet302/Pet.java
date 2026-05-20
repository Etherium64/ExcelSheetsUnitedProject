package project.desktoppet302;

import AnimationStates.animStates;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Random;

/**
 * JavaFX  class responsible for managing the different animation states for the pet
 * when idling or after an action is taken.
 *
 * @author Jacob Farrell
 */
public class Pet {

    /**
     * Animation state of the pet.
     */
    private final animStates petStates;

    /**
     * Displayed image of the pet.
     */
    private final ImageView petImage;

    // check if moving
    private boolean isMoving = false;
    private long lastInteractionTime = System.currentTimeMillis();
    private static final long SAD_IDLE_DELAY = 10_000; // 10 seconds

    /**
     * Constructs a new Pet with the specified animation state and image.
     * @param petStates The animation state of the pet.
     * @param petImage The displayed image of the pet.
     */
    public Pet(animStates petStates, ImageView petImage) {
        this.petStates = petStates;
        this.petImage = petImage;

        // new start for single animation loop
        new AnimationTimer(){
            @Override
            public void handle(long now){
                checkIdleState(now);
                petStates.update(now);
                petImage.setImage(petStates.getCurrentFrame());
            }
        }.start();
    }

    /**
     * Marks the current moment as the last time the user interacted with the pet.
     * <p>
     * This is used to decide when the pet should switch between normal idle
     * and sad idle animations after being ignored for a while.
     */

    private void markInteraction() {
        lastInteractionTime = System.currentTimeMillis();
    }

    /**
     * Checks how long it's been since the last interaction and switches
     * between idle and sad idle animations accordingly.
     * <p>
     * If the pet hasn't been touched for a while, it goes into sad idle.
     * If the user interacts again or enough time hasn't passed, it stays in normal idle.
     *
     * @param now The current timestamp from the animation timer (ns).
     */

    private void checkIdleState(long now) {

        long elapsed = System.currentTimeMillis() - lastInteractionTime;

        // If the pet is moving, don't change idle states.
        if (isMoving) return;

        if (elapsed > SAD_IDLE_DELAY) {

            // Switch to sad idle only if we're not already in it.
            if (!petStates.isSadIdle()) {
                petStates.setSadIdle();
            }

        } else {

            // Switch back to normal idle if needed.
            if (!petStates.isIdle()) {
                petStates.setIdle();
            }
        }
    }


    /**
     * Switches the pet to the normal idle animation.
     */
    public void setIdle()     { petStates.setIdle(); }

    /**
     * Switches the pet to the walking-left animation.
     */
    public void setWalkLeft() { petStates.setWalkLeft(); }

    /**
     * Switches the pet to the walking-right animation.
     */
    public void setWalkRight(){ petStates.setWalkRight(); }

    /**
     * Triggers the shock animation and marks it as an interaction,
     * so the pet doesn't immediately switch into sad idle afterward.
     */
    public void setShock()    { petStates.setShock(); markInteraction(); }

    /**
     * Switches the pet to the sad idle animation.
     */
    public void setSadIdle()  { petStates.setSadIdle(); }

    /**
     * Move the pet left or right from its current position and adjusting its state while it does so.
     * @param desktopPet The pet that is moving.
     * @param movePet The animation transition that moves the pet.
     * @param bounds The bounds of the screen.
     * @param imageBox The vertical box containing the pet and relevant text boxes and buttons.
     */
    public static void movePet(Pet desktopPet,
                               TranslateTransition movePet,
                               Rectangle2D bounds,
                               StackPane imageBox) {

        desktopPet.isMoving = true;

        Random random = new Random();
        double x = random.nextInt(600) - 300;

        if ((imageBox.getTranslateX() + x) < bounds.getMinX()) {
            x = -x;
        } else if ((imageBox.getTranslateX() + x) > bounds.getMaxX() - imageBox.getWidth()) {
            x = -x;
        }

        movePet.setDuration(Duration.seconds(2));
        movePet.setByX(x);

        if (x > 0) {
            desktopPet.setWalkLeft();
        } else {
            desktopPet.setWalkRight();
        }

        movePet.setOnFinished(e -> {
            desktopPet.isMoving = false;
        });

        movePet.play();
    }
}