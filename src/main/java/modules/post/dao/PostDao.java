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
		String sql = "SELECT post.*, u.username, u.user_id, w.username as wall_name "
				+ "FROM (post JOIN user u ON post.author_id = u.user_id ) "
				+ "LEFT OUTER JOIN user w ON post.wall_id = w.user_id "
				+ "WHERE post.wall_id = :user "
				+ "ORDER BY post.pub_date DESC LIMIT 50";
		List<Post> posts = template.query(sql, params, postsMapper);
		populateLikedBy(posts);
		return posts;
	}

	private void populateLikedBy(List<Post> posts) {
		posts.stream().forEach(this::populateLikedBy);
	}
	
	private void populateLikedBy(Post post) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "SELECT user.username, user.user_id "
				+ "FROM likes NATURAL JOIN user "
				+ "WHERE likes.post_id = :post_id";
		params.put("post_id", post.getId());
		post.setLikedBy(template.query(sql, params, likesMapper));
	}

	@Override
	public List<Post> getPublicWallPosts() {
		Map<String, Object> params = new HashMap<String, Object>();

		String sql = "SELECT post.*, u.username, u.user_id, w.user_id as wall_id, w.username as wall_name "
				+ "FROM (post JOIN user u ON post.author_id = u.user_id ) "
				+ "LEFT OUTER JOIN user w ON post.wall_id = w.user_id "
				+ "order by post.pub_date desc LIMIT 50";
		List<Post> posts = template.query(sql, params, postsMapper);
		populateLikedBy(posts);
		return posts;
	}

	@Override
	public void insertPost(Post post) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("author_id", post.getUserId());
		params.put("wall_id", post.getWall().getId());
		params.put("text", post.getMessage());
		params.put("pub_date", post.getPublishingDate());
		String sql = "INSERT INTO post (author_id, wall_id, text, pub_date) VALUES (:author_id, :wall_id, :text, :pub_date)";
		
		template.update(sql, params);
	}
	
	@Override
	public void likePost(Post post, User user) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("post_id", post.getId());
		params.put("user_id", user.getId());
		String sql = "INSERT INTO likes (post_id, user_id) VALUES (:post_id, :user_id)";
		template.update(sql, params);
		populateLikedBy(post);
	}

	@Override
	public void unlikePost(Post post, User user) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("post_id", post.getId());
		params.put("user_id", user.getId());
		String sql = "DELETE FROM likes WHERE post_id=:post_id AND user_id=:user_id";
		
		template.update(sql, params);
		populateLikedBy(post);
	}

	@Override
	public Post getPostById(int id) {
		Post foundPost = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("post_id", id);
		
		String sql = "SELECT post.*, u.username, u.user_id, w.username as wall_name "
				+ "FROM (post JOIN user u ON post.author_id = u.user_id ) "
				+ "LEFT OUTER JOIN user w ON post.wall_id = w.user_id "
				+ "WHERE post.post_id = :post_id";
		List<Post> posts = template.query(sql, params, postsMapper);
		if(posts != null && !posts.isEmpty()) {
			populateLikedBy(posts);
			foundPost = posts.get(0);
		}
		
		return foundPost;
	}
	private RowMapper<User> likesMapper = (rs, rowNum) -> {
		User user = new User();
		user.setId(rs.getInt("user_id"));
		user.setUsername(rs.getString("username"));
		return user;
	};
	
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
