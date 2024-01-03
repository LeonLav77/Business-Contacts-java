package production.bussines_contacts.models;

import production.bussines_contacts.controllers.MenuController;
import production.bussines_contacts.enums.Role;

import java.util.HashMap;
import java.util.Map;

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
}
