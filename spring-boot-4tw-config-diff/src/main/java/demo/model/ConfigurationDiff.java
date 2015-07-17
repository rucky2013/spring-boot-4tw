package demo.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Stephane Nicoll
 */
public class ConfigurationDiff {

	private final Map<String, ConfigurationGroupDiff> groups = new LinkedHashMap<>();

	public Map<String, ConfigurationGroupDiff> getGroups() {
		return groups;
	}

}
