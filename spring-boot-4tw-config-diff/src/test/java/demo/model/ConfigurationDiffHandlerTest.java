package demo.model;

import java.io.IOException;
import java.util.List;

import demo.config.diff.ConfigDiffGeneratorTest;
import demo.config.diff.ConfigDiffResult;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Stephane Nicoll
 */
public class ConfigurationDiffHandlerTest {

	@Test
	public void stupidTest() throws IOException {
		ConfigDiffResult original = ConfigDiffGeneratorTest.generateDiff("foo-first", "foo-second"); // Ooops
		ConfigurationDiff diff = new ConfigurationDiffHandler().handle(original);
		List<ConfigurationGroupDiff> groups = diff.getGroups();
		assertEquals(8, groups.size()); // Automatic sorting
		assertEquals("server", groups.get(0).getId());
		assertEquals("server.undertow", groups.get(7).getId());
	}

}
