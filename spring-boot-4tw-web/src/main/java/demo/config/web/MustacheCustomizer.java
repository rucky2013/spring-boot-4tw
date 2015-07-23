package demo.config.web;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

import com.samskivert.mustache.Mustache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mustache.web.MustacheViewResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

/**
 * Add standard functions to {@link MustacheViewResolver}.
 *
 * @author Stephane Nicoll
 */
@Component
class MustacheCustomizer {

	private final ResourceUrlProvider resourceUrlProvider;

	private final MustacheViewResolver mustacheViewResolver;

	@Autowired
	public MustacheCustomizer(ResourceUrlProvider resourceUrlProvider,
			MustacheViewResolver mustacheViewResolver) {
		this.resourceUrlProvider = resourceUrlProvider;
		this.mustacheViewResolver = mustacheViewResolver;
	}

	@PostConstruct
	public void customizeViewResolver() {
		Map<String, Object> attributesMap = new HashMap<>();

		attributesMap.put("diffClass", (Mustache.Lambda) (frag, out) -> {
			switch (frag.execute()) {
				case "ADD":
					out.write("success");
					break;
				case "DELETE":
					out.write("danger");
					break;
			}
		});

		attributesMap.put("idfy", (Mustache.Lambda) (frag, out) ->
				out.write(frag.execute().replaceAll("\\.", "-")));

		attributesMap.put("url", (Mustache.Lambda) (frag, out) -> {
			String url = frag.execute();
			String resourceUrl = resourceUrlProvider.getForLookupPath(url);
			if (resourceUrl != null) {
				out.write(resourceUrl);
			}
			else {
				out.write(url);
			}
		});

		mustacheViewResolver.setAttributesMap(attributesMap);
	}

}
