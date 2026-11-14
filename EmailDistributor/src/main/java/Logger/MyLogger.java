package Logger;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.*;

public class MyLogger {

    private final Logger LOGGER;
    private static MyLogger genericLogger = null;

    public MyLogger(String className) {
        LogManager.getLogManager().reset();
        LOGGER = Logger.getLogger(className);

        Handler[] existingHandlers = LOGGER.getHandlers();
        for (Handler handler : existingHandlers) {
            LOGGER.removeHandler(handler);
        }

        // Create a console handler
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new MyCustomFormatter());

        FileHandler fileHandler = null;

        try {
            fileHandler = LoggingPathSingleton.GET_LOGGING_FILEHANDLER();
        } catch (IOException e) {
            log(e.getMessage());
        }

        // Add handlers to the logger
        LOGGER.addHandler(consoleHandler);
        LOGGER.addHandler(fileHandler);

        // Set the logger level
        LOGGER.setLevel(Level.ALL);
    }

    public static MyLogger GET_GENERIC_LOGGER() {
        if (Objects.isNull(MyLogger.genericLogger)) {
            genericLogger = new MyLogger("Email Distributor Generic Logger");
        }
        return genericLogger;
    }

    public void log(String message){
        LOGGER.log(Level.INFO, message);
    }


}
