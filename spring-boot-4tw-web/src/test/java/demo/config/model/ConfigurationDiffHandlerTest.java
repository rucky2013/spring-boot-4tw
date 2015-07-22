package demo.config.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import demo.config.diff.ConfigDiffGenerator;
import demo.config.diff.ConfigDiffResult;
import demo.config.diff.support.ConfigurationMetadataRepositoryLoader;
import org.junit.Test;

import org.springframework.boot.configurationmetadata.ConfigurationMetadataRepository;
import org.springframework.boot.configurationmetadata.ConfigurationMetadataRepositoryJsonBuilder;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

/**
 *
 * @author Stephane Nicoll
 */
public class ConfigurationDiffHandlerTest {

	@Test
	public void stupidTest() throws IOException {
		ConfigDiffResult original = generateDiff("foo-first", "foo-second");
		ConfigurationDiff diff = new ConfigurationDiffHandler().handle(original);
		List<ConfigurationGroupDiff> groups = diff.getGroups();
		assertEquals(3, groups.size()); // Automatic sorting
		assertEquals("server", groups.get(0).getId());
		assertEquals("server.undertow", groups.get(2).getId());
	}

	protected static ConfigDiffResult generateDiff(String left, String right) throws IOException {
		ConfigurationMetadataRepository leftRepo = loadRepository(left);
		ConfigurationMetadataRepository rightRepo = loadRepository(right);
		ConfigurationMetadataRepositoryLoader resolver = mock(ConfigurationMetadataRepositoryLoader.class);
		given(resolver.load(left)).willReturn(leftRepo);
		given(resolver.load(right)).willReturn(rightRepo);
		return new ConfigDiffGenerator(resolver).generateDiff(left, right);
	}

	private static ConfigurationMetadataRepository loadRepository(String name) throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("meta-data/" + name + ".json");
		try (InputStream in = classPathResource.getInputStream()) {
			return ConfigurationMetadataRepositoryJsonBuilder.create().withJsonResource(in).build();
		}
	}

}