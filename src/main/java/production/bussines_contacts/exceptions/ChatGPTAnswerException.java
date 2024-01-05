package production.bussines_contacts.exceptions;

public class ChatGPTAnswerException extends RuntimeException {

    public ChatGPTAnswerException(String message) {
        super(message);
    }

    public ChatGPTAnswerException(String message, Throwable cause) {
        super(message, cause);
    }
}