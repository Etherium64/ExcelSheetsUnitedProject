package AnimationStates;

import javafx.scene.image.Image;
import java.util.HashMap;

public class animController {

    public enum State { IDLE, WALKlEFT, WALKrIGHT, JUMP, SADiDLE }

    private HashMap<State, Animation> animations = new HashMap<>();
    private Animation currentAnimation;
    private State currentState;

    public void addAnimation(State state, Animation animation) {
        animations.put(state, animation);
        if (currentAnimation == null) {
            currentAnimation = animation;
            currentState = state;
        }
    }

    public void setState(State newState) {
        if (newState != currentState) {
            currentState = newState;
            currentAnimation = animations.get(newState);
            currentAnimation.reset();
        }
    }

    public void update() {
        currentAnimation.update();
    }

    public Image getCurrentFrame() {
        return currentAnimation.getFrame();
    }
}
