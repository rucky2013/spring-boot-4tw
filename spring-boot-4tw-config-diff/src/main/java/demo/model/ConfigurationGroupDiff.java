package demo.model;

import java.util.LinkedHashMap;
import java.util.Map;

import demo.config.diff.ConfigDiffType;

/**
 *
 * @author Stephane Nicoll
 */
public class ConfigurationGroupDiff {

	private String id;

	private ConfigDiffType diffType;

	private final Map<String, ConfigurationPropertyDiff> properties = new LinkedHashMap<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ConfigDiffType getDiffType() {
		return diffType;
	}

	public void setDiffType(ConfigDiffType diffType) {
		this.diffType = diffType;
	}

	public Map<String, ConfigurationPropertyDiff> getProperties() {
		return properties;
	}

}
