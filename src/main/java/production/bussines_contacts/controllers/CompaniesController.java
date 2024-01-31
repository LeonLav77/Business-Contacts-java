package production.bussines_contacts.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import production.bussines_contacts.database.DB;
import production.bussines_contacts.models.Company;
import production.bussines_contacts.models.Contact;
import production.bussines_contacts.partials.DeletableCell;
import production.bussines_contacts.partials.EditableCell;
import production.bussines_contacts.utils.FunctionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CompaniesController {

    @FXML
    private TableView<Company> companiesTableView;
    @FXML
    private TableColumn<Company, Void> editColumn;
    @FXML
    private TableColumn<Company, Long> companyIdColumn;
    @FXML
    private TableColumn<Company, String> companyNameColumn;
    @FXML
    private TableColumn<Company, String> industryColumn;
    @FXML
    private TableColumn<Company, String> headquartersColumn;
    @FXML
    private TableColumn<Company, String> websiteColumn;
    @FXML
    private TableColumn<Company, String> createdColumn;
    @FXML
    private TextField searchTextField;
    @FXML
    private TableColumn<Company, Void> deleteColumn;

    @FXML
    public void initialize() {
        companyIdColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getId()));
        companyNameColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getName()));
        industryColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getIndustry()));
        headquartersColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getHeadquarters()));
        websiteColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getWebsite()));
        createdColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getCreated_at().toString()));

        if (FunctionUtils.isUserAdmin()) {
            this.setupEditColumn();
            this.setupEditableColumns();
            this.setupDeleteColumn();
        } else {
            editColumn.setVisible(false);
            deleteColumn.setVisible(false);
        }

        this.showAndFilterCompanies();
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> showAndFilterCompanies());
    }

    private void setupDeleteColumn(){
        deleteColumn.setCellFactory(param -> new DeletableCell<>());
    }

    private void setupEditColumn() {
        editColumn.setCellFactory(param -> new EditableCell<>());
    }

    private void setupEditableColumns() {
        FunctionUtils.setupEditableColumn(companyNameColumn, Company::setName);
        FunctionUtils.setupEditableColumn(industryColumn, Company::setIndustry);
        FunctionUtils.setupEditableColumn(headquartersColumn, Company::setHeadquarters);
        FunctionUtils.setupEditableColumn(websiteColumn, Company::setWebsite);
    }

    public static Predicate<Company> matches(String searchText) {
        return company -> {
            String searchLower = searchText.toLowerCase();
            if (company.getName().toLowerCase().contains(searchLower)) {
                return true;
            }
            if (company.getIndustry().toLowerCase().contains(searchLower)) {
                return true;
            }
            if (company.getHeadquarters().toLowerCase().contains(searchLower)) {
                return true;
            }
            return company.getWebsite().toLowerCase().contains(searchLower);
        };
    }

    public void showAndFilterCompanies() {
        ArrayList<Company> companies = DB.fetchCompanies();

        String searchText = searchTextField.getText();
        if (searchText != null && !searchText.isEmpty()) {
            Predicate<Company> matchesSearch = matches(searchText);
            companies.removeIf(matchesSearch.negate());
        }

        ObservableList<Company> observableItemList = FXCollections.observableArrayList(companies);
        companiesTableView.setItems(observableItemList);
    }

    private static List<Contact> fetchContactsByCompanyId(Long companyId) {
        // Implement fetching logic in DB class
        return new ArrayList<>();
    }
}
