package production.bussines_contacts.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import production.bussines_contacts.Application;

import java.io.IOException;

public class MenuController {
    public static void showIndexScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/index.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Application.getMainStage().setTitle("Index");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void showUsersScreen(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/index.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Application.getMainStage().setTitle("Index");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


