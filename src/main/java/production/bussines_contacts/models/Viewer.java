package production.bussines_contacts.models;

import production.bussines_contacts.controllers.MenuController;
import production.bussines_contacts.enums.Role;
import production.bussines_contacts.utils.FileUtils;
import production.bussines_contacts.utils.FunctionUtils;

import java.util.HashMap;
import java.util.Map;

public class Viewer extends User {
    private static final long serialVersionUID = 1L; // Unique version identifier

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

    @Override
    public Viewer clone() {
        return new Viewer(this.getId(), this.getName(), this.getPassword());
    }

    @Override
    public void update(){
        FileUtils.updateUser(this);
    }
}
