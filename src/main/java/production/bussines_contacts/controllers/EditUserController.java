package production.bussines_contacts.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import production.bussines_contacts.enums.Role;
import production.bussines_contacts.models.Admin;
import production.bussines_contacts.models.User;
import production.bussines_contacts.models.Viewer;
import production.bussines_contacts.utils.FileUtils;

import java.io.File;

public class EditUserController {
    @FXML
    private TextField usernameField;
    @FXML
    private ComboBox<String> roleComboBox; // Updated line
    private User user;

    public void initialize() {
        initializeRoleComboBox();
    }

    private void initializeRoleComboBox() {
        for (Role role : Role.values()) {
            roleComboBox.getItems().add(role.getRoleName());
        }
    }

    public void setUser(User user) {
        this.user = user;
        updateFields();
    }

    private void updateFields() {
        if (user != null) {
            usernameField.setText(user.getName());
            roleComboBox.setValue(user.getRole().getRoleName()); // Assuming User has getRole method
        }
    }

    @FXML
    private void saveUser() {
        // display a dialog to confirm the save
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save these changes?", ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Confirm Save");
        confirmDialog.setHeaderText("Save User");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                saveUserChanges();
            }else{
                System.out.println("User changes not saved");
            }
        });
    }

    private void saveUserChanges() {
        // Save logic here
        System.out.println("Saving user: " + user.getName());
        Long id = user.getId();
        String userPassword = user.getPassword();
        String username = usernameField.getText();
        String roleName = roleComboBox.getValue();
        Role role = Role.fromRoleName(roleName);

        User user = (role == Role.ADMIN) ? new Admin(id, username, userPassword) : new Viewer(id, username, userPassword);
        FileUtils.updateUser(user);
        MenuController.redirectToUsersScreen();
    }
}
