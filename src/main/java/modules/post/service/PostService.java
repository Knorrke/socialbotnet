package modules.post.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import modules.post.dao.PostDaoInterface;
import modules.post.model.Post;
import modules.user.model.User;

@Service
public class PostService {
	@Autowired
	private PostDaoInterface postDaoInterface;

	public List<Post> getUserWallPosts(User user) {
		return postDaoInterface.getUserWallPosts(user);
	}

	public List<Post> getPublicWallPosts() {
		return postDaoInterface.getPublicWallPosts();
	}

	public void addPost(Post post) {
		postDaoInterface.insertPost(post);
	}

	public void setPostDao(PostDaoInterface postDaoInterface) {
		this.postDaoInterface = postDaoInterface;
	}
}
