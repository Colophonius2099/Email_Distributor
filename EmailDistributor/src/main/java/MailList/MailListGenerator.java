package MailList;

import Filter.FilterHelper;
import Logger.MyLogger;
import Main.ConfigReader;
import ScoregDataSctructure.ScoregMemberComplete;

import java.util.ArrayList;
import java.util.List;

public class MailListGenerator {

    private final List<FilterHelper.Distributor> selectedMailLists;
    private final MailListWriter mailListWriter;
    private final MyLogger logger;

    public MailListGenerator () {
        selectedMailLists = ConfigReader.getInstance().getSelectedMailLists();
        mailListWriter = new MailListWriter();
        logger = new MyLogger("MailListGenerator");
    }

    public void generateAllMailLists(
            List<ScoregMemberComplete> completes,
            FilterHelper filterHelper
    ) {
        selectedMailLists.forEach(listName -> {
            logger.log("Gathering " + listName.name() + " Emails");
            List<ScoregMemberComplete> filtered = filterHelper.filterList(completes, listName);
            List list = new ArrayList();
            filtered.forEach(s -> list.addAll(s.getRelevantEmails()));
            mailListWriter.writeMailListToFile(listName, list);
        });
    }
}
