package modules.util;

import io.javalin.http.Context;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.eclipse.jetty.util.MultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncodingUtil {
  private static Logger logger = LoggerFactory.getLogger(EncodingUtil.class);
  public static final String ENCODING = "UTF-8";

  private EncodingUtil() {}

  public static MultiMap<String> decode(Context ctx) {
    return new MultiMap<>(ctx.formParamMap());
  }

  public static String uriEncode(String s) {
    try {
      return URLEncoder.encode(s, ENCODING).replaceAll("[+]", "%20");
    } catch (UnsupportedEncodingException e) {
      logger.error("Unsupported Encoding {}", ENCODING);
      return "";
    }
  }
}
