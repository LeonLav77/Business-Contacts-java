package production.bussines_contacts.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import production.bussines_contacts.Application;
import production.bussines_contacts.models.User;

import java.io.IOException;

public class MenuController {
    @FXML
    private Menu usersMenu;
    public void initialize() {
        updateMenuVisibility();
    }
    private void updateMenuVisibility() {
        User currentUser = Application.getLoggedInUser();
        usersMenu.setVisible(currentUser.isAdmin());
    }
    public static void showIndexScreen(){
        loadScreen("index", "Index");
    }
    public void showUsersScreen(ActionEvent actionEvent) {
        loadScreen("users", "Users");
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

    public void logout(ActionEvent actionEvent) {
        System.out.println("Logout");
        Application.setLoggedInUser(null);
        loadScreen("login", "Login");
    }

    public static void editUser(User user) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/editUser.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);

            // Set the user in the controller
            EditUserController controller = fxmlLoader.getController();
            controller.setUser(user);

            Application.getMainStage().setTitle("Edit User");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadScreen(String fxmlName, String title){
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
}


