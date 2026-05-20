package AnimationStates;

import javafx.scene.image.Image;

/**
 * Represents a sequence of animation frames used by an {@link AnimationPlayer}.
 * <p>
 *     This class is a lightweight container around an array of objects.
 *     It provides indexed access to frames and exposes the total frame count.
 * </p>
 * the {@code AnimationPlayer} uses thes class to cycle thought frames when
 * playing an animation.
 */
public class AnimationFrames {

    /** the ordered list of frames */
    private final Image[] frames;

    /**
     * create a new {@code AnimationFrames} container.
     * @param frames an array of objects for the animation frames.
     *               must contain at least one frame
     * @throws IllegalArgumentException if null or empty
     */

    public AnimationFrames(Image[] frames) {
        if (frames == null || frames.length == 0)
            throw new IllegalArgumentException("Animation must contain at least one frame.");

        this.frames = frames;
    }

    /**
     * Returns the number of frames in this animation.
     *
     * @return the total number of frames
     */

    public int size() {
        return frames.length;
    }

    /**
     * Retrieves the frame at the specified index.
     *
     * @param index the index of the frame to retrieve (0-based)
     * @return the {@link Image} at the given index
     *
     * @throws ArrayIndexOutOfBoundsException if {@code index} is outside the valid range
     */

    public Image get(int index) {
        return frames[index];
    }
}
