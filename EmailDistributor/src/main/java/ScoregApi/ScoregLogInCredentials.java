package ScoregApi;

import Main.ConfigReader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ScoregLogInCredentials {

    public final String username;
    public final String password;
    public final String orgId;
    public final String accessKey;

    public ScoregLogInCredentials () {
        ConfigReader configs = ConfigReader.getInstance();
        username = configs.getScoregLoginUsername();
        password = configs.getScoregLoginPassword();
        orgId = configs.getScoregOrgID();
        accessKey = configs.getScoregAccessKey();
    }

    public String getUrl(String service, String method, String... parameter) {
        String host = StringUtils.defaultIfBlank(System.getProperty("wshost"), "https://www.scoreg.at/");

        String url = host + "ScoregWebServer/services/v1/" + service + "/" + method + "/";

        if (parameter.length > 0) {
            url += org.springframework.util.StringUtils.arrayToDelimitedString(parameter, "/");
        }

        return url;
    }

    public HttpHeaders createHeaders() {
        return new HttpHeaders() {
            private static final long serialVersionUID = -7754140196572841464L;
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
                set("accessKey", accessKey);
            }
        };
    }
}
