package demo.config.service;

public class VersionMismatchException extends RuntimeException {

	public VersionMismatchException(String previousVersion, String nextVersion) {
		super(previousVersion + " is not prior to " + nextVersion);
	}
}
