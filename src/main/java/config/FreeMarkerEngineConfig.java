package config;

import freemarker.template.Configuration;
import freemarker.template.Version;
import spark.template.freemarker.FreeMarkerEngine;

public class FreeMarkerEngineConfig {
  public static FreeMarkerEngine getEngine() {
    Configuration config = new Configuration(new Version(2, 3, 28));
    config.setClassForTemplateLoading(FreeMarkerEngineConfig.class, "/templates");
    config.setOutputEncoding("UTF-8");
    config.setDefaultEncoding("UTF-8");
    return new FreeMarkerEngine(config);
  }
}
