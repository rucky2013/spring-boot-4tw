package demo.config.springboot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpringBootVersionService {

	static final String BOOT_VERSION_ENDPOINT = "https://spring.io/project_metadata/spring-boot.json";

	private final RestTemplate restTemplate;

	protected SpringBootVersionService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public SpringBootVersionService() {
		this(new RestTemplate());
	}

	public List<String> fetchBootVersions() {
		String content = restTemplate.getForObject(BOOT_VERSION_ENDPOINT, String.class);
		JSONObject json = new JSONObject(content);
		List<String> versions = new ArrayList<>();
		if (json.has("projectReleases")) {
			JSONArray projectReleases = json.getJSONArray("projectReleases");
			for (int i = 0; i < projectReleases.length(); i++) {
				JSONObject projectRelease = projectReleases.getJSONObject(i);
				if (projectRelease.has("version")) {
					versions.add(projectRelease.getString("version"));
				}
			}
		}
		return versions;
	}
}
