package production.bussines_contacts.models;

import production.bussines_contacts.controllers.MenuController;
import production.bussines_contacts.enums.Role;
import production.bussines_contacts.utils.FileUtils;

import java.io.Serial;

public final class Viewer extends User {
    @Serial
    private static final long serialVersionUID = 1L;

    public Viewer(Long id, String name, String password) {
        super(id, name, password);
    }

    public Viewer() {
        super();
    }

    @Override
    public Role getRole() {
        return Role.VIEWER;
    }

    @Override
    public boolean isAdmin() {
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
    @Override
    public int getImportanceValue() {
        return 1;
    }

}
