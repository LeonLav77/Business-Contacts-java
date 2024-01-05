package production.bussines_contacts.threads;

import production.bussines_contacts.models.ChangeDataModel;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChangeLogWriterTask extends Thread {
    private static final String LOG_FILE_PATH = "change_log/changeLog.dat";
    private static final Logger logger = Logger.getLogger(ChangeLogWriterTask.class.getName());
    private final ChangeDataModel<?> changeDataModel;

    public ChangeLogWriterTask(ChangeDataModel<?> changeDataModel) {
        this.changeDataModel = changeDataModel;
    }

    @Override
    public void run() {
        File file = new File(LOG_FILE_PATH);
        boolean append = file.exists() && file.length() > 0;

        try (ObjectOutputStream out = append
                ? new AppendingObjectOutputStream(new FileOutputStream(file, true))
                : new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(changeDataModel);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing to change log", e);
        }
    }

    private static class AppendingObjectOutputStream extends ObjectOutputStream {
        AppendingObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }
}
