package modules.post.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import modules.post.model.Post;
import modules.user.model.User;

@Repository
public class PostDao implements PostDaoInterface {

	private NamedParameterJdbcTemplate template;

	@Autowired
	public PostDao(DataSource ds) {
		template = new NamedParameterJdbcTemplate(ds);
	}

	@Override
	public List<Post> getUserWallPosts(User user) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user.getId());
		String sql = "SELECT post.*, u.*, w.username as wall_name "
				+ "FROM (post JOIN user u ON post.author_id = u.user_id ) "
				+ "LEFT OUTER JOIN user w ON post.wall_id = w.user_id "
				+ "WHERE post.wall_id = :user "
				+ "ORDER BY post.pub_date DESC LIMIT 50";
		return template.query(sql, params, postsMapper);
	}

	@Override
	public List<Post> getPublicWallPosts() {
		Map<String, Object> params = new HashMap<String, Object>();

		String sql = "SELECT post.*, u.*, w.user_id as wall_id, w.username as wall_name "
				+ "FROM (post JOIN user u ON post.author_id = u.user_id ) "
				+ "LEFT OUTER JOIN user w ON post.wall_id = w.user_id "
				+ "order by post.pub_date desc LIMIT 50";
		return template.query(sql, params, postsMapper);
	}

	@Override
	public void insertPost(Post post) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("author_id", post.getUserId());
		params.put("wall_id", post.getWall().getId());
		params.put("text", post.getMessage());
		params.put("pub_date", post.getPublishingDate());
		String sql = "insert into post (author_id, wall_id, text, pub_date) values (:author_id, :wall_id, :text, :pub_date)";
		
		template.update(sql, params);
	}

	private RowMapper<Post> postsMapper = (rs, rowNum) -> {
		Post post = new Post();
		post.setId(rs.getInt("post_id"));
		post.setMessage(rs.getString("text"));
		post.setPublishingDate(rs.getTimestamp("pub_date"));

		User user = new User();		
		user.setId(rs.getInt("author_id"));
		user.setUsername(rs.getString("username"));
		post.setUser(user);

		User wall = new User();
		wall.setId(rs.getInt("wall_id"));
		wall.setUsername(rs.getString("wall_name"));
		post.setWall(wall);
		
		return post;
	};
}
