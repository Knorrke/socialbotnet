package modules.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtil {
  public static String jsonify(Object object) {
    Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    return gson.toJson(object);
  }
}
