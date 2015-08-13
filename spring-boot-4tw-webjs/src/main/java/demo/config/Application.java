package demo.config;

import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import demo.config.springboot.SpringBootVersionService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCaching
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public JCacheManagerCustomizer cacheManagerCustomizer() {
		return cm -> {
			cm.createCache("diffs", new MutableConfiguration<>()
					.setExpiryPolicyFactory(CreatedExpiryPolicy
							.factoryOf(Duration.ONE_HOUR))
					.setStatisticsEnabled(true));
			cm.createCache("boot-versions", new MutableConfiguration<>()
					.setStatisticsEnabled(true));
		};
	}

	@Bean
	public HealthIndicator releaseRepositoryHealthIndicator(SpringBootVersionService springBootVersionService) {
		return new AbstractHealthIndicator() {
			@Override
			protected void doHealthCheck(Health.Builder builder) throws Exception {
				RestTemplate restTemplate = new RestTemplate();
				for (String url : springBootVersionService.getRepositoryUrls()) {
					ResponseEntity<String> entity = restTemplate
							.getForEntity(url, String.class);
					builder.up().withDetail(url, entity.getStatusCode());
				}
			}
		};
	}

}