package config;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

import modules.post.service.PostService;
import modules.user.service.UserService;

public class WebConfig {
  public WebConfig(PostService postService, UserService userService) {
    staticFileLocation("/public");
    port(getHerokuAssignedPort());

    new Router(postService, userService).setupRoutes();
  }

  static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 30003; // return default port if heroku-port isn't set (i.e. on localhost)
  }
}
