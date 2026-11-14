package MailList;

import Filter.FilterHelper;
import Logger.MyLogger;
import Main.ConfigReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MailSettingCreator {

    private final String filePath = ConfigReader.getInstance().getMailListFilePath();
    private final MyLogger logger = new MyLogger("MailListJSONWriter");

    protected void generateJSONFile(Map<FilterHelper.Distributor, MailList> emailMap) {
        logger.log("Started generating JSON");

        String dir = filePath;
        File folder = new File(dir);

        if (!folder.exists()) {
            folder.mkdir();
        }

        File file = new File(dir, "configVerteiler.json");

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            Map<String, Object> rootMap = new HashMap<>();
            Map<String, Object> listsMap = new HashMap<>();

            for (FilterHelper.Distributor key  : emailMap.keySet()) {
                listsMap.put(key.name(), emailMap.get(key).toJsonObject());
            }

            rootMap.put("lists", listsMap);

            mapper.writeValue(file, rootMap);
            logger.log("Successfully generated configSettings.json!");

        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.log("Finished generating JSON");
    }

    protected void generateCSVFile(Set<FilterHelper.Distributor> selectedDistributors) {
        logger.log("Started generating CSV");

        StringBuilder csvText = new StringBuilder();

        for (FilterHelper.Distributor distributor: selectedDistributors) {
            csvText.append(distributor.name()).append("; Sendet emails an webmaster zur verteilung; webmaster@pfadfindergruppewien71.a\n");
        }

        String dir = filePath;
        File folder = new File(dir);

        if (!folder.exists()) {
            folder.mkdir();
        }

        try {
            FileWriter myWriter = new FileWriter(new File(dir, "forwarding.csv"));
            myWriter.write(csvText.toString());
            myWriter.close();
        } catch (IOException e) {
            logger.log("Filepath for CSV could not be generated");
            logger.log(e.getMessage());
        }

        logger.log("Finished generating CSV");
    }
}
