package Pet;

import AnimationStates.animStates;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.desktoppet302.Pet;
import static org.junit.jupiter.api.Assertions.*;

public class PetTest {

    private Pet pet;
    private final animStates aState1 = new animStates();
    private final ImageView pImage1 = new ImageView();
    private final animStates aState2 = new animStates();
    private final ImageView pImage2 = new ImageView();

    @BeforeEach
    void setUp() {
        pet = new Pet(aState1, pImage1);
    }

    @Test
    public void testPet() {
        assertEquals(aState1, pet.getAnimStates());
        assertEquals(pImage1, pet.getPetImage());
    }

    @Test
    public void testSetIdle() {
        pet.setIdle();
        assertTrue(pet.getAnimStates().isIdle());
    }

    @Test
    public void testSetSadIdle() {
        pet.setSadIdle();
        assertTrue(pet.getAnimStates().isSadIdle());
    }

    @Test
    public void testSetShock() {
        pet.setShock();
        assertTrue(pet.getAnimStates().isShock());
    }

    @Test
    public void testSetWalkLeft() {
        pet.setWalkLeft();
        assertTrue(pet.isMovingLeft);
    }

    @Test
    public void testSetWalkRight() {
        pet.setWalkRight();
        assertTrue(pet.isMovingRight);
    }

    @Test
    public void testGetAnimStates() {
        assertEquals(aState1, pet.getAnimStates());
    }

    @Test
    public void testGetPetImage() {
        assertEquals(pImage1, pet.getPetImage());
    }

    @Test
    public void testSetAnimStates() {
        pet.setAnimStates(aState2);
        assertEquals(aState2, pet.getAnimStates());
    }

    @Test
    public void testSetPetImage() {
        pet.setPetImage(pImage2);
        assertEquals(pImage2, pet.getPetImage());
    }
}
