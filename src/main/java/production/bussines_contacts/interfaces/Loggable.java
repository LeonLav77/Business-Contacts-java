package production.bussines_contacts.interfaces;

import production.bussines_contacts.controllers.ImportOptionsController;

import java.util.logging.Level;
import java.util.logging.Logger;

public sealed interface Loggable permits ImportOptionsController {
    default void log(Level level, String message, Throwable throwable) {
        Logger.getLogger(this.getClass().getName()).log(level, message, throwable);
    }
}