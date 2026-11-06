package MailList;

import Filter.FilterHelper;
import Logger.MyLogger;
import Main.ConfigReader;
import ScoregDataSctructure.ScoregMemberComplete;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MailListGenerator {

    private final List<FilterHelper.Distributor> selectedMailLists;
    private final MailListWriter mailListWriter;
    private final MyLogger logger;

    @Getter
    private final Map<FilterHelper.Distributor, List<String>> selectedMailListMap;

    public MailListGenerator () {
        selectedMailLists = ConfigReader.getInstance().getSelectedMailLists();
        mailListWriter = new MailListWriter();
        selectedMailListMap = new HashMap<>();
        selectedMailLists.forEach(distributor -> selectedMailListMap.put(distributor, new ArrayList<>()));

        logger = new MyLogger("MailListGenerator");
    }

    public void generateAllMailLists(
            List<ScoregMemberComplete> completes,
            FilterHelper filterHelper
    ) {
        MailListCSVCreator csvCreator = new MailListCSVCreator();

        selectedMailLists.forEach(listName -> {
            logger.log("Gathering " + listName.name() + " Emails");
            List<ScoregMemberComplete> filtered = filterHelper.filterList(completes, listName);
            List<String> mailList = new ArrayList();
            filtered.forEach(s -> mailList.addAll(s.getRelevantEmails()));
            mailListWriter.writeMailListToFile(listName, mailList);
            selectedMailListMap.put(listName, mailList);
        });
        csvCreator.generateCSVFile(selectedMailListMap);
    }
}
