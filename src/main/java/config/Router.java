package config;

import modules.post.service.PostService;
import modules.user.controller.UserController;
import modules.user.service.UserService;
import spark.Spark;

public class Router {
	private UserController userController;
	
	public Router(PostService postService, UserService userService) {
		this.userController = new UserController(userService);
	}
	
	public void setupRoutes() {
		
		//User routes
		Spark.get("/register", (req, res) -> userController.register(req, res));
		Spark.post("/register", (req, res) -> userController.register(req, res));
		
		Spark.get("/login", (req, res) -> userController.login(req, res));
		Spark.post("/login", (req, res) -> userController.login(req, res));
		
		Spark.get("/user/update", (req, res) -> userController.updateProfile(req, res));
		Spark.post("/user/update", (req, res) -> userController.updateProfile(req, res));
		
	}

}
