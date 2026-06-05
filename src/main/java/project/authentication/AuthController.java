package project.authentication;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import project.model.User;
import project.model.UserDAO;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class AuthController {
    //Data Access Object for User Data Model
    private static UserDAO userDAO;

    //UI elements for Authentication GUI
    @FXML
    private ListView<User> usersListView;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button registerBtn;

    /**
     * Public Constructor for Auth Controller
     * Starts with an instantiation of UserDAO
     * If there are no User rows, creates the first 15, including sample users AI and Human
     * new User(); are blank slots for you to register in
     */
    public AuthController() throws Exception {
        userDAO = new UserDAO();
        if (userDAO.getAll().isEmpty()) {
            byte[] AISalt = generateSalt();
            byte[] humanSalt = generateSalt();
            String AIPassword = hashPassword("AI", AISalt);
            String humanPassword = hashPassword("Human", humanSalt);
            userDAO.insert(new User("AI", AIPassword, AISalt, true)); //1 AI
            userDAO.insert(new User("Human", humanPassword, humanSalt, true)); //2 Human
            userDAO.insert(new User()); //3
            userDAO.insert(new User()); //4
            userDAO.insert(new User()); //5
            userDAO.insert(new User()); //6
            userDAO.insert(new User()); //7
            userDAO.insert(new User()); //8
            userDAO.insert(new User()); //9
            userDAO.insert(new User()); //10
            userDAO.insert(new User()); //11
            userDAO.insert(new User()); //12
            userDAO.insert(new User()); //13
            userDAO.insert(new User()); //14
            userDAO.insert(new User()); //15
        }
    }

    /**
     * Hashes Password using SHA-512.
     * After password is hashed, it is then stored in the database
     */
    public static String hashPassword(String password, byte[] salt) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.update(salt);
        byte[] hashedPassword = messageDigest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    /**
     * Generates Salt for Hashing the Password
     * A unique Salt is generated and assigned as a field to the User Data Model
     */
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Handles the selection of the User row in a ListView of Users
     * Password field is blank
     * First User row is an already registered User.
     */
    private void selectUser(User user) {
        usersListView.getSelectionModel().select(user);
        usernameTextField.setText(user.getUsername());
        passwordField.setText("");
        usernameTextField.setDisable(true);
        registerBtn.setText("Login");
    }

    /**
     * Renders each cell of the ListView
     * If row contains a registered User, show their Username, disable Username field, and switch to Login button
     * If row contains no user yet, allow Username field to be modified and switch text to Register
     */
    private ListCell<User> renderCell(ListView<User> usersListView) {
        return new ListCell<>() {
            private void onUserSelected(MouseEvent mouseEvent) {
                ListCell<User> clickedCell = (ListCell<User>) mouseEvent.getSource();
                User selectedUser = clickedCell.getItem();
                if (selectedUser != null) {
                    selectUser(selectedUser);
                    if (selectedUser.getRegistered()) {
                        usernameTextField.setDisable(true);
                        registerBtn.setText("Login");
                    } else {
                        usernameTextField.setDisable(false);
                        registerBtn.setText("Register");
                    }
                }
            }

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    super.setOnMouseClicked(this::onUserSelected);
                } else {
                    setText(user.getUsername());
                }
            }
        };

    }

    /**
     * Refresh the user ListView with the List of Users stored in the DAO
     * Select the first user row
     */
    private void refreshList() {
        usersListView.getItems().clear();
        List<User> users = userDAO.getAll();
        usersListView.getItems().addAll(users);
        usersListView.getSelectionModel().selectFirst();
        User firstUser = usersListView.getSelectionModel().getSelectedItem();
        if (firstUser != null) {
            selectUser(firstUser);
        }
    }

    /**
     * Initialises TableView GUI
     */
    @FXML
    public void initialize() {
        usersListView.setCellFactory(this::renderCell);
        refreshList();
    }

    /**
     * Handles what happens if you click the Register / Login Button
     * Eiher calls the Register function if it is a blank ListView row
     * Or calls the Login function if there is already q registered User
     */
    @FXML
    private void registerBtnClick() throws Exception {
        User selectedUser = usersListView.getSelectionModel().getSelectedItem();
        if (!selectedUser.getRegistered()) {
            register(selectedUser);
        } else {
            login(selectedUser);
        }
    }

    /**
     * To 'Register' insert a Username, Salt, Password, and set Registered as True to an empty User class
     */
    private void register(User selectedUser) throws Exception {
        selectedUser.setUsername(usernameTextField.getText());
        selectedUser.setSalt(generateSalt());
        selectedUser.setPassword(hashPassword(passwordField.getText(), selectedUser.getSalt()));
        selectedUser.setRegistered(true);
        userDAO.update(selectedUser);
        refreshList();
    }

    /**
     * Check if input password maches the User registration details after hashing
     * If it matches, allow entry into Desktop Pet application
     * If it doesn't match, do an Incorrect password alert
     */
    protected void login(User selectedUser) throws Exception {
        String inputPassword = hashPassword(passwordField.getText(), selectedUser.getSalt());
        if (Objects.equals(inputPassword, selectedUser.getPassword())) {
            //Stores the Username and User_ID for access across other controller in a UserSingleton Class
            //Simulates an Active User
            UserSingleton.getInstance().setup(selectedUser.getUser_id(), selectedUser.getUsername());

            Stage loginStage = (Stage) registerBtn.getScene().getWindow();
            Stage petStage = new Stage();
            new project.desktoppet302.DesktopPet().start(petStage);
            loginStage.close();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Incorrect Password");
            alert.setHeaderText("The Password you have entered does not match. Please try again.");
            alert.showAndWait();
            selectUser(selectedUser);
        }
    }
}
