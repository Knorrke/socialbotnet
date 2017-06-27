package modules.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;

import modules.user.model.User;
import modules.user.service.UserService;
import modules.util.Renderer;
import spark.Request;
import spark.Response;
import spark.Spark;

public class UserController {

	private UserService service;

	public UserController(UserService service) {
		this.service = service;
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
			User authenticated = service.checkUser(user);
			if (authenticated != null) {
				service.addAuthenticatedUser(req, authenticated);
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

	public String register(Request req, Response res) {
		Map<String, Object> model = new HashMap<>();
		if (req.requestMethod().equals("POST")) {
			User user = new User();
			try { // populate user attributes by registration params
				MultiMap<String> params = new MultiMap<String>();
				UrlEncoded.decodeTo(req.body(), params, "UTF-8");
				if (params.get("password").equals(params.get("password2"))) {					
					BeanUtils.populate(user, params);
					service.registerUser(user);
					service.addAuthenticatedUser(req, user);
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

	public String updateProfile(Request req, Response res) {
		Map<String, Object> model = new HashMap<>();
		return Renderer.render(model, "user/update.ftl");
	}
}
