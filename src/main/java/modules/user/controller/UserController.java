package modules.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;

import modules.post.model.Post;
import modules.post.service.PostService;
import modules.user.model.User;
import modules.user.service.UserService;
import modules.util.Renderer;
import spark.Request;
import spark.Response;
import spark.Spark;

public class UserController {

	private UserService userService;
	private PostService postService;

	public UserController(UserService userService, PostService postService) {
		this.userService = userService;
		this.postService = postService;
	}

	public String login(Request req, Response res) {
		Map<String, Object> model = new HashMap<>();
		if (req.requestMethod().equals("POST")) {
			User user = new User();
			try { // populate user attributes by login params
				MultiMap<String> params = new MultiMap<String>();
				UrlEncoded.decodeTo(req.body(), params, "UTF-8");
				BeanUtils.populate(user, params);
			} catch (Exception e) {
				Spark.halt(501);
				return null;
			}
			User authenticated = userService.checkUser(user);
			if (authenticated != null) {
				userService.addAuthenticatedUser(req, authenticated);
				res.redirect("/");
				Spark.halt();
				model.put("username", user.getUsername());
				model.put("success", "Login erfolgreich. Herzlich Willkommen! :-)");
			} else {
				model.put("error", "Login fehlgeschlagen.");
			}
		}

		return Renderer.render(model, "user/login.ftl");
	}
	
	public String logout(Request req, Response res) {
		userService.removeAuthenticatedUser(req);
		res.redirect("/");
		Spark.halt();
		return null;
	}

	public String register(Request req, Response res) {
		Map<String, Object> model = new HashMap<>();
		if (req.requestMethod().equals("POST")) {
			User user = new User();
			try { // populate user attributes by registration params
				MultiMap<String> params = new MultiMap<String>();
				UrlEncoded.decodeTo(req.body(), params, "UTF-8");
				if (params.get("password").equals(params.get("password2"))) {					
					BeanUtils.populate(user, params);
					userService.registerUser(user);
					userService.addAuthenticatedUser(req, user);
					res.redirect("/");
					Spark.halt();
					model.put("username", user.getUsername());
					model.put("success", "Registrierung erfolgreich");
				} else {
					model.put("error", "Passw&ouml;rter stimmen nicht &uuml;berein");
				}
			} catch (Exception e) {
				Spark.halt(501);
				return null;
			}
		}

		return Renderer.render(model, "user/register.ftl");

	}

	public String showProfile(Request req, Response res) {
		Map<String, Object> model = new HashMap<>();

		String username = req.params("username");
		User profileUser = userService.getUserbyUsername(username);
		if (profileUser == null ) {
			Spark.halt(400, "User unbekannt");
			return null;
		}
		model.put("user", profileUser);

		User authenticatedUser = userService.getAuthenticatedUser(req);
		if (authenticatedUser != null) {
			model.put("authenticatedUser", authenticatedUser);
		}

		List<Post> posts = postService.getUserWallPosts(profileUser);
		model.put("posts", posts);
		return Renderer.render(model, "user/profile.ftl");
	}
	
	public String updateProfile(Request req, Response res) {
		User authenticatedUser = userService.getAuthenticatedUser(req);
		if (authenticatedUser == null) {
			Spark.halt(401, "Du bist nicht angemeldet!");
			return null;
		}
		Map<String, Object> model = new HashMap<>();
		if (req.requestMethod().equals("POST")) {
			User user = new User();
			try { // populate user attributes by registration params
				MultiMap<String> params = new MultiMap<String>();
				UrlEncoded.decodeTo(req.body(), params, "UTF-8");
				BeanUtils.populate(user, params);
				userService.updateUser(authenticatedUser, user);
				userService.addAuthenticatedUser(req, user);
				model.put("success", "Profil erfolgreich aktualisiert");
				res.redirect("/user/profile/"+user.getUsername());
			} catch (Exception e) {
				Spark.halt(501);
				return null;
			}
		}
		
		model.put("authenticatedUser", authenticatedUser);
		return Renderer.render(model, "user/updateProfile.ftl");
	}
}
