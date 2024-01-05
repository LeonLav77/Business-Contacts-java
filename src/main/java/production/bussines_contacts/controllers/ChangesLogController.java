package production.bussines_contacts.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
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
    }
}
