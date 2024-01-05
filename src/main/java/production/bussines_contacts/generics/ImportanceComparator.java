package production.bussines_contacts.generics;

import production.bussines_contacts.interfaces.Importantable;

public class ImportanceComparator<T extends Importantable, U extends Importantable> {
    private final T item1;
    private final U item2;

    public ImportanceComparator(T item1, U item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    public Importantable compareImportance() {
        int importance1 = item1.getImportanceValue();
        int importance2 = item2.getImportanceValue();

        if (importance1 > importance2) {
            return item1;
        } else if (importance1 < importance2) {
            return item2;
        } else {
            // If importance values are equal, you can return either item
            return item1;
        }
    }
}