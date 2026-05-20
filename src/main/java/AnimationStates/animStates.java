package AnimationStates;

import javafx.scene.image.Image;

/**
 * Holds all the different animation states the pet can switch between.
 * <p>
 * This class basically acts as the "animation library" for the pet.
 * It loads every animation (idle, walk, shock, sad idle, etc.) and
 * gives simple methods for switching between them.
 * <p>
 * It also handles special rules like the shock animation temporarily
 * overriding everything else.
 */

public class animStates {

    /** The state machine that actually manages which animation is active. */
    private final AnimationStateMachine stateMachine;

    /** All the different animation states the pet can use. */
    private final AnimationState idle;
    private final AnimationState walkLeft;
    private final AnimationState walkRight;
    private final AnimationState shock;
    private final AnimationState sadIdle;

    /**
     * @return true if the pet is currently in the idle animation.
     */
    public boolean isIdle() {
        return stateMachine.getCurrentState() == idle;
    }

    /**
     * @return true if the pet is currently in the sad idle animation.
     */
    public boolean isSadIdle() {
        return stateMachine.getCurrentState() == sadIdle;
    }

    /** Whether the pet is currently in the shock animation. */
    private boolean shocking = false;

    /** When the shock animation started (ms). */
    private long shockStartTime;

    public boolean isShock() {
        return shocking;
    }
    private static final long SHOCK_DURATION = 1000;

    /**
     * Loads all animation sequences and sets up the state machine.
     * <p>
     * Each animation gets its own AnimationPlayer with its own speed.
     */
    public animStates() {

        AnimationFrames idleFrames = new AnimationFrames(loadImages.loadSequence("/ImageSequences/idle"));
        AnimationFrames walkLeftFrames = new AnimationFrames(loadImages.loadSequence("/ImageSequences/walkLeft"));
        AnimationFrames walkRightFrames = new AnimationFrames(loadImages.loadSequence("/ImageSequences/walkRight"));
        AnimationFrames shockFrames = new AnimationFrames(loadImages.loadSequence("/ImageSequences/shocked"));
        AnimationFrames sadIdleFrames = new AnimationFrames(loadImages.loadSequence("/ImageSequences/sadIdle"));

        idle = new SimpleAnimationState(new AnimationPlayer(idleFrames, 200));
        walkLeft = new SimpleAnimationState(new AnimationPlayer(walkLeftFrames, 150));
        walkRight = new SimpleAnimationState(new AnimationPlayer(walkRightFrames, 150));
        shock = new SimpleAnimationState(new AnimationPlayer(shockFrames, 200));
        sadIdle = new SimpleAnimationState(new AnimationPlayer(sadIdleFrames, 150));

        stateMachine = new AnimationStateMachine(idle);
    }

    public void setIdle() {

        if (!shocking) {
            stateMachine.changeState(idle);
        }
    }

    public void setWalkLeft() {

        if (!shocking) {
            stateMachine.changeState(walkLeft);
        }
    }

    public void setWalkRight() {

        if (!shocking) {
            stateMachine.changeState(walkRight);
        }
    }

    public void setSadIdle() {

        if (!shocking) {
            stateMachine.changeState(sadIdle);
        }
    }

    /**
     * Activates the shock animation.
     * <p>
     * Shock overrides everything else for a short time,
     * so we mark it as "shocking" and let update() handle
     * when it should end.
     */
    public void setShock() {

        shocking = true;
        shockStartTime = System.currentTimeMillis();

        stateMachine.changeState(shock);
    }

    /**
     * Updates the current animation and handles shock timing.
     * <p>
     * If the shock animation has been running long enough,
     * we automatically switch back to idle.
     *
     * @param now The current time in nanoseconds (from AnimationTimer).
     */
    public void update(long now) {

        if (shocking) {

            long elapsed = System.currentTimeMillis() - shockStartTime;

            if (elapsed >= SHOCK_DURATION) {

                shocking = false;
                stateMachine.changeState(idle);
            }
        }

        stateMachine.update(now);
    }

    /**
     * @return the current animation frame to display.
     */
    public Image getCurrentFrame() {
        return stateMachine.getCurrentFrame();
    }
}
