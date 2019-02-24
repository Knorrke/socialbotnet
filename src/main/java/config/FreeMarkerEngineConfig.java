package config;

import freemarker.template.Configuration;
import freemarker.template.Version;
import spark.template.freemarker.FreeMarkerEngine;

public class FreeMarkerEngineConfig {
  public static FreeMarkerEngine getEngine() {
    Configuration config = new Configuration(new Version(2, 3, 23));
    config.setClassForTemplateLoading(FreeMarkerEngineConfig.class, "/templates");
    return new FreeMarkerEngine(config);
  }
}
