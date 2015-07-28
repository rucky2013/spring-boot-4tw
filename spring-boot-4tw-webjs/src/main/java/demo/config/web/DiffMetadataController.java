package demo.config.web;

import demo.config.diff.ConfigDiffResult;
import demo.config.diff.ConfigDiffType;
import demo.config.model.ConfigurationDiff;
import demo.config.model.ConfigurationDiffHandler;
import demo.config.model.ConfigurationGroupDiff;
import demo.config.service.ConfigurationDiffResultLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

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
	public List<ConfigurationGroupDiff> diffMetadataApi(@PathVariable("fromVersion") String fromVersion,
	                                                    @PathVariable("toVersion") String toVersion,
	                                                    @RequestParam(defaultValue = "false") String full) {

		ConfigDiffResult result = resultLoader.load(fromVersion, toVersion);
		ConfigurationDiff configurationDiff = handler.handle(result);

		return configurationDiff.getGroups().stream()
				.filter(g -> "true".equals(full) || g.getDiffType() != ConfigDiffType.EQUALS).collect(Collectors.toList());
	}

}
