package production.bussines_contacts.models;

import production.bussines_contacts.enums.Role;

public class Viewer extends User {

    public Viewer(Long id, String name, String password) {
        super(id, name, password);
    }

    @Override
    public Role getRole() {
        // Implementation specific to Viewer
        return Role.VIEWER;
    }
}
