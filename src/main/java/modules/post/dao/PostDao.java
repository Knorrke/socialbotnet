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
	public List<Post> getUserTimelinePosts(User user) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user.getId());
		String sql = "SELECT * FROM post, user WHERE post.author_id = :user AND post.author_id = user.user_id order by post.pub_date desc LIMIT 50";
		return template.query(sql, params, postsMapper);
	}

	@Override
	public List<Post> getPublicTimelinePosts() {
		Map<String, Object> params = new HashMap<String, Object>();

		String sql = "SELECT * FROM post JOIN user ON post.author_id = user.user_id order by post.pub_date desc LIMIT 50";
		return template.query(sql, params, postsMapper);
	}

	@Override
	public void insertPost(Post m) {
		// TODO Auto-generated method stub
		System.err.println("POSTDAO called but not implemented");
	}

	private RowMapper<Post> postsMapper = (rs, rowNum) -> {
		Post post = new Post();
		User user = new User();
		user.setId(rs.getInt("author_id"));
		user.setUsername(rs.getString("username"));
		
		post.setId(rs.getInt("post_id"));
		post.setUser(user);
		post.setMessage(rs.getString("text"));
		
		post.setPublishingDate(rs.getTimestamp("pub_date"));

		return post;
	};
}
