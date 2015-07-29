package demo.config.web;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import demo.config.diff.ConfigDiffResult;
import demo.config.diff.ConfigDiffType;
import demo.config.model.ConfigurationDiff;
import demo.config.model.ConfigurationDiffHandler;
import demo.config.model.ConfigurationGroupDiff;
import demo.config.service.ConfigurationDiffResultLoader;
import demo.config.validation.Version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	@ResponseBody
	public List<ConfigurationGroupDiff> diffMetadata(@Valid @ModelAttribute DiffRequest diffRequest,
			@RequestParam(defaultValue = "false") String full) {

		ConfigDiffResult result = resultLoader.load(diffRequest.fromVersion, diffRequest.toVersion);
		ConfigurationDiff configurationDiff = handler.handle(result);

		return configurationDiff.getGroups().stream()
				.filter(g -> "true".equals(full) || g.getDiffType() != ConfigDiffType.EQUALS).collect(Collectors.toList());
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
