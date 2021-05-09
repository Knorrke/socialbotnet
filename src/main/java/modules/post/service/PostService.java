package modules.post.service;

import java.util.List;
import modules.error.InputTooLongException;
import modules.post.dao.PostDaoInterface;
import modules.post.model.Post;
import modules.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
  @Autowired private PostDaoInterface postDaoInterface;

  public List<Post> getLatestUserWallPosts(User user, int limit) {
    return postDaoInterface.getLatestUserWallPosts(user, limit);
  }

  public List<Post> getMostLikedUserWallPosts(User user, int limit) {
    return postDaoInterface.getMostLikedUserWallPosts(user, limit);
  }

  public List<Post> getTrendingUserWallPosts(User user, int limit) {
    return postDaoInterface.getTrendingUserWallPosts(user, limit);
  }

  public List<Post> getLatestWallPosts(int limit) {
    return postDaoInterface.getLatestWallPosts(limit);
  }

  public List<Post> getMostLikedWallPosts(int limit) {
    return postDaoInterface.getMostLikedWallPosts(limit);
  }

  public List<Post> getTrendingWallPosts(int limit) {
    return postDaoInterface.getTrendingWallPosts(limit);
  }

  public void addPost(Post post) throws InputTooLongException {
    checkPostDataTooLong(post);
    postDaoInterface.insertPost(post);
  }

  public void setPostDao(PostDaoInterface postDaoInterface) {
    this.postDaoInterface = postDaoInterface;
  }

  public void likePost(Post post, User user) {
    postDaoInterface.likePost(post, user);
  }

  public void unlikePost(Post post, User user) {
    postDaoInterface.unlikePost(post, user);
  }

  public Post getPostById(int id) {
    return postDaoInterface.getPostById(id);
  }

  private boolean checkPostDataTooLong(Post post) throws InputTooLongException {
    if (post.getMessage().length() > 240)
      throw new InputTooLongException("Nachricht", 240, post.getMessage().length());
    else return true;
  }
}
