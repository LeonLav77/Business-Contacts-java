package production.bussines_contacts.utils;

import production.bussines_contacts.Application;
import production.bussines_contacts.controllers.ImportOptionsController;
import production.bussines_contacts.interfaces.Editable;
import production.bussines_contacts.models.ChangeDataModel;
import production.bussines_contacts.models.User;
import production.bussines_contacts.threads.ChangeLogWriterTask;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChangeLog {
    private static final Logger logger = Logger.getLogger(ImportOptionsController.class.getName());
    private static final String LOG_FILE_PATH = "change_log/changeLog.dat";

    public static List<ChangeDataModel<?>> readFromChangeLog() {
        List<ChangeDataModel<?>> changeDataModels = new ArrayList<>();
        File logFile = new File(LOG_FILE_PATH);

        if (!logFile.exists() || logFile.length() == 0) {
            logger.log(Level.WARNING, "Change log file is empty or does not exist");
            return changeDataModels;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(logFile))) {
            while (true) {
                try {
                    ChangeDataModel<?> changeDataModel = (ChangeDataModel<?>) in.readObject();
                    changeDataModels.add(changeDataModel);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error reading from change log", e);
            return new ArrayList<>();
        }

        return changeDataModels;
    }

    public synchronized static <T extends Editable<T>> void persistChanges(Map<String, Map<String, String>> differences, T changedObject) {
        ChangeDataModel<T> changeDataModel = new ChangeDataModel.Builder<T>()
                .withUser(Application.getLoggedInUser())
                .atTime(new Date())
                .withDifferences(differences)
                .forObject(changedObject)
                .build();

        ChangeLogWriterTask changeLogWriterTask = new ChangeLogWriterTask(changeDataModel);
        Thread changeLogWriterThread = new Thread(changeLogWriterTask);
        changeLogWriterThread.start();
    }
}
