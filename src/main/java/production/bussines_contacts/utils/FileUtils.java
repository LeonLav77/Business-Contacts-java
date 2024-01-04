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
                Role role = (Integer.parseInt(parts[3]) == 1) ? Role.VIEWER : Role.ADMIN;

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
            Long roleIndicator = (user instanceof Admin) ? Role.ADMIN.getId() : Role.VIEWER.getId();

            // If user.id is null, find the last ID and increment it by 1
            if (user.getId() == null) {
                user.setId(getNextUserId());
            }

            String userData = user.getId() + " - " + user.getName() + " - " +
                    Base64.getEncoder().encodeToString(user.getPassword().getBytes()) + " - " +
                    roleIndicator;
            writer.write(userData);
            writer.newLine();
            return true;
        } catch (IOException e) {
            logger.severe("Error inserting user into file: " + e.getMessage());
            return false;
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

    public static boolean updateUser(User updatedUser) {
        String filePath = User.STORAGE_FILE_NAME;
        List<User> users = readUsersFromFile(); // Step 1: Read all users
        boolean userFound = false;

        // Step 2: Find and update the user
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(updatedUser.getId())) {
                users.set(i, updatedUser);
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            logger.warning("User with ID " + updatedUser.getId() + " not found.");
            return false;
        }

        // Step 3: Write all users back to the file
        return writeUsersToFile(users);
    }

    private static boolean writeUsersToFile(List<User> users) {
        String filePath = User.STORAGE_FILE_NAME;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (User user : users) {
                Long roleIndicator = (user instanceof Admin) ? Role.ADMIN.getId() : Role.VIEWER.getId();
                String userData = user.getId() + " - " + user.getName() + " - " +
                        Base64.getEncoder().encodeToString(user.getPassword().getBytes()) + " - " +
                        roleIndicator;
                writer.write(userData);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            logger.severe("Error writing users to file: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteUser(User user) {
        Long userId = user.getId();
        List<User> users = readUsersFromFile(); // Step 1: Read all users
        boolean userFound = false;

        // Step 2: Find and remove the user
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(userId)) {
                users.remove(i);
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            logger.warning("User with ID " + userId + " not found.");
            return false;
        }

        // Step 3: Write the updated list back to the file
        return writeUsersToFile(users);
    }
}
