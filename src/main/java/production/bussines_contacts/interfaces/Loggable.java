package production.bussines_contacts.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.bussines_contacts.controllers.ImportOptionsController;

public sealed interface Loggable permits ImportOptionsController {
    default void log(String message, Throwable throwable) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.error(message, throwable);
    }
}
