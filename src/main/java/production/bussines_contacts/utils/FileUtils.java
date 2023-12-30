package production.bussines_contacts.utils;

import production.bussines_contacts.enums.Role;
import production.bussines_contacts.models.Admin;
import production.bussines_contacts.models.User;
import production.bussines_contacts.models.Viewer;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

public class FileUtils {
    private static final Logger logger = Logger.getLogger(FileUtils.class.getName());

    public static List<User> readUsersFromFile() {
        String filePath = User.STORAGE_FILE_NAME;
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                Long id = Long.parseLong(parts[0]);
                String name = parts[1];
                String password = new String(Base64.getDecoder().decode(parts[2]));
                Role role = (Integer.parseInt(parts[3]) == 0) ? Role.VIEWER : Role.ADMIN;

                User user = (role == Role.ADMIN) ? new Admin(id, name, password) : new Viewer(id, name, password);
                users.add(user);
            }
        } catch (IOException e) {
            logger.severe("Error reading from file: " + e.getMessage());
            return new ArrayList<>();
        }
        return users;
    }

    public static boolean insertUser(User user) {
        String filePath = User.STORAGE_FILE_NAME;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            int roleIndicator = (user instanceof Admin) ? 1 : 0; // Check if the user is an instance of Admin
            String userData = user.getId() + " - " + user.getName() + " - " +
                    Base64.getEncoder().encodeToString(user.getPassword().getBytes()) + " - " +
                    roleIndicator;
            writer.write(userData);
            writer.newLine();
            return true; // Return true if insertion is successful
        } catch (IOException e) {
            logger.severe("Error inserting user into file: " + e.getMessage());
            return false; // Return false if an exception occurred
        }
    }

    public static Long getNextUserId() {
        String filePath = User.STORAGE_FILE_NAME;
        Long highestId = 0L;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                Long id = Long.parseLong(parts[0]);
                if (id > highestId) {
                    highestId = id;
                }
            }
        } catch (IOException e) {
            logger.severe("Error reading from file to get next user ID: " + e.getMessage());
        }
        return highestId + 1;
    }
}
