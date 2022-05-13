package modules.post.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import modules.post.model.Post;
import modules.user.dao.UserDao;
import modules.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PostDao implements PostDaoInterface {

  private static final String PUBLISHING_DATE = "pub_date";
  private static final String MESSAGE = "text";
  private static final String ID = "post_id";
  private static final String AUTHOR_ID = "author_id";
  private static final String WALL_ID = "wall_id";
  private static final String LIKES_COUNT = "likes_count";
  private static final String SCORE = "score";

  private NamedParameterJdbcTemplate template;
  private SimpleJdbcInsert insertTemplate;

  private static final String DEEP_SELECT =
      String.format(
          "SELECT p.*, u.%s, u.%s, u.%s, u.%s, w.%1$s as wall_%1$s, w.%2$s as wall_%2$s, w.%3$s as wall_%3$s, w.%4$s as wall_%4$s,"
              // Rank posts by:  likes / (1+days)^gravity
              + "(likes_count / POWER(1 + EXTRACT(epoch from AGE(NOW(), p.pub_date)) / 86400, 2)) as score",
          UserDao.ID, UserDao.USERNAME, UserDao.HOBBIES, UserDao.ABOUT);

  @Autowired
  public PostDao(DataSource ds) {
    template = new NamedParameterJdbcTemplate(ds);
    insertTemplate = new SimpleJdbcInsert(ds).withTableName("post").usingGeneratedKeyColumns(ID);
  }

  @Override
  public List<Post> getUserWallPostsSorted(User user, String sortBy, boolean asc, int limit) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("user", user.getId());
    params.put("limit", limit);
    String orderBy = " ORDER BY " + generateOrderByFromParams(sortBy, asc);
    String sql =
        DEEP_SELECT
            + " FROM (post p JOIN users u ON p.author_id = u.user_id )"
            + " LEFT OUTER JOIN users w ON p.wall_id = w.user_id"
            + " WHERE p.wall_id = :user"
            + orderBy
            + " LIMIT :limit";
    List<Post> posts = template.query(sql, params, postsMapper);
    populateLikedBy(posts);
    return posts;
  }

  @Override
  public List<Post> getWallPostsSorted(String sortBy, boolean asc, int limit) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("limit", limit);

    String orderBy = String.format(" ORDER BY %s", generateOrderByFromParams(sortBy, asc));

    String sql =
        DEEP_SELECT
            + " FROM (post p JOIN users u ON p.author_id = u.user_id )"
            + " LEFT OUTER JOIN users w ON p.wall_id = w.user_id"
            + orderBy
            + " LIMIT :limit";
    List<Post> posts = template.query(sql, params, postsMapper);
    populateLikedBy(posts);
    return posts;
  }

  @Override
  public void insertPost(Post post) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(AUTHOR_ID, post.getUserId());
    params.put(WALL_ID, post.getWall().getId());
    params.put(MESSAGE, post.getMessage());
    params.put(PUBLISHING_DATE, post.getPublishingDate());
    params.put(LIKES_COUNT, 0);
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
    params.put(ID, post.getId());
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
    params.put(ID, post.getId());
    params.put("user_id", user.getId());
    String sql = "DELETE FROM likes WHERE post_id=:post_id AND user_id=:user_id";

    template.update(sql, params);
    populateLikedBy(post);
  }

  @Override
  public Post getPostById(int id) {
    Post foundPost = null;
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(ID, id);

    String sql =
        DEEP_SELECT
            + " FROM (post p JOIN users u ON p.author_id = u.user_id )"
            + " LEFT OUTER JOIN users w ON p.wall_id = w.user_id"
            + " WHERE p.post_id = :post_id";
    List<Post> posts = template.query(sql, params, postsMapper);
    if (posts != null && !posts.isEmpty()) {
      populateLikedBy(posts);
      foundPost = posts.get(0);
    }

    return foundPost;
  }

  @Override
  public List<Post> getPostsLikedByUser(User user) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("user_id", user.getId());

    String sql =
        DEEP_SELECT
            + " FROM (( SELECT post_id FROM likes WHERE user_id = :user_id) l"
            + " JOIN post p ON l.post_id = p.post_id "
            + " JOIN users u ON p.author_id = u.user_id )"
            + " LEFT OUTER JOIN users w ON p.wall_id = w.user_id";
    return template.query(sql, params, postsMapper);
  }

  private String generateOrderByFromParams(String sortBy, boolean asc) {
    String order = asc ? "ASC" : "DESC";

    String sortingExpression = ID; // default
    if (sortBy != null) {
      switch (sortBy) {
        case "message":
          sortingExpression = MESSAGE;
          break;
        case "user":
          sortingExpression = AUTHOR_ID;
          break;
        case "wall":
          sortingExpression = WALL_ID;
          break;
        case "publishingDate":
          sortingExpression = PUBLISHING_DATE;
          break;
        case "likes":
          sortingExpression = LIKES_COUNT;
          break;
        case "trending":
        case "trendingScore":
          sortingExpression = SCORE;
          break;
        default:
          break;
      }
    }
    return sortingExpression + " " + order;
  }

  private void populateLikedBy(List<Post> posts) {
    posts.parallelStream().forEach(this::populateLikedBy);
  }

  private void populateLikedBy(Post post) {
    Map<String, Object> params = new HashMap<String, Object>();
    String sql =
        String.format(
                "SELECT %s, %s, %s, %s",
                UserDao.ID, UserDao.USERNAME, UserDao.ABOUT, UserDao.HOBBIES)
            + " FROM (SELECT * FROM likes WHERE post_id = :post_id ORDER BY like_id DESC) l"
            + " NATURAL JOIN users"
            + " LIMIT 10";
    params.put(ID, post.getId());
    post.setRecentLikes(template.query(sql, params, likesMapper));
  }

  private RowMapper<User> likesMapper = (rs, rowNum) -> getUserFromResult(rs, "");

  private RowMapper<Post> postsMapper =
      (rs, rowNum) -> {
        Post post = new Post();
        post.setId(rs.getInt(ID));
        post.setMessage(rs.getString(MESSAGE));
        post.setPublishingDate(rs.getTimestamp(PUBLISHING_DATE));
        post.setLikesCount(rs.getInt(LIKES_COUNT));
        post.setTrendingScore(rs.getDouble(SCORE));

        post.setUser(getUserFromResult(rs, ""));
        post.setWall(getUserFromResult(rs, "wall_"));

        return post;
      };

  private User getUserFromResult(ResultSet rs, String prefix) throws SQLException {
    User user = new User();
    user.setId(rs.getInt(prefix + UserDao.ID));
    user.setUsername(rs.getString(prefix + UserDao.USERNAME));
    user.setAbout(rs.getString(prefix + UserDao.ABOUT));
    user.setHobbies(rs.getString(prefix + UserDao.HOBBIES));
    return user;
  }
}
