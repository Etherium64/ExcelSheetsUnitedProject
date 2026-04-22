package AnimationStates;

import javafx.scene.image.Image;

public class Animation {

    public int getFrameCount() {
        return frames.length;
    }


    private Image[] frames;
    private int currentFrame = 0;
    private long frameDuration;
    private long lastUpdate = 0;

    public Animation(Image[] frames, long frameDuration) {
        this.frames = frames;
        this.frameDuration = frameDuration;
    }

    public void update() {
        long now = System.currentTimeMillis();

        if (now - lastUpdate >= frameDuration) {
            currentFrame = (currentFrame + 1) % frames.length;
            lastUpdate = now;

            System.out.println("Frame advanced to: " + currentFrame);
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
