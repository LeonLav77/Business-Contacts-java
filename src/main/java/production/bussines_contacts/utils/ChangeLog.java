package production.bussines_contacts.utils;

import production.bussines_contacts.Application;
import production.bussines_contacts.interfaces.Editable;
import production.bussines_contacts.models.ChangeDataModel;
import production.bussines_contacts.models.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ChangeLog {

    private static final String LOG_FILE_PATH = "change_log/changeLog.dat";

    public static void writeToChangeLog(ChangeDataModel<?> changeDataModel) {
        File file = new File(LOG_FILE_PATH);
        boolean append = file.exists() && file.length() > 0;

        try (ObjectOutputStream out = append
                ? new AppendingObjectOutputStream(new FileOutputStream(file, true))
                : new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(changeDataModel);
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    private static class AppendingObjectOutputStream extends ObjectOutputStream {
        AppendingObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            // Do not write a header, but reset:
            reset();
        }
    }

    public static List<ChangeDataModel<?>> readFromChangeLog() {
        List<ChangeDataModel<?>> changeDataModels = new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(LOG_FILE_PATH))) {
            while (true) {
                try {
                    ChangeDataModel<?> changeDataModel = (ChangeDataModel<?>) in.readObject();
                    changeDataModels.add(changeDataModel);
                } catch (EOFException e) {
                    break; // End of file reached
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return changeDataModels;
    }

    public static void persistChanges(Map<String, Map<String, String>> differences, Object changedObject) {
        ChangeDataModel<?> changeDataModel = new ChangeDataModel.Builder<>()
                .withUser(Application.getLoggedInUser())
                .atTime(new Date())
                .withDifferences(differences)
                .forObject((Editable) changedObject)
                .build();

        writeToChangeLog(changeDataModel);
        logChangeData();
    }

    // New method to log change data
    public static void logChangeData() {
        try {
            List<ChangeDataModel<?>> changeLog = readFromChangeLog();
        } catch (Exception e) {
            e.printStackTrace(); // Consider using a logging framework here
        }
    }
}
