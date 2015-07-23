package demo.config.service;

import demo.config.diff.ConfigDiffResult;
import demo.config.model.ConfigurationDiff;
import demo.config.model.ConfigurationDiffHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiffMetadataService {

	private final ConfigurationDiffResultLoader diffResultLoader;

	private ConfigurationDiffHandler diffHandler;

	@Autowired
	public DiffMetadataService(ConfigurationDiffResultLoader diffResultLoader) {
		this.diffResultLoader = diffResultLoader;
		this.diffHandler = new ConfigurationDiffHandler();
	}

	public ConfigurationDiff metadataDiff(String previousVersion, String nextVersion) {
		ConfigDiffResult result = this.diffResultLoader.load(previousVersion, nextVersion);
		return diffHandler.handle(result);
	}

}
