package production.bussines_contacts.partials;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import production.bussines_contacts.interfaces.Editable;

public class EditableCell<T extends Editable<T>> extends TableCell<T, Void> {
    private final Button editButton;

    public EditableCell() {
        editButton = new Button("Edit");
        editButton.setOnAction(event -> handleEditAction());
    }

    private void handleEditAction() {
        T item = getTableView().getItems().get(getIndex());
        item.edit();
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(editButton);
        }
    }
}
