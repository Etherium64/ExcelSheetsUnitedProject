package AnimationStates;

/**
 * A super lightweight animation state.
 * <p>
 * This basically just wraps an {@link AnimationPlayer} so the
 * state machine has something to switch between.
 */
public class SimpleAnimationState implements AnimationState {

    private final AnimationPlayer player;

    /**
     * Creates a new simple animation state.
     *
     * @param player The animation player that handles frame updates.
     */
    public SimpleAnimationState(AnimationPlayer player) {
        this.player = player;
    }

    /**
     * Returns the animation player for this state.
     *
     * @return the {@link AnimationPlayer} tied to this state.
     */
    @Override
    public AnimationPlayer getPlayer() {
        return player;
    }
}
