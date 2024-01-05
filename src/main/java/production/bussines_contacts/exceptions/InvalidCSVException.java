package production.bussines_contacts.exceptions;

public class InvalidCSVException extends RuntimeException {
    public InvalidCSVException() {
        super("Invalid CSV format");
    }

    public InvalidCSVException(String message) {
        super(message);
    }

    public InvalidCSVException(String message, Throwable cause) {
        super(message, cause);
    }
}
