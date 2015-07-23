package demo.config.service;

import java.io.IOException;

import demo.config.model.ConfigurationDiff;
import demo.config.test.ConfigDiffResultTestLoader;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Stephane Nicoll
 */
public class DiffMetadataServiceTest {

	@Test
	public void simpleMetadataDiff() throws IOException {
		DiffMetadataService service = new DiffMetadataService(new ConfigurationDiffResultLoader(
				ConfigDiffResultTestLoader.mockConfigDiffGenerator("1.0.1.RELEASE", "1.1.0.RELEASE")));
		ConfigurationDiff configurationDiff = service.metadataDiff("1.0.1.RELEASE", "1.1.0.RELEASE");
		assertNotNull(configurationDiff);
		assertEquals("1.0.1.RELEASE", configurationDiff.getLeftVersion());
		assertEquals("1.1.0.RELEASE", configurationDiff.getRightVersion());
		assertEquals(3, configurationDiff.getGroups().size());
	}

}
