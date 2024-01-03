package production.bussines_contacts.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import production.bussines_contacts.Application;
import production.bussines_contacts.models.ChangeDataModel;
import production.bussines_contacts.models.Contact;
import production.bussines_contacts.models.Company;
import production.bussines_contacts.enums.Importance;
import production.bussines_contacts.database.DB;
import production.bussines_contacts.utils.ChangeLog;

import java.util.*;

import static production.bussines_contacts.utils.FunctionUtils.confirmSaveOperation;

public class EditContactController {

    @FXML
    private TextField nameField, departmentField, phoneNumberField;
    @FXML
    private ComboBox<Importance> importanceComboBox;
    @FXML
    private TextArea customNoteField;
    @FXML
    private ComboBox<Company> companyComboBox;

    private Contact contact;

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

    public void setContact(Contact contact) {
        this.contact = contact;
        loadContactData();
    }

    private void loadContactData() {
        if (contact != null) {
            nameField.setText(contact.getName());
            departmentField.setText(contact.getDepartment());
            phoneNumberField.setText(contact.getPhone_number());
            customNoteField.setText(contact.getCustom_note());
            importanceComboBox.setValue(contact.getImportance());
            companyComboBox.setValue(contact.getCompany());
        }
    }



    @FXML
    private void saveContact() {
        if(!confirmSaveOperation("Save Contact")) {
            return;
        }

        Contact tempContact = createTempContact();
        Map<String, Map<String, String>> differences = contact.getDifferencesMap(tempContact);


        if (!differences.isEmpty()) {
            ChangeLog.persistChanges(differences, contact);
            updateContactDetails();
        }

        DB.updateContact(contact);
        MenuController.showContactsScreen();
    }


    private void updateContactDetails() {
        contact.setCustom_note(customNoteField.getText());
        contact.setImportance(importanceComboBox.getValue());
        contact.setPhone_number(phoneNumberField.getText());
        contact.setDepartment(departmentField.getText());
        contact.setName(nameField.getText());

        Company selectedCompany = companyComboBox.getValue();
        if (selectedCompany != null) {
            contact.setCompany(selectedCompany);
        }
    }

    private Contact createTempContact() {
        Contact tempContact = new Contact();
        tempContact.setName(nameField.getText());
        tempContact.setDepartment(departmentField.getText());
        tempContact.setPhone_number(phoneNumberField.getText());
        tempContact.setCustom_note(customNoteField.getText());
        tempContact.setImportance(importanceComboBox.getValue());
        tempContact.setCompany(companyComboBox.getValue());
        return tempContact;
    }
}
