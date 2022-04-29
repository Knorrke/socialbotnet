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
  private NamedParameterJdbcTemplate template;

  @Autowired
  public UserDao(DataSource ds) {
    template = new NamedParameterJdbcTemplate(ds);
  }

  @Override
  public User getUserbyUsername(String username) {
    User foundUser = null;
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("username", username);

    String sql = "SELECT * FROM users WHERE users.username=:username";
    List<User> users = template.query(sql, params, userMapper);
    if (users != null && !users.isEmpty()) {
      foundUser = users.get(0);
    }

    return foundUser;
  }

  @Override
  public void registerUser(User user) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("username", user.getUsername());
    params.put("password", user.getPassword());
    String sql = "insert into users (username, password) values (:username, :password)";
    template.update(sql, params);

    // fetch autoincremented id
    String query =
        "SELECT user_id FROM users WHERE username=:username and users.password=:password";
    template.query(
        query,
        params,
        (row, rowNum) -> {
          user.setId(row.getInt("user_id"));
          return user;
        });
  }

  private RowMapper<User> userMapper =
      (row, rowNum) -> {
        User user = new User();

        user.setId(row.getInt("user_id"));
        user.setUsername(row.getString("username"));
        user.setPassword(row.getString("password"));
        user.setHobbies(row.getString("hobbies"));
        user.setAbout(row.getString("about"));

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
  public User getUserbyUsernameWithoutPassword(String username) {
    User foundUser = null;
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("username", username);

    String sql =
        "SELECT user_id, username, null as password, hobbies, about FROM users WHERE users.username=:username";
    List<User> users = template.query(sql, params, userMapper);
    if (users != null && !users.isEmpty()) {
      foundUser = users.get(0);
    }

    return foundUser;
  }

  @Override
  public void updateUser(User oldUser, User newUser) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("user_id", oldUser.getId());
    params.put("username", newUser.getUsername());
    params.put("hobbies", newUser.getHobbies());
    params.put("about", newUser.getAbout());
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

  private Object generateOrderByFromParams(String sortBy, boolean asc) {
    String order = asc ? "ASC" : "DESC";

    String sortingExpression = "users.user_id"; // default
    if (sortBy != null) {
      switch (sortBy) {
        case "username":
          sortingExpression = "users.username";
          break;
        case "hobbies":
          sortingExpression = "users.hobbies";
          break;
        case "about":
          sortingExpression = "users.about";
          break;
        default:
          break;
      }
    }
    return sortingExpression + " " + order;
  }
}
