package demo.config.springboot;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class SpringBootVersionService {

    private final static String BOOT_VERSION_ENDPOINT = "https://spring.io/project_metadata/spring-boot.json";

    private final RestTemplate restTemplate;

    public SpringBootVersionService() {
        this.restTemplate = new RestTemplate();
    }

    public List<String> fetchBootVersions() {

        Map<String, String> response = restTemplate.getForObject(BOOT_VERSION_ENDPOINT, Map.class);

        return Collections.emptyList();
    }
}
