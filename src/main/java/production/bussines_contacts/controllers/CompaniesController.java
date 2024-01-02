package production.bussines_contacts.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import production.bussines_contacts.database.DB;
import production.bussines_contacts.models.Company;
import production.bussines_contacts.models.Contact;
import production.bussines_contacts.partials.DeletableCell;
import production.bussines_contacts.partials.EditableCell;

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

        this.setupEditColumn();
        this.setupEditableColumns();
        this.setupDeleteColumn();
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
        // Making the companyNameColumn editable
        companyNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        companyNameColumn.setOnEditCommit(event -> {
            Company company = event.getRowValue();
            company.setName(event.getNewValue());
            updateCompanyInDatabase(company);
        });

        industryColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        industryColumn.setOnEditCommit(event -> {
            Company company = event.getRowValue();
            company.setIndustry(event.getNewValue());
            updateCompanyInDatabase(company);
        });

        headquartersColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        headquartersColumn.setOnEditCommit(event -> {
            Company company = event.getRowValue();
            company.setHeadquarters(event.getNewValue());
            updateCompanyInDatabase(company);
        });

        websiteColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        websiteColumn.setOnEditCommit(event -> {
            Company company = event.getRowValue();
            company.setWebsite(event.getNewValue());
            updateCompanyInDatabase(company);
        });
    }

    private void updateCompanyInDatabase(Company company) {
        System.out.println("Updating company: " + company.getName());
        DB.updateCompany(company);
        showAndFilterCompanies();
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
            if (company.getWebsite().toLowerCase().contains(searchLower)) {
                return true;
            }
            return false;
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

    // Additional methods to handle events, load data, etc.
}
