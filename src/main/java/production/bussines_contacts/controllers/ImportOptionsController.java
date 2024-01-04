package production.bussines_contacts.controllers;

import javafx.scene.control.Menu;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import production.bussines_contacts.Application;
import production.bussines_contacts.enums.Importance;
import production.bussines_contacts.enums.Role;
import production.bussines_contacts.interfaces.Importable;
import production.bussines_contacts.models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import production.bussines_contacts.database.DB;
import production.bussines_contacts.utils.FileUtils;

public class ImportOptionsController {
    private String importType;

    private File handleOpeningCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        return fileChooser.showOpenDialog(Application.getMainStage());
    }

    private <T> List<T> importCSV(File file, Importable<T> importable, Function<String, T> createItemFunction) {
        List<T> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                try{
                    T item = createItemFunction.apply(line);
                    items.add(item);
                }catch (Exception e) {
                    System.out.println("Company not found");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }


    private Company createCompanyFromCSVLine(String line) {
        String[] values = line.split(",");
        Company company = new Company();
        company.setName(values[0]);
        company.setIndustry(values[1]);
        company.setHeadquarters(values[2]);
        company.setCreated_at(new Date());
        company.setWebsite(values[3]);
        return company;
    }

    public void handleImportCompanies() {
        File file = handleOpeningCSV();
        if (file == null) {
            return;
        }

        List<Company> companies = importCSV(file, new Company(), this::createCompanyFromCSVLine);
        MenuController.showReviewCompaniesScreen(companies);
    }

    public void handleImportUsers() {
        File file = handleOpeningCSV();
        if (file == null) {
            return;
        }

        List<User> users = importCSV(file, new Viewer(), this::createUserFromCSVLine);
        MenuController.showReviewUsersScreen(users);
    }

    public void handleImportContacts() {
        File file = handleOpeningCSV();
        if (file == null) {
            return;
        }

        List<Contact> contacts = importCSV(file, new Contact(), this::createContactFromCSVLine);
        MenuController.showReviewContactsScreen(contacts);
    }

    private User createUserFromCSVLine(String line) {
        String[] values = line.split(",");

        if (values.length != 3) {
            // Handle invalid CSV line, for example, by logging an error or returning null.
            return null;
        }

        String roleStr = values[2].trim();
        Role role = Role.valueOf(roleStr.toUpperCase());

        User user = role == Role.ADMIN ? new Admin() : new Viewer();

        user.setName(values[0].trim());
        user.setPassword(values[1].trim());
        // FIXAT ILI SVI DOBIJU ISTI ID
        user.setId(FileUtils.getNextUserId());
        return user;
    }

    private Contact createContactFromCSVLine(String line) {
        String[] values = line.split(",");

        if (values.length != 6) {
            // Handle invalid CSV line, for example, by logging an error or returning null.
            return null;
        }

        Contact contact = new Contact();

        contact.setName(values[1].trim());
        contact.setDepartment(values[2].trim());

        // Set Importance based on the CSV value (assuming Importance is an enum)
        String importanceStr = values[3].trim();
        Importance importance = Importance.valueOf(importanceStr.toUpperCase());
        contact.setImportance(importance);
        contact.setPhone_number(values[4].trim());
        contact.setCustom_note(values[5].trim());

        // Assuming you want to set the current date as the created_at date
        contact.setCreated_at(new Date());

        String companyName = values[0].trim();
        ArrayList<Company> company = DB.fetchCompanies();
        for (Company c : company) {
            if (c.getName().equals(companyName)) {
                contact.setCompany(c);
                break;
            }
        }
        if(contact.getCompany() == null){
            throw new RuntimeException("Company not found");
        }

        return contact;
    }
}
