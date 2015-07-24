package demo.config.web;

import demo.config.diff.ConfigDiffResult;
import demo.config.diff.ConfigDiffType;
import demo.config.diffview.ConfigDiff;
import demo.config.diffview.DiffViewConverter;
import demo.config.diffview.GroupDiff;
import demo.config.service.ConfigurationDiffResultLoader;
import demo.config.springboot.SpringBootVersionService;
import demo.config.validation.Version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DiffMetadataController {

	private final ConfigurationDiffResultLoader resultLoader;

	private final DiffViewConverter converter;

	private final SpringBootVersionService versionService;

	@Autowired
	public DiffMetadataController(ConfigurationDiffResultLoader resultLoader,
	                              DiffViewConverter converter, SpringBootVersionService versionService) {
		this.resultLoader = resultLoader;
		this.converter = converter;
		this.versionService = versionService;
	}

	@RequestMapping("/")
	public String diffMetadata(@Valid @ModelAttribute DiffRequest diffRequest,
			@RequestParam(defaultValue = "false") boolean full, Model model) {

		if (diffRequest.isVersionSet()) {
			ConfigDiffResult result = resultLoader.load(diffRequest.fromVersion, diffRequest.toVersion);
			ConfigDiff configDiff = converter.convert(result);

			model.addAttribute("previousVersion", configDiff.getPreviousVersion());
			model.addAttribute("nextVersion", configDiff.getNextVersion());
			model.addAttribute("full", full);
			List<GroupDiff> groups = configDiff.getGroups().stream()
					.filter(g -> full || g.getDiffType() != ConfigDiffType.EQUALS).collect(Collectors.toList());
			model.addAttribute("diffs", groups);
		}
		else {
			model.addAttribute("previousVersion", "1.3.0.M1");
			model.addAttribute("nextVersion", "1.3.0.BUILD-SNAPSHOT");
			model.addAttribute("diffs", null);
		}
		return "diff";
	}

	@RequestMapping("/diff/{fromVersion}/{toVersion}")
	@ResponseBody
	public ConfigDiffResult diffMetadataApi(@PathParam("fromVersion") String fromVersion,
			@PathParam("toVersion") String toVersion,
			@RequestParam(defaultValue = "false") String full) {
		if (fromVersion == null || toVersion == null) {
			fromVersion = "1.3.0.M1";
			toVersion = "1.3.0.BUILD-SNAPSHOT";
		}

		return resultLoader.load(fromVersion, toVersion);
	}

	static class DiffRequest {

		@Version
		private String fromVersion;

		@Version
		private String toVersion;

		public boolean isVersionSet() {
			return this.fromVersion != null || this.toVersion != null;
		}

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
