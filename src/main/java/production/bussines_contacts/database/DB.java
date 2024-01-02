package production.bussines_contacts.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import production.bussines_contacts.models.Company;
import production.bussines_contacts.models.Contact;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DB {
    private static Connection getConnection() {
        return Database.getInstance().getConnection();
    }

    private static ConnectionSource getConnectionSource() {
        return ORMDatabase.getInstance().getConnectionSource();
    }

    public static ArrayList<Company> fetchCompanies() {
        try {
            Dao<Company, Long> companyDao = DaoManager.createDao(getConnectionSource(), Company.class);
            companyDao.queryForAll().forEach(System.out::println);

            return new ArrayList<>(companyDao.queryForAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void updateCompany(Company company) {
        try {
            Dao<Company, Long> companyDao = DaoManager.createDao(getConnectionSource(), Company.class);
            companyDao.update(company);
            System.out.println("Company updated: " + company.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating company: " + company.getName());
        }
    }

    public static void deleteCompany(Company company) {
        try {
            Dao<Company, Long> companyDao = DaoManager.createDao(getConnectionSource(), Company.class);
            Dao<Contact, Long> contactDao = DaoManager.createDao(getConnectionSource(), Contact.class);

            // Retrieve all contacts associated with the company
            List<Contact> contacts = contactDao.queryForEq("company_id", company.getId());

            // Delete all retrieved contacts
            for (Contact contact : contacts) {
                contactDao.delete(contact);
                System.out.println("Contact deleted: " + contact.getName());
            }

            companyDao.delete(company);
            System.out.println("Company deleted: " + company.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting company: " + company.getName());
        }
    }

    public static void createCompany(Company company) {
        try {
            Dao<Company, Long> companyDao = DaoManager.createDao(getConnectionSource(), Company.class);
            companyDao.create(company);
            System.out.println("Company created: " + company.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error creating company: " + company.getName());
        }
    }

    public static void createContact(Contact contact) {
        try {
            Dao<Contact, Long> contactDao = DaoManager.createDao(getConnectionSource(), Contact.class);
            contactDao.create(contact);
            System.out.println("Contact created: " + contact.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error creating contact: " + contact.getName());
        }
    }

    public static void updateContact(Contact contact) {
        try {
            Dao<Contact, Long> contactDao = DaoManager.createDao(getConnectionSource(), Contact.class);
            contactDao.update(contact);
            System.out.println("Contact updated: " + contact.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating contact: " + contact.getName());
        }
    }

    public static void deleteContact(Contact contact) {
        try {
            Dao<Contact, Long> contactDao = DaoManager.createDao(getConnectionSource(), Contact.class);
            contactDao.delete(contact);
            System.out.println("Contact deleted: " + contact.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting contact: " + contact.getName());
        }
    }

    public static List<Contact> fetchContacts() {
        try {
            Dao<Contact, Long> contactDao = DaoManager.createDao(getConnectionSource(), Contact.class);

            return contactDao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
