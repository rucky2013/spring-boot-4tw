package demo.config.diffview;

import demo.config.diff.ConfigDiffType;

import java.util.LinkedList;
import java.util.List;

public class GroupDiff {

    private String id;

    private String anchorName;

    private String previousVersion;

    private String nextVersion;

    private ConfigDiffType diffType;

    private final List<PropertyDiff> properties = new LinkedList<>();

    public GroupDiff(String id, String previousVersion, String nextVersion, ConfigDiffType diffType) {
        this.id = id;
        this.anchorName = this.id.replaceAll("\\.", "-");
        this.previousVersion = previousVersion;
        this.nextVersion = nextVersion;
        this.diffType = diffType;
    }

    public ConfigDiffType getDiffType() {
        return diffType;
    }

    public void addProperty(PropertyDiff propertyDiff) {
        this.properties.add(propertyDiff);
    }
}
