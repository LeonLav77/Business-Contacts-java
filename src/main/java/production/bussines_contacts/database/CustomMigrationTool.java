package production.bussines_contacts.database;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Properties;

public class CustomMigrationTool {

    private static final String MIGRATIONS_PATH = "/db";
    private static final String DATABASE_FILE = "conf/database.properties";

    public static void runMigrations() {
        try (FileReader reader = new FileReader(DATABASE_FILE)) {
            Properties loginInfo = new Properties();
            loginInfo.load(reader);

            String dbUrl = loginInfo.getProperty("databaseUrl");
            String dbUsername = loginInfo.getProperty("username");
            String dbPassword = loginInfo.getProperty("password");

            try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
                try {
                    Files.list(Paths.get(Objects.requireNonNull(CustomMigrationTool.class.getResource(MIGRATIONS_PATH)).toURI()))
                            .forEach(path -> {
                                try {
                                    System.out.println("Executing migration: " + path);
                                    String sql = Files.readString(path);
                                    try (Statement stmt = conn.createStatement()) {
                                        stmt.execute(sql);
                                    }
                                    System.out.println("Migration executed successfully: " + path);
                                } catch (IOException | SQLException e) {
                                    throw new RuntimeException("Error executing migration: " + path, e);
                                }
                            });
                } catch (IOException e) {
                    throw new RuntimeException("Error listing migration files", e);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Database connection error", e);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Database properties file not found", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading database properties file", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error", e);
        }
    }
}
