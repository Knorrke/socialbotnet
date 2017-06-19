package modules.post.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
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
		// TODO Auto-generated method stub
		System.err.println("POSTDAO called but not implemented");
		return null;
	}

	@Override
	public List<Post> getPublicTimelinePosts() {
		// TODO Auto-generated method stub
		System.err.println("POSTDAO called but not implemented");
		return null;
	}

	@Override
	public void insertPost(Post m) {
		// TODO Auto-generated method stub
		System.err.println("POSTDAO called but not implemented");
	}

}
