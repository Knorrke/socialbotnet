package config;

import static spark.Spark.*;

import modules.post.service.PostService;
import modules.user.service.UserService;

public class WebConfig {
  public WebConfig(PostService postService, UserService userService) {
    staticFiles.location("/public");
    staticFiles.expireTime(60 * 60 * 24 * 7);
    port(getAssignedPort());
    new Router(postService, userService).setupRoutes();
  }

  static int getAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 30003; // return default port if heroku-port isn't set (i.e. on localhost)
  }
}
