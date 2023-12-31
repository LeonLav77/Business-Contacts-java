package production.bussines_contacts.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import production.bussines_contacts.models.User;
import production.bussines_contacts.partials.EditableCell;
import production.bussines_contacts.utils.FileUtils;

import java.io.File;
import java.util.List;

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
    private TableColumn<User, Void> editColumn;

    public void initialize() {
        userIDTableColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getId()));
        userNameTableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getName()));
        userRoleTableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getRole().getRoleName()));
        this.setupEditColumn();
        this.filterUsers();
    }

    private void setupEditColumn() {
        editColumn.setCellFactory(param -> new EditableCell<>());
    }

    private void editUser(User user) {
        // Implement user editing logic
        System.out.println("Editing user: " + user.getName());
    }

    public void filterUsers(){
        List<User> usersList = FileUtils.readUsersFromFile();

        ObservableList<User> observableItemList = FXCollections.observableArrayList(usersList);
        usersTableView.setItems(observableItemList);
    }
}
