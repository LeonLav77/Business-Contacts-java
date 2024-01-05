package production.bussines_contacts.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.ObservableList;
import production.bussines_contacts.models.Contact;

import java.util.List;

public class ReviewContactsController {

    @FXML
    private TableView<Contact> contactsTable;
    @FXML
    private TableColumn<Contact, String> nameColumn;
    @FXML
    private TableColumn<Contact, String> departmentColumn;
    @FXML
    private TableColumn<Contact, String> companyColumn;
    @FXML
    private TableColumn<Contact, String> importanceColumn;
    @FXML
    private TableColumn<Contact, String> phoneNumberColumn;
    @FXML
    private TableColumn<Contact, String> customNoteColumn;

    private List<Contact> contacts;

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;

        nameColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getName()));
        departmentColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getDepartment()));
        companyColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getCompany().getName()));
        importanceColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getImportance().toString()));
        phoneNumberColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getPhone_number()));
        customNoteColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getCustom_note()));

        ObservableList<Contact> observableContactsList = FXCollections.observableArrayList(contacts);
        contactsTable.setItems(observableContactsList);
    }

    @FXML
    private void handleConfirmImport() {
        for (Contact contact : contacts) {
            contact.save();
        }
        MenuController.showContactsScreen();
    }

    @FXML
    private void handleCancel() {
        MenuController.showIndexScreen();
    }
}
