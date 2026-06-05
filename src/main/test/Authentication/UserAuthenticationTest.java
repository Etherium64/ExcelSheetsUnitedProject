package Authentication;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import project.authentication.AuthController;
import project.authentication.MockAuthController;
import project.model.User;
import project.model.UserDAO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Testing UserDAO and AuthController
 */

public class UserAuthenticationTest {
    private static MockAuthController mockAuthController;
    private static UserDAO mockUserDAO;

    @BeforeAll
    public static void setUp() throws Exception {
        mockAuthController = new MockAuthController();
        mockUserDAO = new UserDAO();
        mockUserDAO.createTable();
        User mockUser = new User(16, "", "", null, false);
        mockUserDAO.insert(mockUser);
    }

    @AfterAll
    public static void closeConnection() {
        mockUserDAO.close();
    }

    @Test
    public void testAuthentication() throws Exception {
        byte[] salt = mockAuthController.generateSalt();
        String username = "testUsername";
        String password = mockAuthController.hashPassword("testPassword", salt);
        User mockUser = mockUserDAO.getById(16);

        mockUser.setUsername(username);
        mockUser.setPassword(password);
        mockUser.setSalt(salt);
        mockUser.setRegistered(true);

        mockUserDAO.update(mockUser);

        assertEquals("testUsername", mockUser.getUsername());

        assertNotEquals("testpassword", mockUser.getPassword());
        assertEquals(password, mockUser.getPassword());

        assertNotEquals(AuthController.generateSalt(), mockUser.getSalt());
        assertEquals(salt, mockUser.getSalt());

        assertEquals(true, mockUser.getRegistered());
    }

}



