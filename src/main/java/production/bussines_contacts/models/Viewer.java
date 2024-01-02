package production.bussines_contacts.models;

import production.bussines_contacts.controllers.MenuController;
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

    @Override
    public boolean isAdmin() {
        // Implementation specific to Admin
        return false;
    }

    @Override
    public void edit() {
        MenuController.editUser(this);
    }

    @Override
    public String deleteText() {
        return "Delete User";
    }
}
