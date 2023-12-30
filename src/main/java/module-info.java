module production.bussines_contacts {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.logging;

    opens production.bussines_contacts to javafx.fxml;
    exports production.bussines_contacts;
    exports production.bussines_contacts.controllers;
    opens production.bussines_contacts.controllers to javafx.fxml;
}