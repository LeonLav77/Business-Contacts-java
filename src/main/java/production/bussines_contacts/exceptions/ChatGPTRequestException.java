package production.bussines_contacts.exceptions;

public class ChatGPTRequestException extends Exception {
    public ChatGPTRequestException(String message) {
        super(message);
    }
}