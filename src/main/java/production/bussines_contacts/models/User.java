package production.bussines_contacts.models;

import production.bussines_contacts.controllers.MenuController;
import production.bussines_contacts.enums.Role;
import production.bussines_contacts.interfaces.Deletable;
import production.bussines_contacts.interfaces.Editable;
import production.bussines_contacts.interfaces.Importable;
import production.bussines_contacts.utils.FileUtils;
import production.bussines_contacts.utils.FunctionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class User implements Editable<User>, Deletable, Serializable, Cloneable, Importable<User>{
    public static final String STORAGE_FILE_NAME = "dat/users.txt";
    protected Long id;
    protected String name;
    protected String password;
    @Serial
    private static final long serialVersionUID = 1L; // Unique version identifier

    public User() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // You can also include abstract methods that must be implemented by subclasses
    public abstract Role getRole();
    public abstract boolean isAdmin();

    @Override
    public void delete() {
        FileUtils.deleteUser(this);
    }
    @Override
    public Map<String, Map<String, String>> getDifferencesMap(User otherUser) {
        Map<String, Map<String, String>> changes = new HashMap<>();

        FunctionUtils.addChange(changes, "name", this.getName(), otherUser.getName());
        FunctionUtils.addChange(changes, "role" , this.getRole().getRoleName(), otherUser.getRole().getRoleName());

        return changes;
    }
    public abstract User clone();
    public void redirectToConfirmScreen(List<User> items) {
//        FunctionUtils.redirectToConfirmScreen(items, "user");
        MenuController.showIndexScreen();
    }

    public void update(){
        FileUtils.updateUser(this);
    }

    public void save() {
        FileUtils.insertUser(this);
    }
}
