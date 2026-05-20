package AnimationStates;

import javafx.scene.image.Image;

/**
 * Handles playnig an animatin - framing timing, looping and keeps track of which frame we are on
 * <p>
 *     Simply cycles through he frames at a speed you tell it to.
 * </p>
 */

public class AnimationPlayer {

    /** The frames this animation will cycle through. */
    private final AnimationFrames frames;
    /** How long each frame should stay on screen (in milliseconds). */
    private final long frameDuration;

    /** Index of the frame we're currently showing. */
    private int currentFrame = 0;

    /**
     * Timestamp (in nanoseconds) of the last time we advanced the frame.
     * Used to figure out when it's time to move to the next one.
     */
    private long lastUpdate = 0;

    /**
     * Creates a new animation player.
     *
     * @param frames        The frames that make up this animation.
     * @param frameDuration How long each frame should last (ms).
     */
    public AnimationPlayer(AnimationFrames frames, long frameDuration) {
        this.frames = frames;
        this.frameDuration = frameDuration;
    }

    /**
     * Updates the animation based on the current timestamp.
     * <p>
     * This gets called every frame by the AnimationTimer.
     * When enough time has passed, we move to the next animation frame.
     *
     * @param now The current time in nanoseconds (from AnimationTimer).
     */
    public void update(long now) {
        long elapsedMs = (now - lastUpdate) / 1_000_000;

        if (elapsedMs >= frameDuration) {
            currentFrame = (currentFrame + 1) % frames.size();
            lastUpdate = now;
        }
    }

    /**
     * Returns the image for the current frame.
     *
     * @return The frame image we're currently on.
     */
    public Image getCurrentFrame() {
        return frames.get(currentFrame);
    }

    /**
     * Resets the animation back to the first frame.
     * <p>
     * This is called whenever the pet switches to a new animation state,
     * so the animation always starts from the beginning.
     */
    public void reset() {
        currentFrame = 0;
        lastUpdate = 0;
    }
}
