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

  public List<Post> getUserWallPostsSorted(User user, String sortBy, boolean asc, int limit) {
    return postDaoInterface.getUserWallPostsSorted(user, sortBy, asc, limit);
  }

  public List<Post> getUserWallPostsSortedByLikes(User user, boolean asc, int limit) {
    return postDaoInterface.getUserWallPostsSortedByLikes(user, asc, limit);
  }

  public List<Post> getTrendingUserWallPosts(User user, boolean asc, int limit) {
    return postDaoInterface.getTrendingUserWallPosts(user, asc, limit);
  }

  public List<Post> getWallPostsSorted(String sortBy, boolean asc, int limit) {
    return postDaoInterface.getWallPostsSorted(sortBy, asc, limit);
  }

  public List<Post> getWallPostsSortedByLikes(boolean asc, int limit) {
    return postDaoInterface.getWallPostsSortedByLikes(asc, limit);
  }

  public List<Post> getTrendingWallPosts(boolean asc, int limit) {
    return postDaoInterface.getTrendingWallPosts(asc, limit);
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

  public List<Post> getPostsLikedByUser(User user) {
    return postDaoInterface.getPostsLikedByUser(user);
  }

  private boolean checkPostDataTooLong(Post post) throws InputTooLongException {
    if (post.getMessage().length() > 240)
      throw new InputTooLongException("Nachricht", 240, post.getMessage().length());
    else return true;
  }
}
