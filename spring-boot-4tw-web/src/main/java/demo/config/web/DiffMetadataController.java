package demo.config.web;

import javax.websocket.server.PathParam;

import demo.config.model.ConfigurationDiff;
import demo.config.service.DiffMetadataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DiffMetadataController {

	private final DiffMetadataService metadataService;

	@Autowired
	public DiffMetadataController(DiffMetadataService metadataService) {
		this.metadataService = metadataService;
	}

	@RequestMapping("/")
	public String diffMetadata(@RequestParam(required = false) String fromVersion,
			@RequestParam(required = false) String toVersion,
			@RequestParam(defaultValue = "false") String full, Model model) {

		if (fromVersion == null || toVersion == null) {
			fromVersion = "1.3.0.M1";
			toVersion = "1.3.0.BUILD-SNAPSHOT";
		}

		ConfigurationDiff diff = metadataService.metadataDiff(fromVersion, toVersion);

		model.addAttribute("previousVersion", diff.getLeftVersion());
		model.addAttribute("nextVersion", diff.getRightVersion());
		model.addAttribute("diffs", diff.getGroups());

		return "diff";
	}

	@RequestMapping("/diff/{fromVersion}/{toVersion}")
	@ResponseBody
	public ConfigurationDiff diffMetadataApi(@PathParam("fromVersion") String fromVersion,
			@PathParam("toVersion") String toVersion,
			@RequestParam(defaultValue = "false") String full) {
		if (fromVersion == null || toVersion == null) {
			fromVersion = "1.3.0.M1";
			toVersion = "1.3.0.BUILD-SNAPSHOT";
		}

		return metadataService.metadataDiff(fromVersion, toVersion);
	}

}
