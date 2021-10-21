package modules.post.controller;

import java.sql.Timestamp;
import modules.error.InputTooLongException;
import modules.error.ResponseError;
import modules.post.model.Post;
import modules.post.service.PostService;
import modules.user.model.User;
import modules.user.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import spark.Request;
import spark.Response;

public class PostApiController {
  private PostService postService;
  private UserService userService;

  public PostApiController(PostService postService, UserService userService) {
    this.postService = postService;
    this.userService = userService;
  }

  /**
   * @api {get} /api/posts Übersicht aller Posts
   * @apiDescription Liefert die Posts.
   *     <p>Sie können nach Likes, Datum oder Trend sortiert werden. Die Anzahl kann mit dem
   *     Parameter limit verändert werden
   * @apiGroup Posts — GET
   * @apiQuery {String="likes","time","trending"} [sortby] Sortierung
   * @apiQuery {Number} [limit=50] Limit der angezeigten Posts.
   * @apiSampleRequest /api/posts
   * @apiSuccessExample {json} Beispiel: Aufruf von /api/posts?sortby=likes&limit=1 HTTP/1.1 200 OK
   *     [ { "id": 3, "message": "This is a post written by user user001 to user004", "user": {
   *     "id": 1, "username": "user001", "hobbies": "", "about": "" }, "wall": { "id": 4,
   *     "username": "user004", "hobbies": "", "about": "" }, "publishingDate": "2014-07-14
   *     09:46:28", "likedBy": [ { "id": 1, "username": "user001", "hobbies": "", "about": "" }, {
   *     "id": 5, "username": "user005", "hobbies": "", "about": "" }, { "id": 6, "username":
   *     "user006", "hobbies": "", "about": "" }, { "id": 7, "username": "user007", "hobbies": "",
   *     "about": "" } ] } ]
   */
  public Object getPosts(Request req, Response res) {
    int limit = req.queryParams("limit") != null ? Integer.parseInt(req.queryParams("limit")) : 50;
    String sortby = req.queryParams("sortby");
    if (sortby != null && sortby.equals("likes")) {
      return postService.getMostLikedWallPosts(limit);
    } else if (sortby != null && sortby.equals("trending")) {
      return postService.getTrendingWallPosts(limit);
    } else {
      return postService.getLatestWallPosts(limit);
    }
  }

  public Object getUserPosts(Request req, Response res) {
    String username = req.params("username");
    User profileUser = userService.getUserbyUsername(username);

    if (profileUser == null) {
      res.status(400);
      return new ResponseError("Der User %s existiert nicht", username);
    }

    int limit = req.queryParams("limit") != null ? Integer.parseInt(req.queryParams("limit")) : 50;
    String sortby = req.queryParams("sortby");
    if (sortby != null && sortby.equals("likes")) {
      return postService.getMostLikedUserWallPosts(profileUser, limit);
    } else if (sortby != null && sortby.equals("trending")) {
      return postService.getTrendingUserWallPosts(profileUser, limit);
    } else {
      return postService.getLatestUserWallPosts(profileUser, limit);
    }
  }

  public Object likePost(Request req, Response res) {
    MultiMap<String> params = new MultiMap<String>();
    UrlEncoded.decodeTo(req.body(), params, "UTF-8");

    User authenticatedUser = userService.getUserbyUsername(params.getString("username"));
    if (!params.containsKey("postid")) {
      res.status(400);
      return new ResponseError("Parameter postid fehlt in der Anfrage.");
    }
    int id = Integer.parseInt(params.getString("postid"));
    Post post = postService.getPostById(id);
    if (post == null) {
      res.status(400);
      return new ResponseError("Der Post mit id %s existiert nicht", id);
    }
    postService.likePost(post, authenticatedUser);
    return post;
  }

  public Object unlikePost(Request req, Response res) {
    MultiMap<String> params = new MultiMap<String>();
    UrlEncoded.decodeTo(req.body(), params, "UTF-8");

    User authenticatedUser = userService.getUserbyUsername(params.getString("username"));
    if (!params.containsKey("postid")) {
      res.status(400);
      return new ResponseError("Parameter postid fehlt in der Anfrage.");
    }
    int id = Integer.parseInt(params.getString("postid"));
    Post post = postService.getPostById(id);
    if (post == null) {
      res.status(400);
      return new ResponseError("Der Post mit id %s existiert nicht", id);
    }

    postService.unlikePost(post, authenticatedUser);
    return post;
  }

  public Object createPost(Request req, Response res) {
    Post post = new Post();
    post.setPublishingDate(new Timestamp(System.currentTimeMillis()));
    try { // populate post attributes by params
      MultiMap<String> params = new MultiMap<String>();
      UrlEncoded.decodeTo(req.body(), params, "UTF-8");
      if (!params.containsKey("message")) {
        res.status(400);
        return new ResponseError("Parameter message fehlt in der Anfrage.");
      }
      BeanUtils.populate(post, params);

      User authenticatedUser = userService.getUserbyUsername(params.getString("username"));
      post.setUser(authenticatedUser);

      String username = req.params("username");
      if (username != null) {
        post.setWall(userService.getUserbyUsername(username));
      } else {
        post.setWall(authenticatedUser);
      }
    } catch (Exception e) {
      res.status(500);
      return new ResponseError("Interner Fehler aufgetreten. Bitte melde das Problem!");
    }

    try {
      postService.addPost(post);
    } catch (InputTooLongException e) {
      res.status(400);
      return new ResponseError(e);
    }
    return post;
  }
}
