package modules.user.controller;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;

import modules.user.model.User;
import modules.user.service.UserService;
import spark.Request;
import spark.Response;
import spark.Spark;

public class UserApiController {

	private UserService service;

	public UserApiController(UserService service) {
		this.service = service;
	}

	public User login(Request req, Response res) {
		User user = new User();
		try { 
			MultiMap<String> params = new MultiMap<String>();
			UrlEncoded.decodeTo(req.body(), params, "UTF-8");
			BeanUtils.populate(user, params);
		} catch (Exception e) {
			Spark.halt(501, "Interner Fehler aufgetreten. Bitte melde das Problem!");
		}
		return service.checkUser(user);
	}

	
	public Object getUsers(Request req, Response res) {
		return service.getAllUsers();
	}
}
