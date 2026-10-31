package Main;

import Filter.FilterHelper;
import Logger.MyLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ConfigReader {

    private static ConfigReader instance = null;

    private final List<FilterHelper.Distributor> fallback_selectedMailLists = Arrays.stream(FilterHelper.Distributor.values()).toList();
    @Getter
    private final List<FilterHelper.Distributor> selectedMailLists;

    private final String fallback_EmailDistributorsFolderName = "Verteiler";
    private final String fallback_MailListFilePath = "";
    @Getter
    private final String mailListFilePath;

    private final String fallback_LoggingPath = "Logs";
    @Getter
    private final String loggingPath;

    @Getter
    private final String scoregLoginUsername;
    @Getter
    private final String scoregLoginPassword;
    @Getter
    private final String scoregAccessKey;
    @Getter
    private final String scoregOrgID;

    private JsonNode config;

    private MyLogger logger;

    private ConfigReader() {
        ConfigReader.instance = this;
        ObjectMapper mapper = new ObjectMapper();
        Path appDirectory;

        try {
            appDirectory = getApplicationDirectory();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        Path configDirectory = null;

        try {
            configDirectory = getConfigPath(getApplicationDirectory());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try {
            config = mapper.readTree(configDirectory.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String mailListFilePath_tmp = getProperty("Mail-List Filepath").getFirst();
        mailListFilePath = mailListFilePath_tmp.isBlank()
                                ? appDirectory
                                        .resolve(fallback_MailListFilePath)
                                        .resolve(fallback_EmailDistributorsFolderName)
                                        .toString()
                                : mailListFilePath_tmp;

        String loggingPath_tmp = getProperty("Logging Filepath").getFirst();
        loggingPath = loggingPath_tmp.isBlank()
                            ? appDirectory.resolve(fallback_LoggingPath).toString()
                            : loggingPath_tmp;

        List<FilterHelper.Distributor> selectedMailLists_tmp = getProperty("Selected Mail Lists")
                                                                    .stream()
                                                                    .map(FilterHelper.Distributor::valueOf)
                                                                    .toList();
        selectedMailLists = selectedMailLists_tmp.isEmpty()
                                    ? fallback_selectedMailLists
                                    : selectedMailLists_tmp;

        scoregAccessKey = getProperty("Scoreg Access Key").getFirst();
        scoregLoginUsername = getProperty("Scoreg Login Username").getFirst();
        scoregLoginPassword = getProperty("Scoreg Login Password").getFirst();
        scoregOrgID = getProperty("Scoreg Org ID").getFirst();
        logger = new MyLogger("Config Reader");
        logger.log("Configs Read");
        logger.log("Path to mail lists: " + mailListFilePath);
        logger.log("Logging Path: " + loggingPath);
        logger.log("Selected Mail lists: " + selectedMailLists);

    }

    private static Path getApplicationDirectory() throws URISyntaxException {
        Path codeLocation = Path.of(
                ConfigReader.class
                        .getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI()
        ).toAbsolutePath().normalize();

        if (Files.isDirectory(codeLocation)) {
            Path current = codeLocation;

            for (int i = 0; i < 10 && current != null; i++) {
                if (Files.exists(current.resolve("pom.xml"))) {
                    return current;
                }
                current = current.getParent();
            }
            return codeLocation;
        }

        Path jarDirectory = codeLocation.getParent();

        if (jarDirectory != null
                && Files.isDirectory(jarDirectory.resolveSibling("runtime"))) {
            return jarDirectory.getParent();
        }

        return jarDirectory;
    }

    private static Path getConfigPath(Path appPath) throws URISyntaxException {
        return appPath
                .resolve("config.json");
    }

    public static ConfigReader getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public List<String> getProperty(String key) {
        JsonNode retValue = config.path(key);
        if(retValue.isArray()){
            Iterator<JsonNode> elements = retValue.elements();
            List<String> list = new ArrayList<>();
            while (elements.hasNext()){
                list.add(elements.next().asText());
            }
            return list;
        } else {
            return List.of(retValue.asText());
        }
    }

}
