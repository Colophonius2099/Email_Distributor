package ScoregApi;

import Logger.MyLogger;
import ScoregDataSctructure.ScoregMemberComplete;
import ScoregDataSctructure.ScoregScoutIdList;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


public class ScoregDataLoader {

    private ScoregScoutIdList scoregScoutIdList;
    private final ExecutorService scoregExecutor;
    private final MyLogger logger;
    private final ScoregLogInCredentials credentials;

    public ScoregDataLoader(ScoregLogInCredentials credentials) {
        scoregExecutor = Executors.newFixedThreadPool(50);
        logger = new MyLogger("ScoregDataLoader");
        this.credentials = credentials;
    }

    private void loadScoutIDs() {
        logger.log("");

        String url = credentials.getUrl("memberV2", "findScoutIdsForOrganization");

        RestTemplate restTemplate = RestTemplateImpl.get_instance();
        scoregScoutIdList = restTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<Void>(credentials.createHeaders()), ScoregScoutIdList.class).getBody();
    }

    private List<FutureTask<ScoregMemberComplete>> loadMemberData() {
        List<FutureTask<ScoregMemberComplete>> taskList = new ArrayList<>();
        for (String requestedScoutId : scoregScoutIdList.getList()) {
            FutureTask<ScoregMemberComplete> task =
                    new FutureTask<ScoregMemberComplete>(new ScoregMemberDataCaller(requestedScoutId, credentials));
            taskList.add(task);
        }

        return taskList;
    }

    public List<FutureTask<ScoregMemberComplete>> generateWorkflow() {
        loadScoutIDs();
        List<FutureTask<ScoregMemberComplete>> list = loadMemberData();
        executeTasks(list);
        return list;
    }

    public void executeTasks(List<FutureTask<ScoregMemberComplete>> taskList) {
        taskList.forEach(scoregExecutor::submit);
    }

    public List<ScoregMemberComplete> endWorkflow(List<FutureTask<ScoregMemberComplete>> taskList) {
        scoregExecutor.close();
        List<ScoregMemberComplete> completes = new ArrayList<>();
        taskList.forEach(s -> {
            try {
                completes.add(s.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        return completes;
    }
}

