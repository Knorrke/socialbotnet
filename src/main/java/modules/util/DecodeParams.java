package modules.util;

import org.eclipse.jetty.util.MultiMap;
import io.javalin.http.Context;

public class DecodeParams {
  public static final String ENCODING = "UTF-8";

  public static MultiMap<String> decode(Context ctx) {
    return new MultiMap<String>(ctx.formParamMap());
  }
}
