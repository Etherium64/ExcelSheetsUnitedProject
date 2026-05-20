package AnimationStates;

import javafx.scene.image.Image;

/**
 * Handles which animation state the pet is currently in.
 * <p>
 * decided which state is active
 * </p>
 */

public class AnimationStateMachine {

    /** The animation state we're currently using. */
    public AnimationState getCurrentState() {
        return currentState;
    }

    /**
     * Creates a new state machine starting in the given state.
     *
     * @param initialState The state the pet should begin in.
     */
    private AnimationState currentState;

    /**
     * @return the animation state that's currently active.
     */
    public AnimationStateMachine(AnimationState initialState) {
        this.currentState = initialState;
    }

    /**
     * Switches to a new animation state.
     * <p>
     * If the state actually changes, we reset the animation so it
     * starts from frame 0. If it's the same state, we don't touch it.
     *
     * @param newState The state to switch to.
     */
    public void changeState(AnimationState newState) {
        if (newState != currentState) {
            currentState = newState;
            currentState.getPlayer().reset();
        }
    }

    /**
     * Updates the currently active animation.
     * <p>
     * This gets called every frame by the pet, and we just pass the
     * timestamp along to the animation player.
     *
     * @param now The current time in nanoseconds (from AnimationTimer).
     */
    public void update(long now) {
        currentState.getPlayer().update(now);
    }

    /**
     * @return the current frame image from the active animation.
     */
    public Image getCurrentFrame() {
        return currentState.getPlayer().getCurrentFrame();
    }
}
