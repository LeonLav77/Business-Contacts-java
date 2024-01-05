package production.bussines_contacts.partials;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import production.bussines_contacts.interfaces.Deletable;

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
        item.delete();
        getTableView().getItems().remove(item);
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
