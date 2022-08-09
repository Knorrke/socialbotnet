package config;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.template.JavalinFreemarker;
import modules.post.service.PostService;
import modules.user.service.UserService;
import modules.util.JSONUtil;

public class WebConfig {
  Javalin app;

  public WebConfig(PostService postService, UserService userService) {
    app =
        Javalin.create(
            config -> {
              config.addStaticFiles("/public", Location.CLASSPATH);
              config.jsonMapper(JSONUtil.create());
            });

    JavalinFreemarker.configure(FreeMarkerEngineConfig.getConfig());

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
