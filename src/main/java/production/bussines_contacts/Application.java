package production.bussines_contacts;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import production.bussines_contacts.database.CustomMigrationTool;
import production.bussines_contacts.database.DB;
import production.bussines_contacts.generics.ImportanceComparator;
import production.bussines_contacts.interfaces.Importantable;
import production.bussines_contacts.models.Company;
import production.bussines_contacts.models.Contact;
import production.bussines_contacts.models.User;

import java.io.IOException;

public class Application extends javafx.application.Application {
    private static Stage mainStage;
    private static User loggedInUser;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        DB.fetchCompanies();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getMainStage(){
        return mainStage;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }


    public static void main(String[] args) {
//        testAdditionalFeature();
        boolean isMigrationNeeded = false;
        if (isMigrationNeeded) {
            CustomMigrationTool.runMigrations();
        }else {
            launch();
        }
    }

    public static void testAdditionalFeature(){
        Company company = new Company();
        Contact contact = new Contact();

        ImportanceComparator<Company, Contact> comparator = new ImportanceComparator<>(company, contact);
        Importantable moreImportant = comparator.compareImportance();

        if (moreImportant == company) {
            System.out.println("Company is more important.");
        } else if (moreImportant == contact) {
            System.out.println("Contact is more important.");
        } else {
            System.out.println("Both are equally important.");
        }
    }
}