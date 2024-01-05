package production.bussines_contacts.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import production.bussines_contacts.interfaces.Editable;
import production.bussines_contacts.interfaces.Loggable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.logging.Level;

public class FunctionUtils {
    public static boolean confirmSaveOperation(String header) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save these changes?", ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Save changes");
        confirmDialog.setHeaderText(header);


        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            return true;
        } else {
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
            return false;
        }
    }

    public static <T extends Editable<T>> void setupEditableColumn(TableColumn<T, String> column,
                                                                   BiConsumer<T, String> setter) {
        column.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        column.setOnEditCommit(event -> {
            if (!confirmSaveOperation("Save")) {
                return;
            }

            T item = event.getRowValue();
            T tempItem = item.clone();

            setter.accept(tempItem, event.getNewValue());
            Map<String, Map<String, String>> differences = item.getDifferencesMap(tempItem);

            if (!differences.isEmpty()) {
                ChangeLog.persistChanges(differences, item);
                setter.accept(item, event.getNewValue());
                item.update();
            }
        });
    }

    public static void addChange(Map<String, Map<String, String>> changes,String fieldName, String oldValue, String newValue) {
        if (oldValue != null && newValue != null && !oldValue.equals(newValue)) {
            Map<String, String> change = new HashMap<>();
            change.put("old", oldValue);
            change.put("new", newValue);
            changes.put(fieldName, change);
        }
    }

    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void handleException(String title, Exception e, Alert.AlertType alertType, Loggable loggable) {
        loggable.log(Level.SEVERE, title + ": " + e.getMessage(), e); // Using the default method
        showAlert(alertType, title, e.getMessage());
        if (e instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
    }
}
