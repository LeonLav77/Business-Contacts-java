package production.bussines_contacts.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class FunctionUtils {
    public static boolean confirmSaveOperation(String header) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save these changes?", ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Save changes");
        confirmDialog.setHeaderText(header);


        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            return true;
        } else {
            System.out.println("Contact changes not saved");
            return false;
        }
    }

    public static boolean confirmDeleteOperation(String header) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save these changes?", ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Delete item");
        confirmDialog.setHeaderText(header);


        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            return true;
        } else {
            System.out.println("Contact changes not saved");
            return false;
        }
    }
}
