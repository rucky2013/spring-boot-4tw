package demo.config.model;

import java.io.IOException;
import java.util.List;

import demo.config.diff.ConfigDiffResult;
import org.junit.Test;

import static demo.config.test.ConfigDiffResultTestLoader.*;
import static org.junit.Assert.*;

/**
 *
 * @author Stephane Nicoll
 */
public class ConfigurationDiffHandlerTest {

	@Test
	public void stupidTest() throws IOException {
		ConfigDiffResult original = generateDiff("1.0.1.RELEASE", "1.1.0.RELEASE");
		ConfigurationDiff diff = new ConfigurationDiffHandler().handle(original);
		List<ConfigurationGroupDiff> groups = diff.getGroups();
		assertEquals(3, groups.size()); // Automatic sorting
		assertEquals("server", groups.get(0).getId());
		assertEquals("server.undertow", groups.get(2).getId());
	}

}