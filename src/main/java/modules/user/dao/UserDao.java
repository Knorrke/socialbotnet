package modules.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import modules.user.model.User;

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
		
		String sql = "SELECT * FROM user WHERE user.username=:username";
		List<User> users = template.query(sql, params, userMapper);
		if(users != null && !users.isEmpty()) {
			foundUser = users.get(0);
		}
		
		return foundUser;
	}

	@Override
	public void registerUser(User user) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", user.getUsername());
		params.put("password", user.getPassword());
		String sql = "insert into user (username, password) values (:username, :password)";
		template.update(sql, params);

		//fetch autoincremented id 
		String query = "SELECT user_id FROM user WHERE username=:username and user.password=:password";
		template.query(query, params, (row, rowNum) -> {
			user.setId(row.getInt("user_id"));
			return user;
		});
	}

	private RowMapper<User> userMapper = (row, rowNum) -> {
		User user = new User();

		user.setId(row.getInt("user_id"));
		user.setUsername(row.getString("username"));
		user.setPassword(row.getString("password"));

		return user;
	};

	@Override
	public List<User> getAllUsers() {
		String sql = "SELECT user_id, username, null as password FROM user ORDER BY user_id DESC LIMIT 100";
		List<User> users = template.query(sql, userMapper);
		return users;
	}

	@Override
	public User getUserbyUsernameWithoutPassword(String username) {
		User foundUser = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		
		String sql = "SELECT user_id, username, null as password FROM user WHERE user.username=:username";
		List<User> users = template.query(sql, params, userMapper);
		if(users != null && !users.isEmpty()) {
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
		String sql = "UPDATE user " +
				"SET username=:username, hobbies=:hobbies, about=:about " +
				"WHERE user_id = :user_id";
		template.update(sql, params);
		newUser.setId(oldUser.getId());
		oldUser.setUsername(newUser.getUsername());
		oldUser.setHobbies(newUser.getHobbies());
		oldUser.setAbout(newUser.getAbout());		
	}

}
