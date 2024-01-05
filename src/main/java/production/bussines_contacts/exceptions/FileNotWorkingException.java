package production.bussines_contacts.exceptions;

public class FileNotWorkingException extends Exception {
    public FileNotWorkingException() {
        super("File operation failed");
    }

    public FileNotWorkingException(String message) {
        super(message);
    }

    public FileNotWorkingException(String message, Throwable cause) {
        super(message, cause);
    }
}
