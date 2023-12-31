package production.bussines_contacts.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import production.bussines_contacts.database.DB;
import production.bussines_contacts.models.Contact;
import production.bussines_contacts.models.User;
import production.bussines_contacts.partials.EditableCell;

import java.util.List;

public class ContactsController {

    @FXML
    private TableView<Contact> contactsTableView;
    @FXML
    private TableColumn<Contact, Void> editColumn;
    @FXML
    private TableColumn<Contact, Long> contactIdColumn;
    @FXML
    private TableColumn<Contact, String> contactNameColumn;
    @FXML
    private TableColumn<Contact, String> departmentColumn;
    @FXML
    private TableColumn<Contact, String> companyColumn;
    @FXML
    private TableColumn<Contact, String> phoneNumberColumn;
    @FXML
    private TableColumn<Contact, String> importanceColumn;
    @FXML
    private TableColumn<Contact, String> createdColumn;
    @FXML
    private TableColumn<Contact, String> customNoteColumn;

    @FXML
    public void initialize() {
        contactIdColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getId()));
        contactNameColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getName()));
        departmentColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getDepartment()));
        companyColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getCompany().getName()));
        phoneNumberColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPhone_number()));
        importanceColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getImportance().toString()));
        createdColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getCreated_at().toString()));
        customNoteColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getCustom_note()));

        this.setupEditColumn();
        this.showAndFilterContacts();
    }

    private void setupEditColumn() {
        editColumn.setCellFactory(param -> new EditableCell<>());
    }

    private void showAndFilterContacts() {
        List<Contact> contactsList = DB.fetchContacts();

        ObservableList<Contact> observableItemList = FXCollections.observableArrayList(contactsList);
        contactsTableView.setItems(observableItemList);

    }

    // Additional methods to handle events, load data, etc.
}
