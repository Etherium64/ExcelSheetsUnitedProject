package AnimationStates;

/**
 * Represents a single animation "state" the pet can be in.
 * <p>
 * Each state basically just wraps an {@link AnimationPlayer},
 * which handles the actual frame updates. The state machine
 * swaps these in and out depending on what the pet is doing.
 */

public interface AnimationState {

    /**
     * @return the animation player that runs this state's frames.
     */
    AnimationPlayer getPlayer();
}
