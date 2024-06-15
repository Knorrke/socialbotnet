package modules.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import modules.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao implements UserDaoInterface {
  public static final String ABOUT = "about";
  public static final String HOBBIES = "hobbies";
  public static final String ID = "user_id";
  public static final String PASSWORD = "password";
  public static final String USERNAME = "username";
  private NamedParameterJdbcTemplate template;

  @Autowired
  public UserDao(DataSource ds) {
    template = new NamedParameterJdbcTemplate(ds);
  }

  @Override
  public User getUserbyUsername(String username) {
    User foundUser = null;
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(USERNAME, username);

    String sql = "SELECT * FROM users WHERE users.username=:username";
    List<User> users = template.query(sql, params, userMapper);
    if (!users.isEmpty()) {
      foundUser = users.get(0);
    }

    return foundUser;
  }

  @Override
  public void registerUser(User user) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(USERNAME, user.getUsername());
    params.put(PASSWORD, user.getPassword());
    String sql = "insert into users (username, password) values (:username, :password)";
    template.update(sql, params);

    // fetch autoincremented id
    String query =
        "SELECT user_id FROM users WHERE username=:username and users.password=:password";
    template.query(
        query,
        params,
        (row, rowNum) -> {
          user.setId(row.getInt(ID));
          return user;
        });
  }

  private RowMapper<User> userMapper =
      (row, rowNum) -> {
        User user = new User();

        user.setId(row.getInt(ID));
        user.setUsername(row.getString(USERNAME));
        user.setPassword(row.getString(PASSWORD));
        user.setHobbies(row.getString(HOBBIES));
        user.setAbout(row.getString(ABOUT));

        return user;
      };

  @Override
  public List<User> getAllUsersSorted(String sortBy, boolean asc, int limit) {
    String sql =
        String.format(
            "SELECT user_id, username, null as password, hobbies, about FROM users ORDER BY %s LIMIT %d",
            generateOrderByFromParams(sortBy, asc), limit);
    List<User> users = template.query(sql, userMapper);
    return users;
  }

  @Override
  public User getUserByIdWithoutPassword(int id) {
    User foundUser = null;
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(ID, id);

    String sql =
        "SELECT user_id, username, null as password, hobbies, about FROM users WHERE users.user_id=:user_id";
    List<User> users = template.query(sql, params, userMapper);
    if (!users.isEmpty()) {
      foundUser = users.get(0);
    }

    return foundUser;
  }

  @Override
  public User getUserByUsernameWithoutPassword(String username) {
    User foundUser = null;
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(USERNAME, username);

    String sql =
        "SELECT user_id, username, null as password, hobbies, about FROM users WHERE users.username=:username";
    List<User> users = template.query(sql, params, userMapper);
    if (!users.isEmpty()) {
      foundUser = users.get(0);
    }

    return foundUser;
  }

  @Override
  public void updateUser(User oldUser, User newUser) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(ID, oldUser.getId());
    params.put(USERNAME, newUser.getUsername());
    params.put(HOBBIES, newUser.getHobbies());
    params.put(ABOUT, newUser.getAbout());
    String sql =
        "UPDATE users "
            + "SET username=:username, hobbies=:hobbies, about=:about "
            + "WHERE user_id = :user_id";
    template.update(sql, params);
    newUser.setId(oldUser.getId());
    oldUser.setUsername(newUser.getUsername());
    oldUser.setHobbies(newUser.getHobbies());
    oldUser.setAbout(newUser.getAbout());
  }

  @Override
  public void updatePassword(User newUser) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(ID, newUser.getId());
    params.put(PASSWORD, newUser.getPassword());
    String sql = "UPDATE users " + "SET password=:password " + "WHERE user_id = :user_id";
    template.update(sql, params);
  }

  private String generateOrderByFromParams(String sortBy, boolean asc) {
    String order = asc ? "ASC" : "DESC";

    String sortingExpression = ID; // default
    if (sortBy != null) {
      switch (sortBy) {
        case USERNAME:
          sortingExpression = USERNAME;
          break;
        case HOBBIES:
          sortingExpression = HOBBIES;
          break;
        case ABOUT:
          sortingExpression = ABOUT;
          break;
        default:
          break;
      }
    }
    return sortingExpression + " " + order;
  }
}
