package demo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCaching
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public HealthIndicator springIoHealthIndicator() {
		return () -> {
			ResponseEntity<Object> entity = new RestTemplate()
					.getForEntity("http://start.spring.io", Object.class);
			return Health.up().withDetail("httpStatus", entity.getStatusCode()).build();
		};
	}

	@Bean
	public EmbeddedServletContainerCustomizer servletContainerCustomizer() {
		return container -> {
			container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/400.html"));
		};
	}

}