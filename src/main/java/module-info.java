module production.bussines_contacts {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.logging;
    requires java.sql;
    requires ormlite.core;
    requires ormlite.jdbc;

    opens production.bussines_contacts to javafx.fxml;
    exports production.bussines_contacts;
    exports production.bussines_contacts.controllers;
    opens production.bussines_contacts.controllers to javafx.fxml;

    // Open the models package to ormlite.core for reflection
    opens production.bussines_contacts.models to ormlite.core;
}
