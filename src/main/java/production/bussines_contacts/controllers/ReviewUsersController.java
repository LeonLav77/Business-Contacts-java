package production.bussines_contacts.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import production.bussines_contacts.models.Company;
import production.bussines_contacts.models.User;

import java.util.Date;
import java.util.List;

public class ReviewUsersController {
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private TableColumn<User, String> roleColumn;

    private List<User> users;

    public void setUsers(List<User> users) {
        this.users = users;

        nameColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getName()));
        passwordColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getPassword()));
        roleColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getRole().toString()));

        System.out.println(users.isEmpty());
        ObservableList<User> observableItemList = FXCollections.observableArrayList(users);
        usersTable.setItems(observableItemList);
        // If necessary, refresh the table view or perform additional setup
    }
    @FXML
    private void handleConfirmImport() {
        for (User users : users) {
            users.save();
        }

        MenuController.redirectToUsersScreen();
    }

    @FXML
    private void handleCancel() {
        MenuController.showIndexScreen();
    }
}
