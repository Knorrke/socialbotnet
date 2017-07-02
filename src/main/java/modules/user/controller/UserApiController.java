package modules.user.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;

import modules.error.ResponseError;
import modules.user.model.User;
import modules.user.service.UserService;
import spark.Request;
import spark.Response;
import spark.Spark;

public class UserApiController {

	private UserService service;
	private volatile Set<String> authenticated = new HashSet<String>();

	public UserApiController(UserService service) {
		this.service = service;
	}

	public Object login(Request req, Response res) {
		User user = new User();
		try { 
			MultiMap<String> params = new MultiMap<String>();
			UrlEncoded.decodeTo(req.body(), params, "UTF-8");
			BeanUtils.populate(user, params);
		} catch (Exception e) {
			Spark.halt(501);
			res.status(501);
			return new ResponseError("Interner Fehler aufgetreten. Bitte melde das Problem!");
		}
		User authenticated = service.checkUser(user);
		if (authenticated == null) {
			res.status(401);
			return new ResponseError("Login fehlgeschlagen.");
		}
		
		return registerAuthentication();
	}

	private synchronized String registerAuthentication() {
		String uuid = UUID.randomUUID().toString();
		authenticated.add(uuid);
		
		return uuid;
	}
	
	public Object getUsers(Request req, Response res) {
		if (!isAuthenticated(req)) {
			return new ResponseError("Nicht authentifiziert. Bitte zuerst einloggen und den Authentifizierungsschlüssel anschließend mitsenden");
		}
		return service.getAllUsers();
	}

	private boolean isAuthenticated(Request req) {
		// TODO Auto-generated method stub
		//return false;
	}
}
