package production.bussines_contacts.models;

import production.bussines_contacts.controllers.MenuController;
import production.bussines_contacts.enums.Role;

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
    public Map<String, Map<String, String>> getDifferencesMap(User otherUser) {
        Map<String, Map<String, String>> changes = new HashMap<>();

        if (!this.getName().equals(otherUser.getName())) {
            Map<String, String> nameChange = new HashMap<>();
            nameChange.put("old", this.getName());
            nameChange.put("new", otherUser.getName());
            changes.put("name", nameChange);
        }

        if(this.getRole() != otherUser.getRole()) {
            Map<String, String> roleChange = new HashMap<>();
            roleChange.put("old", this.getRole().getRoleName());
            roleChange.put("new", otherUser.getRole().getRoleName());
            changes.put("role", roleChange);
        }

        return changes;
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
