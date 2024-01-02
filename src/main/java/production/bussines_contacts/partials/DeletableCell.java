package production.bussines_contacts.partials;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import production.bussines_contacts.interfaces.Deletable; // You need to create this interface

import static production.bussines_contacts.utils.FunctionUtils.confirmDeleteOperation;

public class DeletableCell<T extends Deletable> extends TableCell<T, Void> {
    private final Button deleteButton;

    public DeletableCell() {
        deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> handleDeleteAction());
    }

    private void handleDeleteAction() {
        int index = getIndex();
        T item = getTableView().getItems().get(index);
        if(!confirmDeleteOperation(item.deleteText())) {
            return;
        }
        if (item != null) {
            item.delete(); // Assuming T has a delete method defined in Deletable interface
            getTableView().getItems().remove(item); // Or refresh the table view as needed
        }
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(deleteButton);
        }
    }
}
