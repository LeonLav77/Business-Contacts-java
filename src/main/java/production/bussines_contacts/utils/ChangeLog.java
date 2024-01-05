package production.bussines_contacts.utils;

import production.bussines_contacts.Application;
import production.bussines_contacts.controllers.ImportOptionsController;
import production.bussines_contacts.interfaces.Editable;
import production.bussines_contacts.models.ChangeDataModel;
import production.bussines_contacts.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.bussines_contacts.threads.ChangeLogReaderTask;
import production.bussines_contacts.threads.ChangeLogWriterTask;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ChangeLog {
    private static final Logger logger = LoggerFactory.getLogger(ChangeLog.class);
    private static final Object fileLock = new Object();

    public static List<ChangeDataModel<?>> readFromChangeLog() {
        ChangeLogReaderTask readerTask = new ChangeLogReaderTask(fileLock);
        Thread thread = new Thread(readerTask);
        thread.start();

        try {
            thread.join();
            return readerTask.getChangeDataModels();
        } catch (InterruptedException e) {
            logger.error("Error reading from change log", e);
            Thread.currentThread().interrupt();
            return new ArrayList<>();
        }
    }

    public static <T extends Editable<T>> void persistChanges(Map<String, Map<String, String>> differences, T changedObject) {
        ChangeDataModel<T> changeDataModel = new ChangeDataModel.Builder<T>()
                .withUser(Application.getLoggedInUser())
                .atTime(new Date())
                .withDifferences(differences)
                .forObject(changedObject)
                .build();

        ChangeLogWriterTask changeLogWriterTask = new ChangeLogWriterTask(changeDataModel, fileLock);
        Thread changeLogWriterThread = new Thread(changeLogWriterTask);
        changeLogWriterThread.start();
    }
}
