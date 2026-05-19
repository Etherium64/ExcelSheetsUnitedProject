package project.authentication;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import project.desktoppet302.DesktopPet;
import project.model.User;
import project.model.UserDAO;
import javafx.scene.control.Alert.AlertType;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class AuthController {
    private static UserDAO userDAO;

    @FXML
    private ListView<User> usersListView;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerBtn;


    public AuthController() throws Exception {
        userDAO = new UserDAO();
        if (userDAO.getAll().isEmpty())
        {
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

    private void selectUser(User user) {
        usersListView.getSelectionModel().select(user);
        usernameTextField.setText(user.getUsername());
        passwordField.setText("");
        usernameTextField.setDisable(true);
        registerBtn.setText("Login");
    }

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

    @FXML
    public void initialize() {
        usersListView.setCellFactory(this::renderCell);
        refreshList();
    }

    public byte[] generateSalt()
    {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String password, byte[] salt) throws Exception
    {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.update(salt);
        byte[] hashedPassword = messageDigest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    public void registerBtnClick() throws Exception {
        User selectedUser = usersListView.getSelectionModel().getSelectedItem();
        if (!selectedUser.getRegistered()) {
            register(selectedUser);
        } else {
            login(selectedUser);
        }
    }

    public void register(User selectedUser) throws Exception {
        selectedUser.setUsername(usernameTextField.getText());
        selectedUser.setSalt(generateSalt());
        selectedUser.setPassword(hashPassword(passwordField.getText(),selectedUser.getSalt()));
        selectedUser.setRegistered(true);
        userDAO.update(selectedUser);
        refreshList();
    }

    public void login(User selectedUser) throws Exception {
        String inputPassword = hashPassword(passwordField.getText(),selectedUser.getSalt());
        if (Objects.equals(inputPassword, selectedUser.getPassword()) )
        {
            UserSingleton.getInstance().setup(selectedUser.getUser_id(), selectedUser.getUsername());
            Stage petStage = (Stage) registerBtn.getScene().getWindow();
            DesktopPet newDesktopPet = new DesktopPet();
            newDesktopPet.start(petStage);
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Incorrect Password");
            alert.setHeaderText("The Password you have entered does not match. Please try again.");
            alert.showAndWait();
            selectUser(selectedUser);
        }
    }

}
