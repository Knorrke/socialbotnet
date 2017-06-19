package modules.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import modules.user.dao.UserDaoInterface;
import modules.user.model.User;
import modules.util.PasswordUtil;

@Service
public class UserService {
	@Autowired
	private UserDaoInterface userDaoInterface;

	public User getUserbyUsername(String username) {
		return userDaoInterface.getUserbyUsername(username);
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

	public void setUserDao(UserDaoInterface userDaoInterface) {
		this.userDaoInterface = userDaoInterface;
	}
}
