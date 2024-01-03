package production.bussines_contacts.models;

import production.bussines_contacts.enums.Role;
import production.bussines_contacts.interfaces.Deletable;
import production.bussines_contacts.interfaces.Editable;
import production.bussines_contacts.utils.FileUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

public abstract class User implements Editable, Deletable, Serializable {
    public static final String STORAGE_FILE_NAME = "dat/users.txt";
    protected Long id;
    protected String name;
    protected String password;
    @Serial
    private static final long serialVersionUID = 1L; // Unique version identifier


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
    public abstract Map<String, Map<String, String>> getDifferencesMap(User user);

}
