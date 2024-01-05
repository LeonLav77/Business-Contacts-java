package production.bussines_contacts.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import production.bussines_contacts.models.Company;
import production.bussines_contacts.models.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DB {
    private static final Logger logger = LoggerFactory.getLogger(DB.class);

    private static Connection getConnection() {
        return Database.getInstance().getConnection();
    }

    private static ConnectionSource getConnectionSource() {
        return ORMDatabase.getInstance().getConnectionSource();
    }

    public static ArrayList<Company> fetchCompanies() {
        try {
            Dao<Company, Long> companyDao = DaoManager.createDao(getConnectionSource(), Company.class);
            return new ArrayList<>(companyDao.queryForAll());
        } catch (Exception e) {
            logger.error("Error fetching companies", e);
            return new ArrayList<>();
        }
    }

    public static void updateCompany(Company company) {
        try {
            Dao<Company, Long> companyDao = DaoManager.createDao(getConnectionSource(), Company.class);
            companyDao.update(company);
        } catch (SQLException e) {
            logger.error("Error updating company", e);
        }
    }

    public static void deleteCompany(Company company) {
        try {
            Dao<Company, Long> companyDao = DaoManager.createDao(getConnectionSource(), Company.class);
            Dao<Contact, Long> contactDao = DaoManager.createDao(getConnectionSource(), Contact.class);

            List<Contact> contacts = contactDao.queryForEq("company_id", company.getId());

            for (Contact contact : contacts) {
                contactDao.delete(contact);
            }

            companyDao.delete(company);
        } catch (SQLException e) {
            logger.error("Error deleting company", e);
        }
    }

    public static void createCompany(Company company) {
        try {
            Dao<Company, Long> companyDao = DaoManager.createDao(getConnectionSource(), Company.class);
            companyDao.create(company);
        } catch (SQLException e) {
            logger.error("Error creating company", e);
        }
    }

    public static void createContact(Contact contact) {
        try {
            Dao<Contact, Long> contactDao = DaoManager.createDao(getConnectionSource(), Contact.class);
            contactDao.create(contact);
        } catch (SQLException e) {
            logger.error("Error creating contact", e);
        }
    }

    public static void updateContact(Contact contact) {
        try {
            Dao<Contact, Long> contactDao = DaoManager.createDao(getConnectionSource(), Contact.class);
            contactDao.update(contact);
        } catch (SQLException e) {
            logger.error("Error updating contact", e);
        }
    }

    public static void deleteContact(Contact contact) {
        try {
            Dao<Contact, Long> contactDao = DaoManager.createDao(getConnectionSource(), Contact.class);
            contactDao.delete(contact);
        } catch (SQLException e) {
            logger.error("Error deleting contact", e);
        }
    }

    public static List<Contact> fetchContacts() {
        try {
            Dao<Contact, Long> contactDao = DaoManager.createDao(getConnectionSource(), Contact.class);
            return contactDao.queryForAll();
        } catch (SQLException e) {
            logger.error("Error fetching contacts", e);
            return new ArrayList<>();
        }
    }
}
