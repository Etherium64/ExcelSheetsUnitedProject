package AnimationStates;

import javafx.scene.image.Image;
import java.util.HashMap;


// Controls which animation is currently active and handles switching between them.
public class animController {

    // All possible animation states the controller can switch between.
    public enum State { IDLE, WALKLEFT, WALKRIGHT, JUMP, SADIDLE }

    // Stores animations mapped to their corresponding state.
    private HashMap<State, Animation> animations = new HashMap<>();
    private Animation currentAnimation;
    private State currentState;

    // Registers an animation for a specific state.
    // The first animation added becomes the default active animation.
    public void addAnimation(State state, Animation animation) {
        animations.put(state, animation);
        if (currentAnimation == null) {
            currentAnimation = animation;
            currentState = state;
        }
    }

    // Switches to a new animation state.
    // If the state changes, the new animation is reset and becomes active.
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

    // Returns the current frame of the active animation.
    // Used by rendering code to draw the animation.
    public Image getCurrentFrame() {
        return currentAnimation.getFrame();
    }
}
