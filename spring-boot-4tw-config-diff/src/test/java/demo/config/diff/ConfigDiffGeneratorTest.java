package demo.config.diff;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import org.springframework.boot.configurationmetadata.ConfigurationMetadataRepository;
import org.springframework.boot.configurationmetadata.ConfigurationMetadataRepositoryJsonBuilder;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.*;

/**
 *
 * @author Stephane Nicoll
 */
public class ConfigDiffGeneratorTest {

	@Test
	public void groupDiff() throws IOException {
		ConfigDiffResult diff = generateDiff("foo-first", "foo-second");

		List<ConfigGroupDiff> groupsDeleted = diff.getGroupsDiffFor(ConfigDiffType.DELETE);
		assertEquals(1, groupsDeleted.size());
		ConfigGroupDiff sslGroup = groupsDeleted.get(0);
		assertEquals("server.ssl", sslGroup.getId());
		assertNotNull(sslGroup.getLeft());
		assertNull(sslGroup.getRight());

		List<ConfigGroupDiff> groupsAdded = diff.getGroupsDiffFor(ConfigDiffType.ADD);
		assertEquals(1, groupsAdded.size());
		ConfigGroupDiff compressionGroup = groupsAdded.get(0);
		assertEquals("server.compression", compressionGroup.getId());
		assertNull(compressionGroup.getLeft());
		assertNotNull(compressionGroup.getRight());

		List<ConfigGroupDiff> groupsModified = diff.getGroupsDiffFor(ConfigDiffType.MODIFY);
		assertEquals(1, groupsModified.size());
		ConfigGroupDiff undertowGroup = groupsModified.get(0);
		assertEquals("server.undertow", undertowGroup.getId());
		assertNotNull(undertowGroup.getLeft());
		assertNotNull(undertowGroup.getRight());

		assertEquals(6, diff.getGroupsDiffFor(ConfigDiffType.EQUALS).size());
	}

	@Test
	public void propertyDiff() throws IOException {
		ConfigDiffResult diff = generateDiff("foo-first", "foo-second");
		List<ConfigPropertyDiff> propertiesDiffFor = diff.getGroupsDiffFor(ConfigDiffType.MODIFY)
				.get(0).getPropertiesDiffFor(ConfigDiffType.ADD);
		ConfigPropertyDiff accessLogProperty = propertiesDiffFor.get(0);
		assertEquals("server.undertow.access-log-enabled", accessLogProperty.getId());
		assertNull(accessLogProperty.getLeft());
		assertNotNull(accessLogProperty.getRight());
	}


	protected ConfigDiffResult generateDiff(String left, String right) throws IOException {
		ConfigurationMetadataRepository leftRepo = loadRepository(left);
		ConfigurationMetadataRepository rightRepo = loadRepository(right);
		return new ConfigDiffGenerator(null).processDiff(new ConfigDiffResult(left, right), leftRepo, rightRepo);
	}

	protected ConfigurationMetadataRepository loadRepository(String name) throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("meta-data/" + name + ".json");
		try (InputStream in = classPathResource.getInputStream()) {
			return ConfigurationMetadataRepositoryJsonBuilder.create().withJsonResource(in).build();
		}
	}
}
