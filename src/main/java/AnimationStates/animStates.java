package AnimationStates;

import javafx.scene.image.Image;

public class animStates {
    private animController controller;

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
            Animation idle = new Animation(loadImages.loadSequence("/ImageSequences/idle"), 24);
            System.out.println("Idle frames loaded: " + idle.getFrameCount());

            System.out.println("TEST 1 = " + loadImages.class.getResource("/ImageSequences/idle/"));
            System.out.println("TEST 2 = " + loadImages.class.getResource("/ImageSequences/idle/idle0001.png"));
            System.out.println("TEST 3 = " + loadImages.class.getResource("/ImageSequences/idle/idle0002.png"));
            System.out.println("TEST 4 = " + loadImages.class.getResource("/ImageSequences/idle/idle0003.png"));

            Animation walkleft = new Animation(loadImages.loadSequence("/ImageSequences/walkLeft"), 100);
            Animation walkright = new Animation(loadImages.loadSequence("/ImageSequences/walkRight"), 100);
            Animation jump = new Animation(loadImages.loadSequence("/ImageSequences/jump"), 200);
            Animation sadidle = new Animation(loadImages.loadSequence("/ImageSequences/sadIdle"), 200);

            controller.addAnimation(animController.State.IDLE, idle);
            controller.addAnimation(animController.State.WALKlEFT, walkleft);
            controller.addAnimation(animController.State.WALKrIGHT, walkright);
            controller.addAnimation(animController.State.JUMP, jump);
            controller.addAnimation(animController.State.SADiDLE, sadidle);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

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

    public void update() {
        controller.update();
    }

    public Image getCurrentFrame() {
        return controller.getCurrentFrame();
    }


}
