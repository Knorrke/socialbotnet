package modules.user.dao;

import modules.user.model.User;

public interface UserDaoInterface {

	User getUserbyUsername(String username);
	void registerUser(User user);
}