package config;

import modules.post.controller.PostController;
import modules.post.service.PostService;
import modules.user.controller.UserController;
import modules.user.service.UserService;
import spark.Spark;

public class Router {
	private UserController userController;
	private PostController postController;
	
	public Router(PostService postService, UserService userService) {
		this.userController = new UserController(userService);
		this.postController = new PostController(postService, userService);
	}
	
	public void setupRoutes() {
		
		//User routes
		Spark.get("/register", userController::register);
		Spark.post("/register", userController::register);
		
		Spark.get("/login", userController::login);
		Spark.post("/login", userController::login);
		
		Spark.get("/user/update", userController::updateProfile);
		Spark.post("/user/update", userController::updateProfile);
		
		Spark.get("/", postController::getPosts);
		Spark.get("/timeline/:username", postController::getUserPosts);
	}

}
