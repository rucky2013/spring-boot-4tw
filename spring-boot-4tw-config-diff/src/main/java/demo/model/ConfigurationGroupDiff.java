package demo.model;

import demo.config.diff.ConfigDiffType;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Stephane Nicoll
 */
public class ConfigurationGroupDiff {

	private String id;

	private ConfigDiffType diffType;

	private final List<ConfigurationPropertyDiff> properties = new LinkedList<>();

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

	public List<ConfigurationPropertyDiff> getProperties() {
		return properties;
	}

}
