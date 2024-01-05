package production.bussines_contacts.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import production.bussines_contacts.Application;
import production.bussines_contacts.models.Company;
import production.bussines_contacts.models.Contact;
import production.bussines_contacts.models.User;

import java.io.IOException;
import java.util.List;

public class MenuController {
    @FXML
    private Menu usersMenu;
    @FXML
    private Menu importMenu;
    @FXML
    private MenuItem addCompanySubMenu;
    @FXML
    private MenuItem addContactSubMenu;
    @FXML
    private MenuItem addUserSubMenu;
    public static void editCompany(Company company) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/editCompany.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);

            EditCompanyController controller = fxmlLoader.getController();
            controller.setCompany(company);

            Application.getMainStage().setTitle("Edit User");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
}

    public void initialize() {
        updateMenuVisibility();
    }
    private void updateMenuVisibility() {
        User currentUser = Application.getLoggedInUser();
        usersMenu.setVisible(currentUser.isAdmin());
        importMenu.setVisible(currentUser.isAdmin());
        addCompanySubMenu.setVisible(currentUser.isAdmin());
        addContactSubMenu.setVisible(currentUser.isAdmin());
    }
    public static void showIndexScreen(){
        loadScreen("index", "Index");
    }
    public static void showContactsScreen(){
        loadScreen("contacts", "Contacts");
    }
    public void showUsersScreen(ActionEvent actionEvent) {
        loadScreen("users", "Users");
    }
    public void redirectToAddContactsScreen(ActionEvent actionEvent) {
        loadScreen("addContact", "Add contact");
    }
    public static void redirectToUsersScreen(){
        loadScreen("users", "Users");
    }

    public void showCompaniesScreen(ActionEvent actionEvent){
        loadScreen("companies", "Companies");
    }
    public void showContactsScreen(ActionEvent actionEvent){
        loadScreen("contacts", "Contacts");
    }

    public static void redirectToCompaniesScreen(){
        loadScreen("companies", "Companies");
    }
    public void logout(ActionEvent actionEvent) {
        Application.setLoggedInUser(null);
        loadScreen("login", "Login");
    }

    public static void editUser(User user) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/editUser.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);

            EditUserController controller = fxmlLoader.getController();
            controller.setUser(user);

            Application.getMainStage().setTitle("Edit User");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void editContact(Contact contact) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/editContact.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);

            EditContactController controller = fxmlLoader.getController();
            controller.setContact(contact);

            Application.getMainStage().setTitle("Edit contact");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void showChangeLogScreen(ActionEvent actionEvent) {
        loadScreen("changesLog", "Changes log");
    }

    public void showImportScreen(ActionEvent actionEvent) {
        loadScreen("import", "Import companies");
    }
    public static void showReviewCompaniesScreen(List<Company> companies){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/reviewCompanies.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);

            ReviewCompaniesController controller = fxmlLoader.getController();
            controller.setCompanies(companies);

            Application.getMainStage().setTitle("Review Companies");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showReviewContactsScreen(List<Contact> contacts){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/reviewContacts.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);

            ReviewContactsController controller = fxmlLoader.getController();
            controller.setContacts(contacts);

            Application.getMainStage().setTitle("Review Contacts");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showReviewUsersScreen(List<User> users){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/reviewUsers.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);

            ReviewUsersController controller = fxmlLoader.getController();
            controller.setUsers(users);

            Application.getMainStage().setTitle("Review Users");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showAddUserScreen(ActionEvent actionEvent) {
        loadScreen("addUser", "Add user");
    }

    private static void loadScreen(String fxmlName, String title){
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/" + fxmlName + ".fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Application.getMainStage().setTitle(title);
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showAddCompaniesScreen(ActionEvent actionEvent) {
        loadScreen("addCompany", "Add company");
    }
}


