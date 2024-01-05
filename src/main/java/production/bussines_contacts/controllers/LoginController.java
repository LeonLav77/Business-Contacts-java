package production.bussines_contacts.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import production.bussines_contacts.Application;
import production.bussines_contacts.models.User;
import production.bussines_contacts.utils.FileUtils;
import production.bussines_contacts.utils.FunctionUtils;

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
                Application.setLoggedInUser(user);
                MenuController.showIndexScreen();
                return;
            }
        }
        FunctionUtils.showAlert(Alert.AlertType.WARNING, "Not logged in", "Username or password is incorrect.");

    }
}
