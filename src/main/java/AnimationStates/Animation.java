package AnimationStates;

import javafx.scene.image.Image;

// Represents a looping frame‑based animation.

// This class stores an array of frames and advances through them
// based on a fixed time interval (frameDuration). It does not handle
// rendering — only timing and frame progression.

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

    // Creates a new animation.

    //  Array of images that make up the animation.
    //  frameDuration Time each frame stays on screen (ms).
    public Animation(Image[] frames, long frameDuration) {
        this.frames = frames;
        this.frameDuration = frameDuration;
    }

    public void update() {
        long now = System.currentTimeMillis();

        if (now - lastUpdate >= frameDuration) {

            currentFrame = (currentFrame + 1) % frames.length;
            lastUpdate = now;

            //System.out.println("Frame advanced to: " + currentFrame);
        }
    }

    public Image getFrame() {
        return frames[currentFrame];
    }

    public void reset() {
        currentFrame = 0;
        lastUpdate = 0;
    }
}
