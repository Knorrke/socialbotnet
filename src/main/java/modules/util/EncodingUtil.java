package modules.util;

import io.javalin.http.Context;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import org.eclipse.jetty.util.MultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncodingUtil {
  private static Logger logger = LoggerFactory.getLogger(EncodingUtil.class);
  public static final String ENCODING = "UTF-8";

  private EncodingUtil() {}

  public static MultiMap<String> decode(Context ctx) {
    Map<String, List<String>> map = ctx.formParamMap();
    MultiMap<String> caseInsensitive = new MultiMap<>();
    map.forEach((k, v) -> caseInsensitive.put(k.toLowerCase(), v));
    return caseInsensitive;
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
