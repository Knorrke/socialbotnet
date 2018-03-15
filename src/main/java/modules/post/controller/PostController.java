package modules.post.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import modules.error.InputTooLongException;
import modules.post.model.Post;
import modules.post.service.PostService;
import modules.user.model.User;
import modules.user.service.UserService;
import modules.util.Renderer;
import spark.Request;
import spark.Response;
import spark.Spark;

public class PostController {
	final static Logger logger = LoggerFactory.getLogger(PostController.class);
	
	private PostService postService;
	private UserService userService;

	public PostController(PostService postService, UserService userService) {
		this.postService = postService;
		this.userService = userService;
	}

	public String getPosts(Request req, Response res) {
		Map<String, Object> model = new HashMap<>();
		User user = userService.getAuthenticatedUser(req);
		if (user != null) {
			model.put("authenticatedUser", user);
		}
		String sortBy = req.queryParams("sortby");
		if (sortBy == null || sortBy.equals("")) {
			List<Post> recentPosts = postService.getLatestWallPosts(50);
			List<Post> mostLiked = postService.getMostLikedWallPosts(5);
			model.put("mostliked", mostLiked);
			model.put("posts", recentPosts);
		} else if (sortBy.equals("likes")){			
			List<Post> posts = postService.getMostLikedWallPosts(50);
			model.put("mostliked", posts);
			model.put("sortby", "likes");
		} else {
			List<Post> posts = postService.getLatestWallPosts(50);
			model.put("posts", posts);
			model.put("sortby", "time");
		}

		return Renderer.render(model, "posts/wall.page.ftl");
	}

	public String likePost(Request req, Response res) {
		User authenticatedUser = userService.getAuthenticatedUser(req);
		if (authenticatedUser == null) {
			Spark.halt(401, "Du bist nicht angemeldet!");
			return null;
		}
		Post post = postService.getPostById(Integer.parseInt(req.params("post")));
		if (post == null ) {
			Spark.halt(400, "Post existiert nicht");
			return null;
		}
		postService.likePost(post, authenticatedUser);
		res.redirect(req.headers("referer"));
		return null;
	}
	
	public String unlikePost(Request req, Response res) {
		User authenticatedUser = userService.getAuthenticatedUser(req);
		if (authenticatedUser == null) {
			Spark.halt(401, "Du bist nicht angemeldet!");
			return null;
		}
		Post post = postService.getPostById(Integer.parseInt(req.params("post")));
		if (post == null ) {
			Spark.halt(400, "Post existiert nicht");
			return null;
		}
		postService.unlikePost(post, authenticatedUser);
		res.redirect(req.headers("referer"));
		return null;
	}
	
	public String createPost(Request req, Response res) {
		User authenticatedUser = userService.getAuthenticatedUser(req);
		if (authenticatedUser == null) {
			Spark.halt(401, "Du bist nicht angemeldet!");
			return null;
		}

		Post post = new Post();
		post.setUser(authenticatedUser);
		post.setPublishingDate(new Timestamp(System.currentTimeMillis()));
		try { // populate post attributes by params
			MultiMap<String> params = new MultiMap<String>();
			UrlEncoded.decodeTo(req.body(), params, "UTF-8");
			BeanUtils.populate(post, params);
			String username = req.params("username");
			if (username != null) {
				post.setWall(userService.getUserbyUsername(username));
				res.redirect("/pinnwand/" + username);
			} else {
				post.setWall(authenticatedUser);
				res.redirect("/pinnwand/" + authenticatedUser.getUsername());
			}
		} catch (Exception e) {
			Spark.halt(500);
			return null;
		}

		try {
			postService.addPost(post);
		} catch (InputTooLongException e) {
			
		}
		return null;
	}
}
