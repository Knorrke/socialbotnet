package config;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

import modules.post.service.PostService;
import modules.user.service.UserService;

public class WebConfig {
	public WebConfig(PostService postService, UserService userService) {
    	staticFileLocation("/public");
    	port(30003);
    	new Router(postService, userService).setupRoutes();
    }
}
