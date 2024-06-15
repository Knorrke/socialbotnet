package modules.user.dao;

import java.util.List;
import modules.user.model.User;

public interface UserDaoInterface {

  User getUserbyUsername(String username);

  User getUserByIdWithoutPassword(int id);

  User getUserByUsernameWithoutPassword(String username);

  void registerUser(User user);

  List<User> getAllUsersSorted(String sortBy, boolean asc, int limit);

  void updateUser(User oldUser, User newUser);

  void updatePassword(User newUser);
}
