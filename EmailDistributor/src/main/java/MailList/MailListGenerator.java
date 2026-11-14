package MailList;

import Filter.FilterHelper;
import Logger.MyLogger;
import Main.ConfigReader;
import ScoregDataSctructure.ScoregMemberComplete;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MailListGenerator {

    private final List<FilterHelper.Distributor> selectedMailLists;
    private final MyLogger logger;

    @Getter
    private final Map<FilterHelper.Distributor, MailList> selectedMailListMap;

    public MailListGenerator () {
        selectedMailLists = ConfigReader.getInstance().getSelectedMailLists();
        selectedMailListMap = new HashMap<>();
        selectedMailLists.forEach(distributor -> selectedMailListMap.put(distributor, null));

        logger = new MyLogger("MailListGenerator");
    }

    public void generateAllMailLists(
            List<ScoregMemberComplete> completes
    ) {
        MailSettingCreator csvCreator = new MailSettingCreator();

        selectedMailLists.forEach(listName -> {
            logger.log("Gathering " + listName.name() + " Emails");
            selectedMailListMap.put(listName, new MailList(completes, listName));
        });
        csvCreator.generateJSONFile(selectedMailListMap);
        csvCreator.generateCSVFile(selectedMailListMap.keySet());
    }
}
