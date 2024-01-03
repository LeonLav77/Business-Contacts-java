package production.bussines_contacts.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import production.bussines_contacts.Application;
import production.bussines_contacts.models.ChangeDataModel;
import production.bussines_contacts.models.Company;
import production.bussines_contacts.database.DB;
import production.bussines_contacts.models.User;
import production.bussines_contacts.utils.ChangeLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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
        if (!confirmSaveOperation("Save Company")) {
            return;
        }

        Company tempCompany = createTempCompanyFromFields();
        Map<String, Map<String, String>> differences = company.getDifferencesMap(tempCompany);

        if (!differences.isEmpty()) {
            ChangeLog.persistChanges(differences, company);
            updateCompanyDetails();
        }

        MenuController.redirectToCompaniesScreen();
    }

    private Company createTempCompanyFromFields() {
        Company tempCompany = new Company();
        tempCompany.setName(nameField.getText());
        tempCompany.setIndustry(industryField.getText());
        tempCompany.setHeadquarters(headquartersField.getText());
        tempCompany.setWebsite(websiteField.getText());
        return tempCompany;
    }

    private void updateCompanyDetails() {
        company.setName(nameField.getText());
        company.setIndustry(industryField.getText());
        company.setHeadquarters(headquartersField.getText());
        company.setWebsite(websiteField.getText());
        company.update();
    }
}
