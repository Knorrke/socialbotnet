package modules.util;

import org.jetbrains.annotations.NotNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.plugin.json.JsonMapper;

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
  
  public String toJsonString(@NotNull Object obj) {
    return gson.toJson(obj);
  }
}
