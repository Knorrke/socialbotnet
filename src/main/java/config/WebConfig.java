package config;

import io.javalin.Javalin;
import io.javalin.http.Header;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinFreemarker;
import java.util.Map;
import modules.post.service.PostService;
import modules.user.service.UserService;
import modules.util.EncodingUtil;
import modules.util.JSONUtil;

public class WebConfig {
  Javalin app;

  public WebConfig(PostService postService, UserService userService) {
    app =
        Javalin.create(
            config -> {
              config.staticFiles.add(
                  staticFiles -> {
                    staticFiles.directory = "/public";
                    staticFiles.location = Location.CLASSPATH;
                    staticFiles.headers =
                        Map.of(
                            Header.CACHE_CONTROL,
                            "max-age=31622400",
                            Header.CONTENT_ENCODING,
                            EncodingUtil.ENCODING);
                  });
              config.jsonMapper(JSONUtil.create());
            });

    JavalinFreemarker.init(FreeMarkerEngineConfig.getConfig());

    app.start(getAssignedPort());
    new Router(app, postService, userService).setupRoutes();
  }

  static int getAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 30003; // return default port if heroku-port isn't set (i.e. on localhost)
  }

  public Javalin getJavalinApp() {
    return app;
  }
}
