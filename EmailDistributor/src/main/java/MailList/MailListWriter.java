package MailList;

import Filter.FilterHelper;
import Logger.MyLogger;
import Main.ConfigReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;

public class MailListWriter {

    private final String filePath = ConfigReader.getInstance().getMailListFilePath();
    private final MyLogger logger = new MyLogger("MailListWriter");

    public void writeMailListToFile (FilterHelper.Distributor distributor, List<String> mailList) {
        String dir = filePath;
        File folder = new File(dir);

        if (!folder.exists()) {
            folder.mkdir();
        }

        try {
            FileWriter myWriter = new FileWriter(new File(dir, distributor.name() + ".txt"));
            mailList.forEach(s -> {
                try {
                    myWriter.write(s + "\n");
                } catch (IOException e) {
                    logger.log(distributor.name() + " can not be written. An Error has occurred");
                    logger.log(e.getMessage());
                }
            });
            myWriter.close();  // must close manually
            logger.log("Successfully wrote to the " + distributor.name() + " Mail List");
        } catch (IOException e) {
            logger.log("Filepath for Mail list could not be generated");
            logger.log(e.getMessage());
        }
    }
}
