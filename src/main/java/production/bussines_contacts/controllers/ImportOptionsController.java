package production.bussines_contacts.controllers;

import javafx.scene.control.Menu;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import production.bussines_contacts.Application;
import production.bussines_contacts.enums.Role;
import production.bussines_contacts.interfaces.Importable;
import production.bussines_contacts.models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import production.bussines_contacts.database.DB;
import production.bussines_contacts.utils.FileUtils;

public class ImportOptionsController {
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
                try {
                    T item = createItemFunction.apply(line);
                    if (item != null) {
                        items.add(item);
                    } else {
                        // Handle invalid CSV line, for example, by logging an error.
                        System.out.println("Invalid CSV line: " + line);
                    }
                } catch (Exception e) {
                    System.out.println("Error processing CSV line: " + line);
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    public <T> void handleImport(Importable<T> importable, Function<String, T> createItemFunction) {
        File file = handleOpeningCSV();
        if (file == null) {
            return;
        }

        List<T> items = importCSV(file, importable, createItemFunction);
        importable.redirectToConfirmScreen(items);
    }

    public void handleImportCompanies() {
        handleImport(new Company(), this::createCompanyFromCSVLine);
    }

    public void handleImportUsers() {
        handleImport(new Viewer(), this::createUserFromCSVLine);
    }

    public void handleImportContacts() {
        handleImport(new Contact(), this::createContactFromCSVLine);
    }

    private <T> T createObjectFromCSVLine(String line, Importable<T> importable) {
        String[] values = line.split(",");
        if (values.length != importable.getNumberOfColumns()) {
            // Handle invalid CSV line, for example, by logging an error or returning null.
            return null;
        }

        return importable.createItem(values);
    }

    private Company createCompanyFromCSVLine(String line) {
        return createObjectFromCSVLine(line, new Company());
    }

    private User createUserFromCSVLine(String line) {
        return createObjectFromCSVLine(line, new Viewer());
    }

    private Contact createContactFromCSVLine(String line) {
        return createObjectFromCSVLine(line, new Contact());
    }
}
