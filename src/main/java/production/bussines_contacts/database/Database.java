package production.bussines_contacts.database;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    private Connection connection;
    private static final String DATABASE_FILE = "conf/database.properties";

    private Database() {
        try (FileReader reader = new FileReader(DATABASE_FILE)) {
            Properties loginInfo = new Properties();
            loginInfo.load(reader);

            String dbUrl = loginInfo.getProperty("databaseUrl");
            String dbUsername = loginInfo.getProperty("username");
            String dbPassword = loginInfo.getProperty("password");

            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (IOException | SQLException e) {
            logger.error("Error initializing database connection.", e);
        }
    }

    private static class DatabaseHolder {
        private static final Database INSTANCE = new Database();
    }

    public static Database getInstance() {
        return DatabaseHolder.INSTANCE;
    }

    public synchronized Connection getConnection() {
        return connection;
    }

    public synchronized void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("Error closing database connection.", e);
        }
    }
}
