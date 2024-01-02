package production.bussines_contacts.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import production.bussines_contacts.database.DB;
import production.bussines_contacts.enums.Importance;
import production.bussines_contacts.models.Contact;
import production.bussines_contacts.models.User;
import production.bussines_contacts.partials.DeletableCell;
import production.bussines_contacts.partials.EditableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

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
    private CheckBox groupByCompanyCheckBox;
    @FXML
    private TextField searchTextField;
    @FXML
    private TableColumn<Contact, Void> deleteColumn;
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
        //this.setupRowFactory();
        this.showAndFilterContacts();
        this.setupEditableColumns();
        this.setupDeleteColumn();
        groupByCompanyCheckBox.setOnAction(event -> sortContacts());
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> filterContacts(newValue));
    }

    private void setupDeleteColumn() {
        deleteColumn.setCellFactory(param -> new DeletableCell<>());
    }


    private void setupEditableColumns(){
        // Making the companyNameColumn editable
        contactNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        contactNameColumn.setOnEditCommit(event -> {
            Contact contact = event.getRowValue();
            contact.setName(event.getNewValue());
            updateContactInDatabase(contact);
        });
        departmentColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        departmentColumn.setOnEditCommit(event -> {
            Contact contact = event.getRowValue();
            contact.setDepartment(event.getNewValue());
            updateContactInDatabase(contact);
        });
        phoneNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        phoneNumberColumn.setOnEditCommit(event -> {
            Contact contact = event.getRowValue();
            contact.setPhone_number(event.getNewValue());
            updateContactInDatabase(contact);
        });
        customNoteColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        customNoteColumn.setOnEditCommit(event -> {
            Contact contact = event.getRowValue();
            contact.setCustom_note (event.getNewValue());
            updateContactInDatabase(contact);
        });
    }

    private void updateContactInDatabase(Contact contact) {
        DB.updateContact(contact);
    }

    private void sortContacts() {
        // Retrieve the current items in the TableView
        ObservableList<Contact> currentItems = contactsTableView.getItems();

        if (groupByCompanyCheckBox.isSelected()) {
            // Group by company when the checkbox is selected
            currentItems.sort(Comparator.comparing(contact -> contact.getCompany().getName()));
        } else {
            // Default sorting (by importance or other criteria)
            currentItems.sort(Comparator.comparing(Contact::getImportance));
        }

        // No need to set the items again since we are sorting the existing ObservableList
    }

    private void filterContacts(String searchText) {
        List<Contact> contactsList = DB.fetchContacts();

        if (searchText != null && !searchText.isEmpty()) {
            Predicate<Contact> matchesSearch = createSearchPredicate(searchText);
            contactsList.removeIf(matchesSearch.negate());
        }

        ObservableList<Contact> observableItemList = FXCollections.observableArrayList(contactsList);
        contactsTableView.setItems(observableItemList);
        sortContacts(); // Sort after filtering
    }

    private void setupEditColumn() {
        editColumn.setCellFactory(param -> new EditableCell<>());
    }

    private Predicate<Contact> createSearchPredicate(String searchText) {
        return contact -> {
            String searchLower = searchText.toLowerCase();
            if (contact.getName().toLowerCase().contains(searchLower)) {
                return true;
            }
            if (contact.getCompany() != null && contact.getCompany().getName().toLowerCase().contains(searchLower)) {
                return true;
            }
            if (contact.getDepartment().toLowerCase().contains(searchLower)) {
                return true;
            }
            if (contact.getPhone_number().toLowerCase().contains(searchLower)) {
                return true;
            }
            return contact.getCustom_note().toLowerCase().contains(searchLower);
        };
    }

    private void setupRowFactory() {
        contactsTableView.setRowFactory(tableView -> new TableRow<Contact>() {
            @Override
            protected void updateItem(Contact contact, boolean empty) {
                super.updateItem(contact, empty);
                if (contact == null || empty) {
                    setStyle("");
                } else {
                    // Check the importance of the contact and set the row color
                    if (contact.getImportance() == Importance.HIGH) {
                        setStyle("-fx-background-color: #ff99a4;");
                    } else {
                        setStyle(""); // Default style
                    }
                }
            }
        });
    }

    private void showAndFilterContacts() {
        List<Contact> contactsList = DB.fetchContacts();

        // Sort contacts by importance
        contactsList.sort(Comparator.comparing(Contact::getImportance));

        ObservableList<Contact> observableItemList = FXCollections.observableArrayList(contactsList);
        contactsTableView.setItems(observableItemList);
    }

    // Additional methods to handle events, load data, etc.
}
