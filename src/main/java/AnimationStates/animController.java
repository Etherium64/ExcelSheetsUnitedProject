package AnimationStates;

import javafx.scene.image.Image;
import java.util.HashMap;

/**
 * Controls all animation state transitions for the pet. This class manages a collection
 * of animations mapped to specific {@link State} values and provides methods for switching,
 * updating, and retrieving the currently active animation frame.
 *
 * <p>The controller is responsible for:
 * <ul>
 *     <li>Registering animations for each state</li>
 *     <li>Tracking the current active state</li>
 *     <li>Resetting animations when switching states</li>
 *     <li>Updating the active animation each frame</li>
 *     <li>Providing the current frame for rendering</li>
 * </ul>
 *
 * <p>This class is used internally by {@link animStates} to coordinate animation behavior.</p>
 */

public class animController {

    // All possible animation states the controller can switch between.
    public enum State { IDLE, WALKLEFT, WALKRIGHT, SHOCK, SADIDLE }

    // Stores animations mapped to their corresponding state.
    private HashMap<State, Animation> animations = new HashMap<>();
    private Animation currentAnimation;
    private State currentState;

    /**
     * Registers an animation for a specific state. The first animation added becomes
     * the default active animation.
     *
     * @param state the animation state to associate with the animation
     * @param animation the animation object to register
     */
    public void addAnimation(State state, Animation animation) {
        animations.put(state, animation);
        if (currentAnimation == null) {
            currentAnimation = animation;
            currentState = state;
        }
    }

    /**
     * Switches the controller to a new animation state. If the state changes, the
     * corresponding animation is reset and becomes the active animation.
     *
     * @param newState the new animation state to activate
     */
    public void setState(State newState) {
        if (newState != currentState) {
            currentState = newState;
            currentAnimation = animations.get(newState);
            currentAnimation.reset();
        }
    }

    // Updates the currently active animation.
    // Should be called once per frame.
    public void update() {
        currentAnimation.update();
    }

    /**
     * Returns the current frame of the active animation. This is used by rendering
     * code to draw the animation on screen.
     *
     * @return the current animation frame as an {@link Image}
     */
    public Image getCurrentFrame() {
        return currentAnimation.getFrame();
    }
}
