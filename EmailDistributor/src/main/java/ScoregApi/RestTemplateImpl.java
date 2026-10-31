package ScoregApi;

import org.springframework.web.client.RestTemplate;

import java.util.Objects;

public class RestTemplateImpl extends RestTemplate {

    private static RestTemplateImpl instance = null;

    public static RestTemplateImpl get_instance() {
        if (Objects.isNull(instance)) {
            RestTemplateImpl.instance = new RestTemplateImpl();
        }
        return instance;
    }
}
