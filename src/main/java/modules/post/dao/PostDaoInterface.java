package modules.post.dao;

import java.util.List;

import modules.post.model.Post;
import modules.user.model.User;

public interface PostDaoInterface {
	List<Post> getLatestUserWallPosts(User user, int limit);
	List<Post> getMostLikedUserWallPosts(User user, int limit);
	List<Post> getMostLikedWallPosts(int limit);
	List<Post> getLatestWallPosts(int limit);
	
	void insertPost(Post m);

	void likePost(Post post, User user);
	void unlikePost(Post post, User user);

	Post getPostById(int id);

}