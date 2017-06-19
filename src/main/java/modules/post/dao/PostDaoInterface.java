package modules.post.dao;

import java.util.List;

import modules.post.model.Post;
import modules.user.model.User;

public interface PostDaoInterface {
	List<Post> getUserTimelinePosts(User user);
	
	List<Post> getPublicTimelinePosts();
	
	void insertPost(Post m);
}