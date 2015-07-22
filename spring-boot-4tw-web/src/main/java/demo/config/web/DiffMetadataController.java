package demo.config.web;

import java.io.IOException;
import java.io.Writer;
import javax.websocket.server.PathParam;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import demo.config.service.DiffMetadataService;
import demo.model.ConfigurationDiff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

@Controller
public class DiffMetadataController {

	private final DiffMetadataService metadataService;

	@Autowired
	private ResourceUrlProvider resourceUrlProvider;

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

        /* Add those Lambdas at the application/ViewResolver level? */
		model.addAttribute("diffClass", new Mustache.Lambda() {
			public void execute(Template.Fragment frag, Writer out) throws IOException {
				switch (frag.execute()) {
					case "ADD":
						out.write("success");
						break;
					case "DELETE":
						out.write("danger");
						break;
				}
			}
		});

		model.addAttribute("idfy", new Mustache.Lambda() {
			public void execute(Template.Fragment frag, Writer out) throws IOException {
				out.write(frag.execute().replaceAll("\\.", "-"));
			}
		});

		model.addAttribute("url", new Mustache.Lambda() {
			public void execute(Template.Fragment frag, Writer out) throws IOException {
				String url = frag.execute();
				String resourceUrl = resourceUrlProvider.getForLookupPath(url);
				if (resourceUrl != null) {
					out.write(resourceUrl);
				}
				else {
					out.write(url);
				}
			}
		});

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
