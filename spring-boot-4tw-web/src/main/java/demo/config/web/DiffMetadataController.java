package demo.config.web;

import demo.config.diff.ConfigDiffResult;
import demo.config.diff.ConfigDiffType;
import demo.config.diffview.ConfigDiff;
import demo.config.diffview.DiffViewConverter;
import demo.config.diffview.GroupDiff;
import demo.config.model.ConfigurationDiff;
import demo.config.model.ConfigurationDiffHandler;
import demo.config.model.ConfigurationGroupDiff;
import demo.config.service.ConfigurationDiffResultLoader;
import demo.config.springboot.SpringBootVersionService;
import demo.config.validation.Version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DiffMetadataController {

	private final ConfigurationDiffResultLoader resultLoader;

	private final DiffViewConverter converter;

	private final SpringBootVersionService versionService;

	private final ConfigurationDiffHandler handler;

	@Autowired
	public DiffMetadataController(ConfigurationDiffResultLoader resultLoader,
	                              DiffViewConverter converter, SpringBootVersionService versionService) {
		this.resultLoader = resultLoader;
		this.converter = converter;
		this.versionService = versionService;
		this.handler = new ConfigurationDiffHandler();
	}

	@RequestMapping("/server")
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

	@RequestMapping("/diff/{fromVersion}/{toVersion}/")
	@ResponseBody
	public List<ConfigurationGroupDiff> diffMetadataApi(@PathVariable("fromVersion") String fromVersion,
	                                                    @PathVariable("toVersion") String toVersion,
	                                                    @RequestParam(defaultValue = "false") String full) {

		ConfigDiffResult result = resultLoader.load(fromVersion, toVersion);
		ConfigurationDiff configurationDiff = handler.handle(result);

		return configurationDiff.getGroups().stream()
				.filter(g -> "true".equals(full) || g.getDiffType() != ConfigDiffType.EQUALS).collect(Collectors.toList());
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
