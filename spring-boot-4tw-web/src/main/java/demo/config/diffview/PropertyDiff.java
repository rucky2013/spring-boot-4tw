package demo.config.diffview;

import demo.config.diff.ConfigDiffType;

public class PropertyDiff {

	public static Property EMPTY_PROPERTY = new Property("", "", "", "");

	private ConfigDiffType diffType;

	private String cssClass;

	private Property previous;

	private Property next;

	public PropertyDiff(ConfigDiffType diffType, Property previous, Property next) {
		this.diffType = diffType;
		this.previous = previous != null ? previous : EMPTY_PROPERTY;
		this.next = next != null ? next : EMPTY_PROPERTY;
		switch (diffType.toString()) {
			case "ADD":
				this.cssClass = "success";
				break;
			case "DELETE":
				this.cssClass = "danger";
				break;
			default:
				this.cssClass = "";
		}
	}
}
