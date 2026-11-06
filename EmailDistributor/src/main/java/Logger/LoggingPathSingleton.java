package Logger;

import Main.ConfigReader;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Level;

public class LoggingPathSingleton {

    private static String LOGGING_FILE = null;
    private static String LOGGING_FOLDER = null;
    private static FileHandler LOGGING_FILEHANDLER = null;

    private LoggingPathSingleton() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        LOGGING_FOLDER = ConfigReader.getInstance().getLoggingPath();
        LOGGING_FILE = LOGGING_FOLDER
                + "/"
                + "Logs_("
                + LocalDateTime.now(Clock.systemDefaultZone()).format(formatter)
                + ").txt";

        File folder = new File(LOGGING_FOLDER);

        if (!folder.exists()) {
            folder.mkdir();
        }

        FileHandler fileHandler = new FileHandler(LOGGING_FILE, true);

        fileHandler.setLevel(Level.ALL);
        fileHandler.setFormatter(new MyCustomFormatter());
        LOGGING_FILEHANDLER = fileHandler;
    }

    protected static String GET_LOGGINGFILE() throws IOException {
        if (Objects.isNull(LOGGING_FILE)) {
            new LoggingPathSingleton();
        }
        return LOGGING_FILE;
    }

    protected static String GET_LOGGINGFOLDER() throws IOException {
        if (Objects.isNull(LOGGING_FOLDER)) {
            new LoggingPathSingleton();
        }
        return LOGGING_FOLDER;
    }

    protected static FileHandler GET_LOGGING_FILEHANDLER() throws IOException {
        if (Objects.isNull(LOGGING_FOLDER)) {
            new LoggingPathSingleton();
        }
        return LOGGING_FILEHANDLER;
    }
}
