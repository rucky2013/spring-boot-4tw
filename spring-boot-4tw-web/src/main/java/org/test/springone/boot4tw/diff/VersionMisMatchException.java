package org.test.springone.boot4tw.diff;

public class VersionMisMatchException extends RuntimeException {

    public VersionMisMatchException(String previousVersion, String nextVersion) {
        super(previousVersion + " is not prior to " + nextVersion);
    }
}
