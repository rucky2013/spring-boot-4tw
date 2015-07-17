package demo.config.diff;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.configurationmetadata.ConfigurationMetadataGroup;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author Stephane Nicoll
 */
public class ConfigGroupDiff {

	private final String id;

	private final ConfigurationMetadataGroup left;

	private final ConfigurationMetadataGroup right;

	private final MultiValueMap<ConfigDiffType, ConfigPropertyDiff> properties;

	public ConfigGroupDiff(String id, ConfigurationMetadataGroup left, ConfigurationMetadataGroup right) {
		this.id = id;
		this.left = left;
		this.right = right;
		this.properties = new LinkedMultiValueMap<ConfigDiffType, ConfigPropertyDiff>();
	}

	public String getId() {
		return id;
	}

	public ConfigurationMetadataGroup getLeft() {
		return left;
	}

	public ConfigurationMetadataGroup getRight() {
		return right;
	}

	public List<ConfigPropertyDiff> getPropertiesDiffFor(ConfigDiffType type) {
		List<ConfigPropertyDiff> content = this.properties.get(type);
		if (content == null) {
			return Collections.emptyList();
		}
		return content;
	}

	void register(ConfigDiffType diffType, ConfigPropertyDiff propertyDiff) {
		this.properties.add(diffType, propertyDiff);
	}

}
