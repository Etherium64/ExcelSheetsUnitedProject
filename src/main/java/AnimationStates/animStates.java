package AnimationStates;

import javafx.scene.image.Image;

// Handles switching between animations and updating the current one.

// Team Usage:
// addAnimation(State, Animation) to register animations
// setState(State) to switch animations
// update() once per frame
// getCurrentFrame() to draw


public class animStates {
    private animController controller;

    // Public enum of all available animation states.
    public enum PetState {
        IDLE,
        WALKRIGHT,
        WALKLEFT,
        JUMP,
        SADIDLE
    }

    private PetState currentState = PetState.IDLE;

    public animStates() {
        controller = new animController();

        try {
            // Load animation sequences
            Animation idle = new Animation(loadImages.loadSequence("/ImageSequences/idle"), 24);
            System.out.println("Idle frames loaded: " + idle.getFrameCount());

            /*
            System.out.println("TEST 1 = " + loadImages.class.getResource("/ImageSequences/idle/"));
            System.out.println("TEST 2 = " + loadImages.class.getResource("/ImageSequences/idle/idle0001.png"));
            System.out.println("TEST 3 = " + loadImages.class.getResource("/ImageSequences/idle/idle0002.png"));
            System.out.println("TEST 4 = " + loadImages.class.getResource("/ImageSequences/idle/idle0003.png"));
            */

            // change the frame duration to influence how long each frame is held
            Animation walkLeft = new Animation(loadImages.loadSequence("/ImageSequences/walkLeft"), 100);
            Animation walkRight = new Animation(loadImages.loadSequence("/ImageSequences/walkRight"), 100);
            Animation jump = new Animation(loadImages.loadSequence("/ImageSequences/jump"), 200);
            Animation sadIdle = new Animation(loadImages.loadSequence("/ImageSequences/sadIdle"), 200);

            // Register animations with the controller
            controller.addAnimation(animController.State.IDLE, idle);
            controller.addAnimation(animController.State.WALKLEFT, walkLeft);
            controller.addAnimation(animController.State.WALKRIGHT, walkRight);
            controller.addAnimation(animController.State.JUMP, jump);
            controller.addAnimation(animController.State.SADIDLE, sadIdle);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    //Switches the pet to a new animation state.
    // The animation will loop automatically until another state is set.
    public void setState(PetState newState) {
        currentState = newState;

        switch (newState) {
            case IDLE:
                controller.setState(animController.State.IDLE);
                break;
            case WALKLEFT:
                controller.setState(animController.State.WALKLEFT);
                break;
            case WALKRIGHT:
                controller.setState(animController.State.WALKRIGHT);
                break;
            case JUMP:
                controller.setState(animController.State.JUMP);
                break;
            case SADIDLE:
                controller.setState(animController.State.SADIDLE);
                break;
        }
    }

    //Updates the current animation frame. Call once per frame.
    public void update() {
        controller.update();
    }

    //Returns the current frame image for rendering.
    public Image getCurrentFrame() {
        return controller.getCurrentFrame();
    }


}
