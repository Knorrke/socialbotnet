package config;

import java.io.File;
import java.io.IOException;

import freemarker.template.Configuration;
import freemarker.template.Version;
import spark.template.freemarker.FreeMarkerEngine;

public class FreeMarkerEngineConfig {
	public static FreeMarkerEngine getEngine() {
		Configuration config = new Configuration(new Version(2,3,0));
		try {
			config.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new FreeMarkerEngine(config);
	}
}
