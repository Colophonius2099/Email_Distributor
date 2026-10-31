package Main;

import Logger.MyLogger;
import MailList.MailListGenerator;
import Filter.FilterHelper;
import ScoregApi.ScoregDataLoader;
import ScoregApi.ScoregLogInCredentials;
import ScoregDataSctructure.ScoregMemberComplete;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyLogger logger = MyLogger.GET_GENERIC_LOGGER();
        logger.log("Starting Program");

        logger.log("Starting Initialisation");

        ConfigReader configReader = ConfigReader.getInstance();
        ScoregLogInCredentials credentials = new ScoregLogInCredentials();
        ScoregDataLoader loader = new ScoregDataLoader(credentials);
        FilterHelper filterHelper = new FilterHelper();
        MailListGenerator generator = new MailListGenerator();

        logger.log("Finished Initialisation");


        List<FutureTask<ScoregMemberComplete>> taskList = loader.generateWorkflow();
        List<ScoregMemberComplete> completes = loader.endWorkflow(taskList);

        generator.generateAllMailLists(completes, filterHelper);
        logger.log("Finished Workflow - all selected Mail Lists were generated");
    }
}