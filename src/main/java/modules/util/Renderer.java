package modules.util;

import java.util.Map;

import config.FreeMarkerEngineConfig;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

public class Renderer {
	
	private static FreeMarkerEngine freeMarkerEngine = FreeMarkerEngineConfig.getEngine();
	
	public static String render(Map<String, Object> model, String path) {
		return freeMarkerEngine.render(new ModelAndView(model, path));
	}
}
