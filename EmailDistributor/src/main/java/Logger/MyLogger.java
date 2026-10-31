package Logger;

import Main.ConfigReader;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.logging.*;

public class MyLogger {

    private final Logger LOGGER;
    private final String filepath = ConfigReader.getInstance().getLoggingPath();
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


        // Create a file handler
        File folder = new File(filepath);

        if (!folder.exists()) {
            folder.mkdir();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        String filename = filepath
                        + "Logs_("
                        + LocalDateTime.now(Clock.systemDefaultZone()).format(formatter)
                        + ").txt";

        FileHandler fileHandler = null;

        try {
            fileHandler = new FileHandler(filename, true);
        } catch (IOException e) {
            log(e.getMessage());
        }

        fileHandler.setLevel(Level.ALL);
        fileHandler.setFormatter(new MyCustomFormatter());

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
