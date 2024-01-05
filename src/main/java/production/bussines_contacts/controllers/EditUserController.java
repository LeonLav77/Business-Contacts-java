package production.bussines_contacts.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import production.bussines_contacts.enums.Role;
import production.bussines_contacts.models.Admin;
import production.bussines_contacts.models.User;
import production.bussines_contacts.models.Viewer;
import production.bussines_contacts.utils.ChangeLog;
import production.bussines_contacts.utils.FileUtils;

import java.io.File;
import java.util.Map;

import static production.bussines_contacts.utils.FunctionUtils.confirmSaveOperation;

public class EditUserController {
    @FXML
    private TextField usernameField;
    @FXML
    private ComboBox<String> roleComboBox;
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
            roleComboBox.setValue(user.getRole().getRoleName());
        }
    }

    @FXML
    private void saveUser() {
        if(!confirmSaveOperation("Save Contact")) {
            return;
        }

        User tempUser = createTempUser();
        Map<String, Map<String, String>> differences = user.getDifferencesMap(tempUser);

        if (!differences.isEmpty()) {
            ChangeLog.persistChanges(differences, user);
            saveUserChanges();
        }

        MenuController.redirectToUsersScreen();
    }

    private void saveUserChanges() {
        Long id = user.getId();
        String userPassword = user.getPassword();
        String username = usernameField.getText();
        String roleName = roleComboBox.getValue();
        Role role = Role.fromRoleName(roleName);

        User user = (role == Role.ADMIN) ? new Admin(id, username, userPassword) : new Viewer(id, username, userPassword);
        FileUtils.updateUser(user);
    }

    private User createTempUser() {
        String username = usernameField.getText();
        String roleName = roleComboBox.getValue();
        Role role = Role.fromRoleName(roleName);
        return (role == Role.ADMIN) ? new Admin(null, username, null) : new Viewer(null, username, null);
    }
}
