package production.bussines_contacts.threads;

import production.bussines_contacts.models.ChangeDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.logging.Level;

public class ChangeLogWriterTask extends Thread {
    private static final String LOG_FILE_PATH = "change_log/changeLog.dat";
    private static final Logger logger = LoggerFactory.getLogger(ChangeLogWriterTask.class);
    private final ChangeDataModel<?> changeDataModel;
    private final Object fileLock;

    public ChangeLogWriterTask(ChangeDataModel<?> changeDataModel, Object fileLock) {
        this.changeDataModel = changeDataModel;
        this.fileLock = fileLock;
    }

    @Override
    public void run() {
        synchronized (fileLock) {
            File file = new File(LOG_FILE_PATH);
            boolean append = file.exists() && file.length() > 0;

            try (ObjectOutputStream out = append
                    ? new AppendingObjectOutputStream(new FileOutputStream(file, true))
                    : new ObjectOutputStream(new FileOutputStream(file))) {
                out.writeObject(changeDataModel);
            } catch (IOException e) {
                logger.error("Error writing to change log", e);
            }
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
