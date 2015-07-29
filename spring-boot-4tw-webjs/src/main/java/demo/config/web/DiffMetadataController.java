package demo.config.web;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import demo.config.diff.ConfigDiffResult;
import demo.config.diff.ConfigDiffType;
import demo.config.diff.support.UnknownSpringBootVersion;
import demo.config.model.ConfigurationDiff;
import demo.config.model.ConfigurationDiffHandler;
import demo.config.model.ConfigurationGroupDiff;
import demo.config.service.ConfigurationDiffResultLoader;
import demo.config.validation.Version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DiffMetadataController {

	private final ConfigurationDiffResultLoader resultLoader;

	private final ConfigurationDiffHandler handler;

	@Autowired
	public DiffMetadataController(ConfigurationDiffResultLoader resultLoader) {
		this.resultLoader = resultLoader;
		this.handler = new ConfigurationDiffHandler();
	}

	@RequestMapping("/diff/{fromVersion}/{toVersion}/")
	public ResponseEntity<List<ConfigurationGroupDiff>> diffMetadata(@Valid @ModelAttribute DiffRequest diffRequest,
			@RequestParam(defaultValue = "false") String full) throws BindException {

		ConfigDiffResult result = loadDiff(diffRequest);
		ConfigurationDiff configurationDiff = handler.handle(result);


		List<ConfigurationGroupDiff> content = configurationDiff.getGroups().stream()
				.filter(g -> "true".equals(full) || g.getDiffType() != ConfigDiffType.EQUALS).collect(Collectors.toList());
		return ResponseEntity.ok().eTag(createDiffETag(configurationDiff)).body(content);
	}

	private ConfigDiffResult loadDiff(DiffRequest diffRequest) throws BindException {
		try {
			return resultLoader.load(diffRequest.fromVersion, diffRequest.toVersion);
		}
		catch (UnknownSpringBootVersion ex) {
			BindException bindException = new BindException(diffRequest, "diffRequest");
			String invalidVersion = ex.getVersion();
			if (invalidVersion.equals(diffRequest.fromVersion)) {
				bindException.rejectValue("fromVersion", "{Version.unknown}");
			}
			else {
				bindException.rejectValue("toVersion", "{Version.unknown}");
			}
			throw bindException;
		}
	}

	private String createDiffETag(ConfigurationDiff diff) {
		return "\"" + diff.getLeftVersion() + "#" + diff.getRightVersion() + "\"";
	}


	static class DiffRequest {


		@Version
		private String fromVersion;

		@Version
		private String toVersion;

		public String getFromVersion() {
			return fromVersion;
		}

		public void setFromVersion(String fromVersion) {
			this.fromVersion = fromVersion;
		}

		public String getToVersion() {
			return toVersion;
		}

		public void setToVersion(String toVersion) {
			this.toVersion = toVersion;
		}
	}

}
