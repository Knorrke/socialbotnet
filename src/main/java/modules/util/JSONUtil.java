package modules.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.json.JsonMapper;
import java.lang.reflect.Type;

public class JSONUtil implements JsonMapper {
  private static JSONUtil instance;
  private Gson gson;

  private JSONUtil() {
    gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
  }

  public static JSONUtil create() {
    if (instance == null) {
      instance = new JSONUtil();
    }
    return instance;
  }

  @Override
  public String toJsonString(Object obj, Type type) {
    return gson.toJson(obj, type);
  }

  @Override
  public <T> T fromJsonString(String json, Type type) {
    return gson.fromJson(json, type);
  }
}
