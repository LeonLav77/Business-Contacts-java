package production.bussines_contacts.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import production.bussines_contacts.controllers.MenuController;
import production.bussines_contacts.database.DB;
import production.bussines_contacts.interfaces.Deletable;
import production.bussines_contacts.interfaces.Editable;
import production.bussines_contacts.interfaces.Importable;
import production.bussines_contacts.interfaces.Importantable;
import production.bussines_contacts.utils.FunctionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@DatabaseTable(tableName = "companies")
public final class Company implements Editable<Company>, Deletable, Serializable, Cloneable, Importable<Company>, Importantable {
    @Serial
    private static final long serialVersionUID = 1L;

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField
    private String industry;

    @DatabaseField
    private String headquarters;

    @DatabaseField
    private Date created_at;

    @DatabaseField
    private String website;
    public Company() {}

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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Company(Long id, String name, String industry, String headquarters, Date created_at, String website) {
        this.id = id;
        this.name = name;
        this.industry = industry;
        this.headquarters = headquarters;
        this.created_at = created_at;
        this.website = website;
    }

    @Override
    public void edit() {
        MenuController.editCompany(this);
    }

    public void delete(){
        DB.deleteCompany(this);
    }

    public void update() {
        DB.updateCompany(this);
    }
    public void save() {
        DB.createCompany(this);
    }

    @Override
    public Company clone() {
        return new Company(this.getId(), this.getName(), this.getIndustry(), this.getHeadquarters(), this.getCreated_at(), this.getWebsite());
    }

    @Override
    public String toString() {
        return this.getName();
    }
    @Override
    public String deleteText() {
        return "Delete " + this + "?" + "\n" + "This action cannot be undone.\n All of the contacts associated with this company will also be deleted.";
    }

    @Override
    public Map<String, Map<String, String>> getDifferencesMap(Company changedCompany) {
        Map<String, Map<String, String>> changes = new java.util.HashMap<>();

        FunctionUtils.addChange(changes, "name", this.getName(), changedCompany.getName());
        FunctionUtils.addChange(changes, "industry", this.getIndustry(), changedCompany.getIndustry());
        FunctionUtils.addChange(changes, "headquarters", this.getHeadquarters(), changedCompany.getHeadquarters());
        FunctionUtils.addChange(changes, "website", this.getWebsite(), changedCompany.getWebsite());

        return changes;
    }

    public void redirectToConfirmScreen(List<Company> companies) {
        MenuController.showReviewCompaniesScreen(companies);
    }

    public int getNumberOfColumns() {
        return 4;
    }

    public Company createItem(String[] data) {
        return new Company(null, data[0], data[1], data[2], new Date(), data[3]);
    }

    public String getCSVHeader() {
        return "Name,Industry,Headquarters,Website";
    }
    @Override
    public int getImportanceValue() {
        return 20;
    }
}
