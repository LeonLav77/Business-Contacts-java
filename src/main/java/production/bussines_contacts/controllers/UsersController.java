package production.bussines_contacts.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import production.bussines_contacts.enums.Role;
import production.bussines_contacts.models.Admin;
import production.bussines_contacts.models.User;
import production.bussines_contacts.models.Viewer;
import production.bussines_contacts.partials.DeletableCell;
import production.bussines_contacts.partials.EditableCell;
import production.bussines_contacts.utils.ChangeLog;
import production.bussines_contacts.utils.FileUtils;

import java.util.List;
import java.util.Map;

import static production.bussines_contacts.utils.FunctionUtils.confirmSaveOperation;

public class UsersController {
    @FXML
    private TableView<User> usersTableView;
    @FXML
    private TableColumn<User, Long> userIDTableColumn;
    @FXML
    private TableColumn<User, String> userNameTableColumn;
    @FXML
    private TableColumn<User, String> userRoleTableColumn;
    @FXML
    private TextField searchTextField;
    @FXML
    private TableColumn<User, Void> editColumn;
    @FXML
    private TableColumn<User, Void> deleteColumn;

    public void initialize() {
        userIDTableColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getId()));
        userNameTableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getName()));
        userRoleTableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getRole().getRoleName()));
        this.setupEditColumn();
        this.setupEditableColumns();
        this.setupDeleteColumn();
        this.filterUsers();
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> filterUsers());
    }

    private void setupEditableColumns(){
        userNameTableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        userNameTableColumn.setOnEditCommit(event -> {
            if(confirmSaveOperation("Save User")) {
                return;
            }
            User user = event.getRowValue();
            User tempUser = (user.getRole() == Role.ADMIN) ? new Admin(null, event.getNewValue(), null) : new Viewer(null, event.getNewValue(), null);
            Map<String, Map<String, String>> differences = user.getDifferencesMap(tempUser);

            if (!differences.isEmpty()) {
                ChangeLog.persistChanges(differences, user);
                user.setName(event.getNewValue());
                FileUtils.updateUser(user);
            }
        });
    }

    private void setupDeleteColumn(){
        deleteColumn.setCellFactory(param -> new DeletableCell<>());
    }

    private void setupEditColumn() {
        editColumn.setCellFactory(param -> new EditableCell<>());
    }

    public void filterUsers(){
        List<User> usersList = FileUtils.readUsersFromFile();

        String searchQuery = searchTextField.getText();
        if (!searchQuery.isEmpty()) {
            usersList.removeIf(user -> !user.getName().toLowerCase().contains(searchQuery.toLowerCase()));
        }

        ObservableList<User> observableItemList = FXCollections.observableArrayList(usersList);
        usersTableView.setItems(observableItemList);
    }
}
