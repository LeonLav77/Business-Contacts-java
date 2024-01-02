package production.bussines_contacts.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import production.bussines_contacts.models.Company;
import production.bussines_contacts.models.User;
import production.bussines_contacts.partials.DeletableCell;
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
            User user = event.getRowValue();
            user.setName(event.getNewValue());
            FileUtils.updateUser(user);
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
