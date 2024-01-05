package production.bussines_contacts.threads;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import production.bussines_contacts.models.ChangeDataModel;
public class ChangeLogReaderTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(ChangeLogReaderTask.class);
    private static final String LOG_FILE_PATH = "change_log/changeLog.dat";
    private final Object fileLock;
    private List<ChangeDataModel<?>> changeDataModels;

    public ChangeLogReaderTask(Object fileLock) {
        this.fileLock = fileLock;
        this.changeDataModels = new ArrayList<>();
    }

    @Override
    public void run() {
        synchronized(fileLock) {
            File logFile = new File(LOG_FILE_PATH);

            if (!logFile.exists() || logFile.length() == 0) {
                logger.warn("Change log file is empty or does not exist");
                return;
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
                logger.error("Error reading from change log", e);
                changeDataModels = new ArrayList<>();
            }
        }
    }

    public List<ChangeDataModel<?>> getChangeDataModels() {
        return changeDataModels;
    }
}
