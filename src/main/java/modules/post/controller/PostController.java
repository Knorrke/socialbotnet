package modules.post.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modules.post.model.Post;
import modules.post.service.PostService;
import modules.user.model.User;
import modules.user.service.UserService;
import modules.util.Renderer;
import spark.Request;
import spark.Response;

public class PostController {

	private PostService postService;
	private UserService userService;
	
	public PostController(PostService postService, UserService userService) {
		this.postService = postService;
		this.userService = userService;
	}

	public String getPosts(Request req, Response res) {
		Map<String, Object> model = new HashMap<>();
		User user = userService.getAuthenticatedUser(req);
		if(user != null) {			
			model.put("authenticatedUser", user);
		}
		List<Post> posts = postService.getPublicTimelinePosts();
		model.put("posts", posts);

		return Renderer.render(model, "posts/timeline.ftl");
	}
	
	public String getUserPosts(Request req, Response res) {
		String username = req.params("username");
		User profileUser = userService.getUserbyUsername(username);
		User authenticatedUser = userService.getAuthenticatedUser(req);
		Map<String, Object> model = new HashMap<>();
		if (authenticatedUser != null) {
			model.put("authenticatedUser", authenticatedUser);
		};
		model.put("user", profileUser);
		List<Post> posts = postService.getUserTimelinePosts(profileUser);
		model.put("posts", posts);

		return Renderer.render(model, "posts/timeline.ftl");
	}
}
