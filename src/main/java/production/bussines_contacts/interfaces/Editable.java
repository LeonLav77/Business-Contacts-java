package production.bussines_contacts.interfaces;

import java.util.Map;

public interface Editable<T extends Editable<T>> {
    void edit();
    Long getId();
    void update();
    T clone();
    Map<String, Map<String, String>> getDifferencesMap(T item);
}
