package AnimationStates;

import javafx.scene.image.Image;

/**
 * Represents a looping frame‑based animation. This class stores an array of frames
 * and advances through them based on a fixed time interval. It does not handle
 * rendering — only timing and frame progression.
 *
 * <p>The animation system:
 * <ul>
 *     <li>Cycles through frames in order</li>
 *     <li>Advances frames based on a fixed duration</li>
 *     <li>Loops back to the first frame when the end is reached</li>
 *     <li>Allows resetting to restart the animation</li>
 * </ul>
 *
 * <p>This class is used by {@link animController} to manage animation playback.</p>
 */

public class Animation {

    // returns how many frames the current animation has
    public int getFrameCount() {
        return frames.length;
    }

    // All frames that make up the animation.
    private Image[] frames;
    private int currentFrame = 0;
    // How long each frame should be displayed (in milliseconds).
    private long frameDuration;
    private long lastUpdate = 0;

    /**
     * Creates a new animation with the given frames and frame duration.
     *
     * @param frames        the array of images that make up the animation
     * @param frameDuration how long each frame should be displayed (in milliseconds)
     */
    public Animation(Image[] frames, long frameDuration) {
        this.frames = frames;
        this.frameDuration = frameDuration;
    }

    /**
     * Returns the total number of frames in this animation.
     *
     * @return the number of frames
     */

    public void update() {
        long now = System.currentTimeMillis();

        if (now - lastUpdate >= frameDuration) {

            currentFrame = (currentFrame + 1) % frames.length;
            lastUpdate = now;

            System.out.println("Frame advanced to: " + currentFrame);
        }
    }

    /**
     * Returns the current frame image.
     *
     * @return the current {@link Image} frame
     */

    public Image getFrame() {
        return frames[currentFrame];
    }


    // Resets the animation to the first frame and clears timing information.
    public void reset() {
        currentFrame = 0;
        lastUpdate = 0;
    }
}
