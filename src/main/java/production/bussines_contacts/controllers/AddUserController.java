package production.bussines_contacts.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import production.bussines_contacts.controllers.MenuController;
import production.bussines_contacts.enums.Role;
import production.bussines_contacts.models.Admin;
import production.bussines_contacts.models.User;
import production.bussines_contacts.models.Viewer;
import production.bussines_contacts.utils.FileUtils;

public class AddUserController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<Role> roleComboBox;

    @FXML
    private void initialize() {
        roleComboBox.getItems().addAll(Role.values());
    }

    @FXML
    private void saveUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Role role = roleComboBox.getValue();
        User user = (role == Role.ADMIN) ? new Admin(FileUtils.getNextUserId(), username, password) : new Viewer(FileUtils.getNextUserId(), username, password);

        FileUtils.insertUser(user);
        MenuController.redirectToUsersScreen();
    }
}
