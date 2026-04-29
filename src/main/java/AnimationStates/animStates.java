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
        WALKrIGHT,
        WALKlEFT,
        JUMP,
        SADiDLE
    }

    private PetState currentState = PetState.IDLE;

    public animStates() {
        controller = new animController();

        try {
            // Load animation sequences
            Animation idle = new Animation(loadImages.loadSequence("/ImageSequences/idle"), 24);
            System.out.println("Idle frames loaded: " + idle.getFrameCount());

//            System.out.println("TEST 1 = " + loadImages.class.getResource("/ImageSequences/idle/"));
//            System.out.println("TEST 2 = " + loadImages.class.getResource("/ImageSequences/idle/idle0001.png"));
//            System.out.println("TEST 3 = " + loadImages.class.getResource("/ImageSequences/idle/idle0002.png"));
//            System.out.println("TEST 4 = " + loadImages.class.getResource("/ImageSequences/idle/idle0003.png"));

            // change the frame duration to influence how long each frame is held
            Animation walkleft = new Animation(loadImages.loadSequence("/ImageSequences/walkLeft"), 100);
            Animation walkright = new Animation(loadImages.loadSequence("/ImageSequences/walkRight"), 100);
            Animation jump = new Animation(loadImages.loadSequence("/ImageSequences/jump"), 100);
            Animation sadidle = new Animation(loadImages.loadSequence("/ImageSequences/sadIdle"), 200);

            // Register animations with the controller
            controller.addAnimation(animController.State.IDLE, idle);
            controller.addAnimation(animController.State.WALKlEFT, walkleft);
            controller.addAnimation(animController.State.WALKrIGHT, walkright);
            controller.addAnimation(animController.State.JUMP, jump);
            controller.addAnimation(animController.State.SADiDLE, sadidle);

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
            case WALKlEFT:
                controller.setState(animController.State.WALKlEFT);
                break;
            case WALKrIGHT:
                controller.setState(animController.State.WALKrIGHT);
                break;
            case JUMP:
                controller.setState(animController.State.JUMP);
                break;
            case SADiDLE:
                controller.setState(animController.State.SADiDLE);
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
