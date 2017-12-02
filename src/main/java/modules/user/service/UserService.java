package modules.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import modules.user.dao.UserDaoInterface;
import modules.user.model.User;
import modules.util.PasswordUtil;
import spark.Request;

@Service
public class UserService {
	private static final String USER_SESSION_ID = "user";

	@Autowired
	private UserDaoInterface userDaoInterface;


	public User getUserbyUsername(String username) {
		User user = userDaoInterface.getUserbyUsernameWithoutPassword(username);
		return user;
	}

	public User checkUser(User user) {
		User userFound = userDaoInterface.getUserbyUsername(user.getUsername());
		if (userFound != null && PasswordUtil.verifyPassword(user.getPassword(), userFound.getPassword())) {
			return userFound;
		} else {
			return null;
		}
	}

	public void registerUser(User user) {
		user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
		userDaoInterface.registerUser(user);
	}


	public List<User> getAllUsers() {
		return userDaoInterface.getAllUsers();
	}
	
	public void setUserDao(UserDaoInterface userDaoInterface) {
		this.userDaoInterface = userDaoInterface;
	}
	
	public void addAuthenticatedUser(Request req, User user) {
		req.session().attribute(USER_SESSION_ID, user);
	}
	
	public void removeAuthenticatedUser(Request request) {
		request.session().removeAttribute(USER_SESSION_ID);
	}

	public User getAuthenticatedUser(Request request) {
		return request.session().attribute(USER_SESSION_ID);
	}

	public void updateUser(User oldUser, User newUser) {
		this.userDaoInterface.updateUser(oldUser, newUser);
	}
}
