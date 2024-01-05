package production.bussines_contacts.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import production.bussines_contacts.models.Contact;
import production.bussines_contacts.models.Company;
import production.bussines_contacts.enums.Importance;
import production.bussines_contacts.database.DB;

import java.util.Date;
import java.util.List;

public class AddContactController {

    @FXML
    private TextField nameField, departmentField, phoneNumberField;
    @FXML
    private TextArea customNoteField;
    @FXML
    private ComboBox<Importance> importanceComboBox;
    @FXML
    private ComboBox<Company> companyComboBox;

    public void initialize() {
        initializeImportanceComboBox();
        initializeCompanyComboBox();
    }

    private void initializeImportanceComboBox() {
        importanceComboBox.getItems().setAll(Importance.values());
    }

    private void initializeCompanyComboBox() {
        List<Company> companies = DB.fetchCompanies();
        companyComboBox.getItems().setAll(companies);
    }

    @FXML
    private void createContact() {
        Contact newContact = new Contact();
        newContact.setName(nameField.getText());
        newContact.setDepartment(departmentField.getText());
        newContact.setPhone_number(phoneNumberField.getText());
        newContact.setCustom_note(customNoteField.getText());
        newContact.setImportance(importanceComboBox.getValue());
        newContact.setCompany(companyComboBox.getValue());
        newContact.setCreated_at(new Date());

        DB.createContact(newContact);
        MenuController.showContactsScreen();
    }
}
