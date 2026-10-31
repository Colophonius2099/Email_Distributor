package Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class MyCustomFormatter extends Formatter {
    // Date format
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();

        // Date and time
        sb.append(dateFormat.format(new Date(record.getMillis()))).append(" - ");

        // Log level
        sb.append(record.getLevel().getName()).append(" || ");

        // Logger name
        sb.append(record.getLoggerName()).append(" || ");

        // Log message
        sb.append(formatMessage(record)).append(System.lineSeparator());

        // Throwable
        if (record.getThrown() != null) {
            try {
                sb.append(record.getThrown().toString()).append(System.lineSeparator());
                for (StackTraceElement element : record.getThrown().getStackTrace()) {
                    sb.append("\tat ").append(element.toString()).append(System.lineSeparator());
                }
            } catch (Exception ex) {
                // Ignored
            }
        }

        return sb.toString();
    }
}
