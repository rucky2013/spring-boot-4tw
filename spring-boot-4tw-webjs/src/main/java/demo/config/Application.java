package demo.config;


import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import org.springframework.boot.SpringApplication;
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
		// Workaround for https://github.com/ehcache/ehcache3/issues/387
		System.setProperty("org.ehcache.statisticsExecutor.poolSize", "1");
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public HealthIndicator springIoHealthIndicator() {
		return () -> {
			ResponseEntity<Object> entity = new RestTemplate()
					.getForEntity("https://spring.io/project_metadata/spring-boot.json", Object.class);
			return Health.up().withDetail("httpStatus", entity.getStatusCode()).build();
		};
	}

	@Bean
	public JCacheManagerCustomizer cacheManagerCustomizer() {
		return cacheManager -> {
			MutableConfiguration<?, ?> config = new MutableConfiguration<>();
			config.setExpiryPolicyFactory(CreatedExpiryPolicy
					.factoryOf(Duration.ONE_HOUR));
			config.setStatisticsEnabled(true);
			cacheManager.createCache("diffs", config);
			cacheManager.createCache("bootversions", config);

		};
	}

}