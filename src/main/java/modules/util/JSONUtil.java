package modules.util;

import com.google.gson.Gson;

public class JSONUtil {
  public static String jsonify(Object object) {
    return new Gson().toJson(object);
  }
}
