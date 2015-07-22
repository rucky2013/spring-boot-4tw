package demo.config.service;

import java.io.IOException;

import demo.config.diff.ConfigDiffGenerator;
import demo.config.diff.ConfigDiffResult;
import demo.config.diff.support.AetherDependencyResolver;
import demo.model.ConfigurationDiff;
import demo.model.ConfigurationDiffHandler;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class DiffMetadataService {

	private final ConfigDiffGenerator diffGenerator;

	private final ConfigurationDiffHandler diffHandler;

	private final VersionStringComparator versionComparator;

	public DiffMetadataService() throws Exception {
		this(new ConfigDiffGenerator(AetherDependencyResolver.withAllRepositories()),
				new ConfigurationDiffHandler(),
				new VersionStringComparator());
	}

	public DiffMetadataService(ConfigDiffGenerator diffGenerator, ConfigurationDiffHandler diffHandler,
			VersionStringComparator versionComparator) {
		this.diffGenerator = diffGenerator;
		this.diffHandler = diffHandler;
		this.versionComparator = versionComparator;
	}

	public ConfigurationDiff metadataDiff(String previousVersion, String nextVersion) {
		try {
			Assert.hasText(previousVersion);
			Assert.hasText(nextVersion);
			if (versionComparator.compare(previousVersion, nextVersion) >= 0) {
				throw new VersionMisMatchException(previousVersion, nextVersion);
			}
			ConfigDiffResult result = diffGenerator.generateDiff(previousVersion, nextVersion);
			return diffHandler.handle(result);
		}
		catch (IOException ex) {
			throw new RepositoryNotReachableException(ex);
		}
	}

}
