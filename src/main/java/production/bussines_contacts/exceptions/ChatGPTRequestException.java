package production.bussines_contacts.exceptions;

public class ChatGPTRequestException extends Exception {

    // Constructor that accepts a String message
    public ChatGPTRequestException(String message) {
        super(message);
    }
}