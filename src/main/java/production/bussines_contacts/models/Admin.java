package production.bussines_contacts.models;

import production.bussines_contacts.controllers.MenuController;
import production.bussines_contacts.enums.Role;
import production.bussines_contacts.utils.FileUtils;
import production.bussines_contacts.utils.FunctionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Admin extends User {

    public Admin(Long id, String name, String password) {
        super(id, name, password);
    }
    @Serial
    private static final long serialVersionUID = 1L; // Unique version identifier

    public Admin() {

    }

    @Override
    public Role getRole() {
        // Implementation specific to Admin
        return Role.ADMIN;
    }

    @Override
    public boolean isAdmin() {
        // Implementation specific to Admin
        return true;
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
    public Admin clone() {
        return new Admin(this.getId(), this.getName(), this.getPassword());
    }

    @Override
    public void update(){
        FileUtils.updateUser(this);
    }
}
