import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.model.User;
import project.model.UserDAO;
import project.authentication.MockAuthController;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing UserDAO and AuthController
 */

public class UserAuthenticationTest {
    private MockAuthController mockAuthController;
    private User mockUser;

    @BeforeAll
    public void setUp() {
        mockAuthController = new MockAuthController();
    }

    @AfterAll
    public void closeConnection() {
        mockAuthController.logout();
    }

    @Test
    public void testRegister() {
        salt = mockAuthController.generateSalt();
        password = mockAuthController.hashPassword("test", salt);
        mockUser = mockAuthController.mockRegister("test", password, salt, true);

        assetEquals("test", mockUser.getUsername());

        assertNotEquals("test", mockUser.getPassword());
        assertEquals(password, mockUser.getPassword());

        assertNotEquals(mockAuthController.generateSalt(), mockUser.getSalt);
        assertEquals(salt, mockUser.getSalt());

        assertEquals(true, mockUser.getRegistered());
    }

    @Test
    public void testLogin() {
        assertEquals(false, mockAuthController.mockLogin(mockUser, "test"));
        String mockPassword = mockAuthController.hashPassword("test", mockUser.getSalt());
        asserEquals(true, mockAuthController.mockLogin(mockUser, mockPassword));
    }
}



