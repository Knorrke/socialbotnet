package modules.post.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import modules.post.model.Post;
import modules.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PostDao implements PostDaoInterface {

  private NamedParameterJdbcTemplate template;
  private SimpleJdbcInsert insertTemplate;

  @Autowired
  public PostDao(DataSource ds) {
    template = new NamedParameterJdbcTemplate(ds);
    insertTemplate =
        new SimpleJdbcInsert(ds).withTableName("post").usingGeneratedKeyColumns("post_id");
  }

  @Override
  public List<Post> getLatestUserWallPosts(User user, int limit) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("user", user.getId());
    params.put("limit", limit);
    String sql =
        "SELECT post.*, u.username, u.user_id, w.username as wall_name "
            + "FROM (post JOIN users u ON post.author_id = u.user_id ) "
            + "LEFT OUTER JOIN users w ON post.wall_id = w.user_id "
            + "WHERE post.wall_id = :user "
            + "ORDER BY post.pub_date DESC LIMIT :limit";
    List<Post> posts = template.query(sql, params, postsMapper);
    populateLikedBy(posts);
    return posts;
  }

  @Override
  public List<Post> getMostLikedUserWallPosts(User user, int limit) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("user", user.getId());
    params.put("limit", limit);
    String sql =
        "SELECT post.*, u.username, u.user_id, w.username as wall_name "
            + "FROM (post JOIN users u ON post.author_id = u.user_id ) "
            + "LEFT OUTER JOIN users w ON post.wall_id = w.user_id "
            + "JOIN (SELECT l.post_id, COUNT(l.user_id) as likeCount FROM likes l GROUP BY l.post_id) o ON o.post_id = post.post_id "
            + "WHERE post.wall_id = :user "
            + "ORDER BY o.likeCount desc, post.pub_date desc "
            + "LIMIT :limit";
    List<Post> posts = template.query(sql, params, postsMapper);
    populateLikedBy(posts);
    return posts;
  }

  private void populateLikedBy(List<Post> posts) {
    posts.stream().forEach(this::populateLikedBy);
  }

  private void populateLikedBy(Post post) {
    Map<String, Object> params = new HashMap<String, Object>();
    String sql =
        "SELECT users.username, users.user_id "
            + "FROM likes NATURAL JOIN users "
            + "WHERE likes.post_id = :post_id";
    params.put("post_id", post.getId());
    post.setLikedBy(template.query(sql, params, likesMapper));
  }

  @Override
  public List<Post> getMostLikedWallPosts(int limit) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("limit", limit);

    String sql =
        "SELECT post.*, u.username, u.user_id, w.user_id as wall_id, w.username as wall_name "
            + "FROM (post JOIN users u ON post.author_id = u.user_id ) "
            + "LEFT OUTER JOIN users w ON post.wall_id = w.user_id "
            + "JOIN (SELECT l.post_id, COUNT(l.user_id) as likeCount FROM likes l GROUP BY l.post_id) o ON o.post_id = post.post_id "
            + "ORDER BY o.likeCount desc, post.pub_date desc "
            + "LIMIT :limit";
    List<Post> posts = template.query(sql, params, postsMapper);
    populateLikedBy(posts);
    return posts;
  }

  @Override
  public List<Post> getLatestWallPosts(int limit) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("limit", limit);

    String sql =
        "SELECT post.*, u.username, u.user_id, w.user_id as wall_id, w.username as wall_name "
            + "FROM (post JOIN users u ON post.author_id = u.user_id ) "
            + "LEFT OUTER JOIN users w ON post.wall_id = w.user_id "
            + "ORDER BY post.pub_date desc LIMIT :limit";
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
    String findExisting =
        "SELECT * FROM post WHERE author_id=:author_id and wall_id=:wall_id and text=:text";
    boolean maximumReached = template.query(findExisting, params, (rs, num) -> true).size() > 4;
    if (maximumReached) {
      return;
    }
    Number generatedKey = insertTemplate.executeAndReturnKey(params);
    post.setId(generatedKey.intValue());
  }

  @Override
  public void likePost(Post post, User user) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("post_id", post.getId());
    params.put("user_id", user.getId());
    String findExisting = "SELECT * from likes WHERE post_id=:post_id and user_id=:user_id";
    boolean newLike = template.query(findExisting, params, (rs, num) -> true).isEmpty();
    if (!newLike) {
      return;
    }
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

    String sql =
        "SELECT post.*, u.username, u.user_id, w.username as wall_name "
            + "FROM (post JOIN users u ON post.author_id = u.user_id ) "
            + "LEFT OUTER JOIN users w ON post.wall_id = w.user_id "
            + "WHERE post.post_id = :post_id";
    List<Post> posts = template.query(sql, params, postsMapper);
    if (posts != null && !posts.isEmpty()) {
      populateLikedBy(posts);
      foundPost = posts.get(0);
    }

    return foundPost;
  }

  private RowMapper<User> likesMapper =
      (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        return user;
      };

  private RowMapper<Post> postsMapper =
      (rs, rowNum) -> {
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
