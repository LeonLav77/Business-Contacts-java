package production.bussines_contacts.database;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class ORMDatabase {
    private static final Logger logger = LoggerFactory.getLogger(ORMDatabase.class);
    private ConnectionSource connectionSource;
    private static final String DATABASE_FILE = "conf/database.properties";

    private ORMDatabase() {
        try (FileReader reader = new FileReader(DATABASE_FILE)) {
            Properties loginInfo = new Properties();
            loginInfo.load(reader);

            String dbUrl = loginInfo.getProperty("databaseUrl");
            String dbUsername = loginInfo.getProperty("username");
            String dbPassword = loginInfo.getProperty("password");

            connectionSource = new JdbcConnectionSource(dbUrl, dbUsername, dbPassword);
        } catch (IOException | SQLException e) {
            logger.error("Error initializing database connection source.", e);
        }
    }

    private static class ORMDatabaseHolder {
        private static final ORMDatabase INSTANCE = new ORMDatabase();
    }

    public static ORMDatabase getInstance() {
        return ORMDatabaseHolder.INSTANCE;
    }

    public synchronized ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public synchronized void closeConnectionSource() {
        if (connectionSource != null) {
            try {
                connectionSource.close();
            } catch (IOException e) {
                logger.error("Error closing database connection source.", e);
            }
        }
    }
}
