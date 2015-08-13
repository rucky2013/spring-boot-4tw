package demo.config.web;

import demo.config.diff.ConfigDiffResult;
import demo.config.diff.support.UnknownSpringBootVersion;
import demo.config.model.ConfigurationDiff;
import demo.config.model.ConfigurationDiffHandler;
import demo.config.service.ConfigurationDiffResultLoader;
import demo.config.springboot.SpringBootVersionService;
import demo.config.validation.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
public class DiffMetadataController {

	private final ConfigurationDiffResultLoader resultLoader;

	private final ConfigurationDiffHandler handler;

	private final SpringBootVersionService bootVersionService;

	@Autowired
	public DiffMetadataController(ConfigurationDiffResultLoader resultLoader,
	                              SpringBootVersionService bootVersionService) {
		this.resultLoader = resultLoader;
		this.bootVersionService = bootVersionService;
		this.handler = new ConfigurationDiffHandler();
	}

	@RequestMapping("/diff/{fromVersion}/{toVersion}/")
	public ResponseEntity<ConfigurationDiff> diffMetadata(
			@Valid @ModelAttribute DiffRequest diffRequest) throws BindException {

		ConfigDiffResult result = loadDiff(diffRequest);
		ConfigurationDiff configurationDiff = handler.handle(result);

		return ResponseEntity.ok().eTag(createDiffETag(configurationDiff)).body(configurationDiff);
	}

	@RequestMapping("/springboot/versions")
	@ResponseBody
	public List<String> fetchBootVersions() {
		return bootVersionService.fetchBootVersions();
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
