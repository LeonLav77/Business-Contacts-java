package production.bussines_contacts.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import production.bussines_contacts.controllers.MenuController;
import production.bussines_contacts.database.DB;
import production.bussines_contacts.enums.Importance;
import production.bussines_contacts.interfaces.Deletable;
import production.bussines_contacts.interfaces.Editable;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@DatabaseTable(tableName = "contacts")
public class Contact implements Editable, Deletable, Serializable {
    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField
    private String department;

    @DatabaseField(columnName = "company_id", foreign = true, foreignAutoRefresh = true)
    private Company company;

    @DatabaseField
    private Date created_at;

    @DatabaseField(dataType = com.j256.ormlite.field.DataType.ENUM_STRING)
    private Importance importance;

    @DatabaseField
    private String phone_number;

    @DatabaseField
    private String custom_note;

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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Importance getImportance() {
        return importance;
    }

    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCustom_note() {
        return custom_note;
    }

    public void setCustom_note(String custom_note) {
        this.custom_note = custom_note;
    }

    // Constructors, Getters, and Setters

    public Contact() {
        // ORMLite needs a no-arg constructor
    }

    public Contact(Long id, String name, String department, Company company, Date created_at, Importance importance, String phone_number, String custom_note) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.company = company;
        this.created_at = created_at;
        this.importance = importance;
        this.phone_number = phone_number;
        this.custom_note = custom_note;
    }

    @Override
    public void edit() {
        MenuController.editContact(this);
    }

    @Override
    public String toString() {
        return name + " - " + company.getName();
    }

    public void delete(){
        DB.deleteContact(this);
    }

    @Override
    public String deleteText() {
        return "Delete " + this + "?" + "\n" + "This action cannot be undone";
    }
    // Add your constructors, getters, and setters here
    public Map<String, Map<String, String>> getDifferencesMap(Contact changedContact) {
        Map<String, Map<String, String>> changes = new HashMap<>();

        if (!this.getName().equals(changedContact.getName())) {
            Map<String, String> nameChange = new HashMap<>();
            nameChange.put("old", this.getName());
            nameChange.put("new", changedContact.getName());
            changes.put("name", nameChange);
        }

        if (!this.getDepartment().equals(changedContact.getDepartment())) {
            Map<String, String> departmentChange = new HashMap<>();
            departmentChange.put("old", this.getDepartment());
            departmentChange.put("new", changedContact.getDepartment());
            changes.put("department", departmentChange);
        }

        // Assuming Company has an overridden equals method
        if (this.getCompany() != null && !this.getCompany().equals(changedContact.getCompany())) {
            Map<String, String> companyChange = new HashMap<>();
            companyChange.put("old", this.getCompany().getName());
            companyChange.put("new", changedContact.getCompany().getName());
            changes.put("company", companyChange);
        }

        if (this.getImportance() != changedContact.getImportance()) {
            Map<String, String> importanceChange = new HashMap<>();
            importanceChange.put("old", this.getImportance().toString());
            importanceChange.put("new", changedContact.getImportance().toString());
            changes.put("importance", importanceChange);
        }

        if (!this.getPhone_number().equals(changedContact.getPhone_number())) {
            Map<String, String> phoneNumberChange = new HashMap<>();
            phoneNumberChange.put("old", this.getPhone_number());
            phoneNumberChange.put("new", changedContact.getPhone_number());
            changes.put("phone_number", phoneNumberChange);
        }

        if (!this.getCustom_note().equals(changedContact.getCustom_note())) {
            Map<String, String> customNoteChange = new HashMap<>();
            customNoteChange.put("old", this.getCustom_note());
            customNoteChange.put("new", changedContact.getCustom_note());
            changes.put("custom_note", customNoteChange);
        }

        // Add similar blocks for other fields you want to track changes for

        return changes;
    }
}
