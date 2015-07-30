package demo.config.springboot;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Stephane Nicoll
 */
public class SpringBootVersionServiceTest {

	@Test
	public void fetchBootVersions() throws IOException {
		RestTemplate restTemplate = mock(RestTemplate.class);

		when(restTemplate.getForObject(SpringBootVersionService.BOOT_VERSION_ENDPOINT, String.class))
				.thenReturn(loadMetadata());
		SpringBootVersionService service = new SpringBootVersionService(restTemplate);
		List<String> versions = service.fetchBootVersions();
		assertEquals(5, versions.size());
		assertEquals("1.3.0.M2", versions.get(0));
		assertEquals("1.3.0.BUILD-SNAPSHOT", versions.get(1));
		assertEquals("1.2.6.BUILD-SNAPSHOT", versions.get(2));
		assertEquals("1.2.5.RELEASE", versions.get(3));
		assertEquals("1.1.12.RELEASE", versions.get(4));

	}

	private String loadMetadata() throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("meta-data/spring-boot.json");
		return StreamUtils.copyToString(classPathResource.getInputStream(), Charset.forName("UTF-8"));
	}

}
