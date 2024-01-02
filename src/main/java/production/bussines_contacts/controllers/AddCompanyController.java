package production.bussines_contacts.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import production.bussines_contacts.models.Company;
import production.bussines_contacts.database.DB;

import java.util.Date;

public class AddCompanyController {

    @FXML
    private TextField nameField, industryField, headquartersField, websiteField;

    @FXML
    private void createCompany() {
        Company company = new Company();
        company.setName(nameField.getText());
        company.setIndustry(industryField.getText());
        company.setHeadquarters(headquartersField.getText());
        company.setWebsite(websiteField.getText());
        company.setCreated_at(new Date()); // Set the current date as the creation date

        DB.createCompany(company);
        MenuController.redirectToCompaniesScreen();
        // Handle post-creation logic (like closing the dialog or refreshing a list)
    }
}