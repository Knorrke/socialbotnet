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

}
