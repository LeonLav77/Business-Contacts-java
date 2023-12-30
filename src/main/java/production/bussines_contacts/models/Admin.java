package production.bussines_contacts.models;

import production.bussines_contacts.enums.Role;

public class Admin extends User {

    public Admin(Long id, String name, String password) {
        super(id, name, password);
    }

    @Override
    public Role getRole() {
        // Implementation specific to Admin
        return Role.ADMIN;
    }
}
