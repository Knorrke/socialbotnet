package modules.post.dao;

import java.util.List;

import modules.post.model.Post;
import modules.user.model.User;

public interface PostDaoInterface {
	List<Post> getUserWallPosts(User user);
	
	List<Post> getPublicWallPosts();
	
	void insertPost(Post m);
}