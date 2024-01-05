package production.bussines_contacts.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import production.bussines_contacts.controllers.MenuController;
import production.bussines_contacts.database.DB;
import production.bussines_contacts.enums.Importance;
import production.bussines_contacts.interfaces.Deletable;
import production.bussines_contacts.interfaces.Editable;
import production.bussines_contacts.interfaces.Importable;
import production.bussines_contacts.utils.FunctionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@DatabaseTable(tableName = "contacts")
public final class Contact implements Editable<Contact>, Deletable, Serializable, Cloneable, Importable<Contact> {
    @Serial
    private static final long serialVersionUID = 1L;

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

    public Contact() {
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
    public Map<String, Map<String, String>> getDifferencesMap(Contact changedContact) {
        Map<String, Map<String, String>> changes = new HashMap<>();

        FunctionUtils.addChange(changes, "name", this.getName(), changedContact.getName());
        FunctionUtils.addChange(changes, "department", this.getDepartment(), changedContact.getDepartment());
        FunctionUtils.addChange(changes, "importance", this.getImportance().getDescription(), changedContact.getImportance().getDescription());
        FunctionUtils.addChange(changes, "phone_number", this.getPhone_number(), changedContact.getPhone_number());
        FunctionUtils.addChange(changes, "custom_note", this.getCustom_note(), changedContact.getCustom_note());

        return changes;
    }

    @Override
    public Contact clone() {
        try {
            return (Contact) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public void update(){
        DB.updateContact(this);
    }
    @Override
    public void redirectToConfirmScreen(List<Contact> contacts) {
        MenuController.showReviewContactsScreen(contacts);
    }

    public void save() {
        DB.createContact(this);
    }
    @Override
    public int getNumberOfColumns() {
        return 6;
    }
    @Override
    public Contact createItem(String[] data) {
        String companyName = data[0].trim();
        ArrayList<Company> companies = DB.fetchCompanies();

        Optional<Company> contactCompany = companies.stream()
                .filter(c -> c.getName().equals(companyName))
                .findFirst();

        if (contactCompany.isEmpty()) {
            throw new RuntimeException("Company not found");
        }

        return contactCompany.map(company ->
                        new Contact(null, data[1], data[2], company, new Date(),
                                Importance.valueOf(data[3].trim().toUpperCase()), data[4], data[5]))
                .orElse(null);
    }

    public String getCSVHeader() {
        return "CompanyName,Name,Department,Importance,PhoneNumber,CustomNote";
    }
}
