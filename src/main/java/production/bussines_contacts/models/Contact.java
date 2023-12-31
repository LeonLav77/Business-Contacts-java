package production.bussines_contacts.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import production.bussines_contacts.enums.Importance;
import production.bussines_contacts.interfaces.Editable;

import java.util.Date;

@DatabaseTable(tableName = "contacts")
public class Contact implements Editable {
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

    public void edit(){
        System.out.println("Editing contact");
    }


    // Add your constructors, getters, and setters here
}
