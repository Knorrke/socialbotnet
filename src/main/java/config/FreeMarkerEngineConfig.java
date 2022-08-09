package config;

import freemarker.template.Configuration;
import freemarker.template.Version;
import modules.util.EncodingUtil;

public class FreeMarkerEngineConfig {
  private FreeMarkerEngineConfig() {}

  public static Configuration getConfig() {
    Configuration config = new Configuration(new Version(2, 3, 28));
    config.setClassForTemplateLoading(FreeMarkerEngineConfig.class, "/templates");
    config.setOutputEncoding(EncodingUtil.ENCODING);
    config.setDefaultEncoding(EncodingUtil.ENCODING);
    return config;
  }
}
