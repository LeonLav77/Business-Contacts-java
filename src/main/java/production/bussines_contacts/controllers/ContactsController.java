package production.bussines_contacts.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import production.bussines_contacts.database.DB;
import production.bussines_contacts.models.Contact;
import production.bussines_contacts.partials.DeletableCell;
import production.bussines_contacts.partials.EditableCell;
import production.bussines_contacts.utils.FunctionUtils;

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


        if (FunctionUtils.isUserAdmin()) {
            this.setupEditColumn();
            this.setupEditableColumns();
            this.setupDeleteColumn();
        } else {
            editColumn.setVisible(false);
            deleteColumn.setVisible(false);
        }

        this.showAndFilterContacts();
        groupByCompanyCheckBox.setOnAction(event -> sortContacts());
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> filterContacts(newValue));
    }

    private void setupDeleteColumn() {
        deleteColumn.setCellFactory(param -> new DeletableCell<>());
    }


    private void setupEditableColumns() {
        FunctionUtils.setupEditableColumn(contactNameColumn, Contact::setName);
        FunctionUtils.setupEditableColumn(departmentColumn, Contact::setDepartment);
        FunctionUtils.setupEditableColumn(phoneNumberColumn, Contact::setPhone_number);
        FunctionUtils.setupEditableColumn(customNoteColumn, Contact::setCustom_note);
    }

    private void sortContacts() {
        ObservableList<Contact> currentItems = contactsTableView.getItems();

        if (groupByCompanyCheckBox.isSelected()) {
            currentItems.sort(Comparator.comparing(contact -> contact.getCompany().getName()));
        } else {
            currentItems.sort(Comparator.comparing(Contact::getImportance));
        }
    }

    private void filterContacts(String searchText) {
        List<Contact> contactsList = DB.fetchContacts();

        if (searchText != null && !searchText.isEmpty()) {
            Predicate<Contact> matchesSearch = createSearchPredicate(searchText);
            contactsList.removeIf(matchesSearch.negate());
        }

        ObservableList<Contact> observableItemList = FXCollections.observableArrayList(contactsList);
        contactsTableView.setItems(observableItemList);
        sortContacts();
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

    private void showAndFilterContacts() {
        List<Contact> contactsList = DB.fetchContacts();

        contactsList.sort(Comparator.comparing(Contact::getImportance));

        ObservableList<Contact> observableItemList = FXCollections.observableArrayList(contactsList);
        contactsTableView.setItems(observableItemList);
    }
}
