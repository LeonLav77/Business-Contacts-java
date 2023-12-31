package production.bussines_contacts.database;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class CustomMigrationTool {

    private static final String MIGRATIONS_PATH = "/db";
    private static final String DATABASE_FILE = "conf/database.properties";

    public static void runMigrations() {
        // Load database properties
        try (FileReader reader = new FileReader(DATABASE_FILE)) {
            Properties loginInfo = new Properties();
            loginInfo.load(reader);

            String dbUrl = loginInfo.getProperty("databaseUrl");
            String dbUsername = loginInfo.getProperty("username");
            String dbPassword = loginInfo.getProperty("password");


        // Connect to the database
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            // List files in the migrations directory
            try {
                Files.list(Paths.get(CustomMigrationTool.class.getResource(MIGRATIONS_PATH).toURI()))
                        .forEach(path -> {
                            // Execute SQL in each file
                            try {
                                System.out.println("Executing migration: " + path);
                                String sql = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                                try (Statement stmt = conn.createStatement()) {
                                    stmt.execute(sql);
                                }
                            } catch (IOException | SQLException e) {
                                throw new RuntimeException("Error executing migration: " + path, e);
                            }
                        });
            } catch (Exception e) {
                throw new RuntimeException("Error listing migration files", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);
        }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
