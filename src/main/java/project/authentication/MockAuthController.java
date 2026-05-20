package project.authentication;

import project.model.User;
import project.model.UserDAO;

import java.util.Objects;

public class MockAuthController extends AuthController{
    private UserDAO userDAO;

    public MockAuthController() throws Exception {
        userDAO = new UserDAO();
        userDAO.dropTable();
        userDAO.createTable();
        userDAO.insert(new User());
    }

    @Override
    public void initialize() {
    }

    private User mockRegister(String username, String password, byte[] salt, Boolean registered) throws Exception {
        User mockUser = userDAO.getByUsername(username);
        mockUser.setSalt(salt);
        mockUser.setPassword(hashPassword(password, salt));
        mockUser.setRegistered(registered);
        userDAO.update(mockUser);
        return mockUser;
    }

    private Boolean mockLogin(User user, String password) throws Exception {
        if (Objects.equals( hashPassword(password, user.getSalt()) , user.getPassword()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void logout()
    {
        userDAO.close();
    }

}
