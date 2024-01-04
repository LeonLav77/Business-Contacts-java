package production.bussines_contacts.interfaces;

import java.util.List;

public interface Importable<T> {
    void redirectToConfirmScreen(List<T> items);
    int getNumberOfColumns();
    T createItem(String[] data);
}