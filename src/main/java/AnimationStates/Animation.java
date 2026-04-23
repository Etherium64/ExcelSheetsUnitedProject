package AnimationStates;

import javafx.scene.image.Image;

public class Animation {

    private Image[] frames;
    private int currentFrame = 0;
    private long frameDuration;
    private long lastUpdate = 0;

    public Animation(Image[] frames, long frameDuration) {
        this.frames = frames;
        this.frameDuration = frameDuration;
    }

    public int getFrameCount() {
        return frames.length;
    }

    public void update() {
        if (frames.length == 0) {
            return;
        }

        long now = System.currentTimeMillis();

        if (now - lastUpdate >= frameDuration) {
            currentFrame = (currentFrame + 1) % frames.length;
            lastUpdate = now;
        }
    }

    public Image getFrame() {
        if (frames.length == 0) {
            return null;
        }

        return frames[currentFrame];
    }

    public void reset() {
        currentFrame = 0;
        lastUpdate = 0;
    }
}