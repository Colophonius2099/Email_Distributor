package MailList;

import Filter.FilterHelper;
import Logger.MyLogger;
import Main.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class MailListCSVCreator {

    private final String filePath = ConfigReader.getInstance().getMailListFilePath();
    private final MyLogger logger = new MyLogger("MailListCSVWriter");

    private String csvPattern(List<String> emailAddresses, FilterHelper.Distributor name, String note) {
        logger.log("Generating CSV Pattern for " + name.name());
        AtomicReference<String> listAsString = new AtomicReference<>("");
        emailAddresses.forEach(emailAddress -> listAsString.set(listAsString.get() + "; " + emailAddress));
        note = Objects.isNull(note) ? "" : note;
        return name.name() + ";" + note + listAsString;
    }

    private String generateNote(FilterHelper.Distributor distributor) {
        return switch (distributor) {
            case WiWoe, GuSp, CaEx, RaRo -> "Schickt aus an alle " + distributor.name();
            case Info -> "Schickt aus an alle Gruppenmitglieder";
            case ScoutFix -> "Schickt aus an alle für Scout Fix relevante";
            case Gruppenrat -> "Schickt aus an alle GruppenratteilnehmerInnen";
        };
    }

    protected void generateCSVFile(Map<FilterHelper.Distributor, List<String>> emailMap) {
        logger.log("Started generating CSV");
        AtomicReference<String> csvText = new AtomicReference<String>("");
        emailMap.keySet().forEach(
                key -> csvText.set(csvText.get() + csvPattern(emailMap.get(key), key, generateNote(key)) + "\n")
        );

        String dir = filePath;
        File folder = new File(dir);

        if (!folder.exists()) {
            folder.mkdir();
        }

        try {
            FileWriter myWriter = new FileWriter(new File(dir, "csv.txt"));
            myWriter.write(csvText.get());
            myWriter.close();
        } catch (IOException e) {
            logger.log("Filepath for CSV could not be generated");
            logger.log(e.getMessage());
        }

        logger.log("Finished generating CSV");
    }
}
