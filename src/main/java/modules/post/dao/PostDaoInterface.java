package modules.post.dao;

import java.util.List;
import modules.post.model.Post;
import modules.user.model.User;

public interface PostDaoInterface {
  List<Post> getUserWallPostsSorted(User user, String sortBy, boolean asc, int limit);

  List<Post> getUserWallPostsSortedByLikes(User user, boolean asc, int limit);

  List<Post> getTrendingUserWallPosts(User user, boolean asc, int limit);

  List<Post> getWallPostsSorted(String sortBy, boolean asc, int limit);

  List<Post> getWallPostsSortedByLikes(boolean asc, int limit);

  List<Post> getTrendingWallPosts(boolean asc, int limit);

  void insertPost(Post m);

  void likePost(Post post, User user);

  void unlikePost(Post post, User user);

  Post getPostById(int id);

  List<Post> getPostsLikedByUser(User user);
}
