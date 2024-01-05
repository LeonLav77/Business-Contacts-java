package production.bussines_contacts.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import production.bussines_contacts.models.Company;

import java.util.Date;
import java.util.List;

public class ReviewCompaniesController {

    @FXML
    private TableView<Company> companiesTable;
    @FXML
    private TableColumn<Company, String> nameColumn;
    @FXML
    private TableColumn<Company, String> industryColumn;
    @FXML
    private TableColumn<Company, String> headquartersColumn;
    @FXML
    private TableColumn<Company, String> websiteColumn;
    private List<Company> companies;

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;

        nameColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getName()));
        industryColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getIndustry()));
        headquartersColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getHeadquarters()));
        websiteColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getWebsite()));

        ObservableList<Company> observableItemList = FXCollections.observableArrayList(companies);
        companiesTable.setItems(observableItemList);
    }

    @FXML
    private void handleConfirmImport() {
        for (Company company : companies) {
            company.setCreated_at(new Date());
            company.save();
        }

        MenuController.redirectToCompaniesScreen();
    }

    @FXML
    private void handleCancel() {
        MenuController.showIndexScreen();
    }
}
