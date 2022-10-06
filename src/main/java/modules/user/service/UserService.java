package modules.user.service;

import io.javalin.http.Context;
import java.util.List;
import modules.error.InputTooLongException;
import modules.user.dao.UserDaoInterface;
import modules.user.model.User;
import modules.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private static final String USER_SESSION_ID = "user";

  @Autowired private UserDaoInterface userDaoInterface;

  public User getUserbyUsername(String username) {
    return userDaoInterface.getUserByUsernameWithoutPassword(username);
  }

  public User getUserById(int id) {
    return userDaoInterface.getUserByIdWithoutPassword(id);
  }

  public User checkUser(User user) {
    User userFound = userDaoInterface.getUserbyUsername(user.getUsername());
    if (userFound != null
        && PasswordUtil.verifyPassword(user.getPassword(), userFound.getPassword())) {
      return userFound;
    } else {
      return null;
    }
  }

  public void registerUser(User user) throws InputTooLongException {
    user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
    checkUserdataToLong(user);
    userDaoInterface.registerUser(user);
  }

  public List<User> getAllUsersSorted(String sortBy, boolean asc, int limit) {
    return userDaoInterface.getAllUsersSorted(sortBy, asc, limit);
  }

  public void setUserDao(UserDaoInterface userDaoInterface) {
    this.userDaoInterface = userDaoInterface;
  }

  public void addAuthenticatedUser(Context ctx, User user) {
    ctx.sessionAttribute(USER_SESSION_ID, user);
  }

  public void removeAuthenticatedUser(Context ctx) {
    ctx.req().getSession().removeAttribute(USER_SESSION_ID);
  }

  public User getAuthenticatedUser(Context ctx) {
    return ctx.sessionAttribute(USER_SESSION_ID);
  }

  public void updateUser(User oldUser, User newUser) throws InputTooLongException {
    checkUserdataToLong(newUser);
    this.userDaoInterface.updateUser(oldUser, newUser);
  }

  private boolean checkUserdataToLong(User user) throws InputTooLongException {
    return checkData("Benutzername", 50, user.getUsername())
        && checkData("Ueber mich", 255, user.getAbout())
        && checkData("Hobbies", 255, user.getHobbies());
  }

  private boolean checkData(String description, int maxlength, String data)
      throws InputTooLongException {
    int length = data.length();
    if (maxlength < length) throw new InputTooLongException(description, maxlength, length);
    else return true;
  }
}
