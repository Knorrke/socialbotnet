package modules.user.dao;

import java.util.List;

import modules.user.model.User;

public interface UserDaoInterface {

	User getUserbyUsername(String username);
	void registerUser(User user);
	List<User> getAllUsers();
}