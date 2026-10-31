package ScoregApi;

import ScoregDataSctructure.ScoregMemberComplete;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.concurrent.Callable;

public class ScoregMemberDataCaller implements Callable<ScoregMemberComplete>{

    private final String requestedScoutId;
    private final ScoregLogInCredentials credentials;

    public ScoregMemberDataCaller(String requestedScoutId, ScoregLogInCredentials credentials) {
        this.requestedScoutId = requestedScoutId;
        this.credentials = credentials;
    }
    

    @Override
    public ScoregMemberComplete call() {
        String url = credentials.getUrl("memberV2", "findMemberCompleteByScoutId", requestedScoutId);
        return RestTemplateImpl.get_instance().exchange(url, HttpMethod.GET,
                new HttpEntity<Void>(credentials.createHeaders()), ScoregMemberComplete.class).getBody();
    }
}
