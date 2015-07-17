package demo.model;

import java.io.IOException;
import java.util.Map;

import demo.config.diff.ConfigDiffGeneratorTest;
import demo.config.diff.ConfigDiffResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Stephane Nicoll
 */
public class ConfigurationDiffHandlerTest {

	@Test
	public void stupidTest() throws IOException {
		ConfigDiffResult original = ConfigDiffGeneratorTest.generateDiff("foo-first", "foo-second"); // Ooops
		ConfigurationDiff diff = new ConfigurationDiffHandler().handle(original);
		Map<String, ConfigurationGroupDiff> groups = diff.getGroups();
		assertEquals(8, groups.size());
	}

}
