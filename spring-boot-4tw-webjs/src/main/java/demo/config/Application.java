package demo.config;

import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.metrics.jmx.JmxMetricWriter;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.jmx.export.MBeanExporter;

@SpringBootApplication
@EnableCaching
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public JCacheManagerCustomizer cacheManagerCustomizer() {
		return cm -> {
			MutableConfiguration<Object, Object> configuration = new MutableConfiguration<>()
					.setExpiryPolicyFactory(CreatedExpiryPolicy
							.factoryOf(Duration.ONE_HOUR))
					.setStoreByValue(false)
					.setStatisticsEnabled(true);
			cm.createCache("diffs", configuration);
			cm.createCache("boot-versions", configuration);
		};
	}

	@Bean
	@ExportMetricWriter
	MetricWriter metricWriter(MBeanExporter exporter) {
		return new JmxMetricWriter(exporter);
	}

}