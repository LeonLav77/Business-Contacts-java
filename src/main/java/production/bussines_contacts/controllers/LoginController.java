package production.bussines_contacts.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import production.bussines_contacts.Application;
import production.bussines_contacts.models.User;
import production.bussines_contacts.utils.FileUtils;

import java.util.List;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onLoginButtonClick() {
        List<User> users = FileUtils.readUsersFromFile();
        for (User user : users) {
            if (user.getName().equals(usernameField.getText()) && users.getFirst().getPassword().trim().equals(passwordField.getText().trim())) {
                Application.setLoggedInUser(user); // Set the logged-in user
                MenuController.showIndexScreen();
                return;
            }
        }
    }
}
