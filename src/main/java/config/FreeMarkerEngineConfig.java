package config;

import freemarker.template.Configuration;
import freemarker.template.Version;
import modules.util.DecodeParams;
import spark.template.freemarker.FreeMarkerEngine;

public class FreeMarkerEngineConfig {
  public static FreeMarkerEngine getEngine() {
    Configuration config = new Configuration(new Version(2, 3, 28));
    config.setClassForTemplateLoading(FreeMarkerEngineConfig.class, "/templates");
    config.setOutputEncoding(DecodeParams.ENCODING);
    config.setDefaultEncoding(DecodeParams.ENCODING);
    return new FreeMarkerEngine(config);
  }
}
