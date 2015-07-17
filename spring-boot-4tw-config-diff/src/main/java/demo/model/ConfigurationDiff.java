package demo.model;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Stephane Nicoll
 */
public class ConfigurationDiff {

	private final List<ConfigurationGroupDiff> groups = new LinkedList<>();

	public List<ConfigurationGroupDiff> getGroups() {
		return groups;
	}

}
