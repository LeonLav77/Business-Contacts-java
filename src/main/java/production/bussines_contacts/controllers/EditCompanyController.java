package production.bussines_contacts.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import production.bussines_contacts.models.Company;
import production.bussines_contacts.database.DB;

import static production.bussines_contacts.utils.FunctionUtils.confirmSaveOperation;

public class EditCompanyController {

    @FXML
    private TextField nameField, industryField, headquartersField, websiteField;

    private Company company;

    public void setCompany(Company company) {
        this.company = company;
        loadCompanyData();
    }

    private void loadCompanyData() {
        if (company != null) {
            nameField.setText(company.getName());
            industryField.setText(company.getIndustry());
            headquartersField.setText(company.getHeadquarters());
            websiteField.setText(company.getWebsite());
        }
    }

    @FXML
    private void saveCompany() {
        if(!confirmSaveOperation("Save Company")) {
            return;
        }

        if (company != null) {
            company.setName(nameField.getText());
            company.setIndustry(industryField.getText());
            company.setHeadquarters(headquartersField.getText());
            company.setWebsite(websiteField.getText());

            DB.updateCompany(company);
            MenuController.redirectToCompaniesScreen();

            // Handle post-update logic (such as navigating back to a list of companies)
        }
    }
}
