package config;

import static spark.Spark.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import modules.error.ResponseError;
import modules.post.controller.PostApiController;
import modules.post.controller.PostController;
import modules.post.service.PostService;
import modules.user.controller.UserApiController;
import modules.user.controller.UserController;
import modules.user.service.UserService;
import modules.util.JSONUtil;

public class Router {
	final static Logger logger = LoggerFactory.getLogger(Router.class);
	private UserController userController;
	private UserApiController userApiController;
	private PostController postController;
	private PostApiController postApiController;
	
	public Router(PostService postService, UserService userService) {
		this.userController = new UserController(userService);
		this.postController = new PostController(postService, userService);
	}
	
	public void setupRoutes() {
		get("/registrieren", userController::register);
		post("/registrieren", userController::register);
		
		get("/login", userController::login);
		post("/login", userController::login);
		
		get("/user/update", userController::updateProfile);
		post("/user/update", userController::updateProfile);
		
		get("/", postController::getPosts);
		get("/pinnwand/:username", postController::getUserPosts);
		
		post("/post", postController::createPost);
		post("/post/:username", postController::createPost);
		
		path("/api", () -> {
			before("/*", (q, a) -> logger.info("Received api call"));
			get("/login", (req, res) -> {
				res.status(400);
				return new ResponseError("Falscher Anfragemodus. Erwartet wurde POST, erhalten wurde GET");
			});
			post("/login", userApiController::login);
			
			get("/users", userApiController::getUsers, JSONUtil::jsonify);
			//get("/posts", postApiController::getPosts, JSONUtil::jsonify);
			
			after((req, res) -> {
				res.type("application/json");
			});
		});
	}

}
