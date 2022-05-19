package modules.util;

import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import spark.Request;

public class DecodeParams {
  public static final String ENCODING = "UTF-8";

  public static MultiMap<String> decode(Request req) {
    MultiMap<String> params = new MultiMap<String>();
    UrlEncoded.decodeTo(req.body(), params, ENCODING);
    return params;
  }
}
