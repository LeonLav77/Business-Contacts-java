package production.bussines_contacts.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import production.bussines_contacts.models.ChangeDataModel;
import production.bussines_contacts.utils.ChangeLog;

import java.util.List;

public class ChangesLogController {

    @FXML
    private ListView<ChangeDataModel<?>> changesListView;

    public void initialize() {
        loadChangeLog();
    }

    private void loadChangeLog() {
        List<ChangeDataModel<?>> changeDataModels = ChangeLog.readFromChangeLog();
        changesListView.getItems().addAll(changeDataModels);
        changesListView.setCellFactory(param -> new ListCell<ChangeDataModel<?>>() {
            @Override
            protected void updateItem(ChangeDataModel<?> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    Label changeLabel = new Label(item.toString()); // Customize this based on how you want to display changes
                    Button undoButton = new Button("Undo");
                    undoButton.setOnAction(event -> undoChange(item));
                    hbox.getChildren().addAll(changeLabel, undoButton);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void undoChange(ChangeDataModel<?> changeDataModel) {
        // Implement the logic to undo changes.
        // This will depend on how you apply changes to your objects and might involve reverting changes in your database or application model.
    }
}
