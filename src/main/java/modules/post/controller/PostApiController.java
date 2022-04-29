package modules.post.controller;

import java.sql.Timestamp;
import java.util.List;
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
   *
   * Sie können nach Likes, Datum oder Trend sortiert werden. Die Anzahl kann mit dem
   * Parameter limit verändert werden
   * @apiGroup Posts — GET
   * @apiQuery {String="likes", "trending", "id", "message", "user", "wall", "publishingDate"} [sortby=id] Sortierung
   * @apiQuery {String="asc","desc"} [order=desc] Aufsteigende oder absteigende Sortierung
   * @apiQuery {Number} [limit=50] Limit der angezeigten Posts.
   * @apiSampleRequest /api/posts
   * @apiComment <pre>
   * @apiSuccessExample {json} Beispiel: Aufruf von /api/posts?sortby=likes&limit=1
   * HTTP/1.1 200 OK
   * [
   *   {
   *     "id": 3,
   *     "message": "This is a post written by user user001 to user004",
   *     "user": { "id": 1, "username": "user001", "hobbies": "", "about": "" },
   *     "wall": { "id": 4, "username": "user004", "hobbies": "", "about": "" },
   *     "publishingDate": "2014-07-14 09:46:28",
   *     "likedBy": [
   *       { "id": 1, "username": "user001", "hobbies": "", "about": "" },
   *       { "id": 5, "username": "user005", "hobbies": "", "about": "" },
   *       { "id": 6, "username": "user006", "hobbies": "", "about": "" },
   *       { "id": 7, "username": "user007", "hobbies": "", "about": "" }
   *     ]
   *   }
   * ]
   * @apiComment </pre>
   */
  public List<Post> getPosts(Request req, Response res) {
    int limit = getLimitParam(req);
    String sortby = getSortByParam(req);
    boolean asc = getAscendingParam(req);
    if (sortby.equals("likes")) {
      return postService.getWallPostsSortedByLikes(asc, limit);
    } else if (sortby.equals("trending")) {
      return postService.getTrendingWallPosts(asc, limit);
    } else {
      return postService.getWallPostsSorted(sortby, asc, limit);
    }
  }

  /**
   * @api {get} /api/pinnwand/:username Pinnwand eines Users
   * @apiParam (URL Parameter) {String} username Der Benutzername eines Users
   * @apiDescription Liefert alle Posts der Pinnwand eines Users. Das sind zum einen Posts <strong>von diesem User</strong>
   * und zum anderen Posts, die <strong>von anderen an diesen User</strong> geschrieben wurden.
   *
   * Sie können nach Likes, Datum oder Trend sortiert werden. Die Anzahl kann mit dem Parameter limit verändert werden.
   * @apiGroup Posts — GET
   * @apiQuery {String="likes", "trending", "id", "message", "user", "wall", "publishingDate"} [sortby=id] Sortierung
   * @apiQuery {String="asc","desc"} [order=desc] Aufsteigende oder absteigende Sortierung
   * @apiQuery {Number} [limit=50] Limit der angezeigten Posts.
   * @apiSampleRequest /api/pinnwand/:username
   * @apiComment <pre>
   * @apiSuccessExample {json} Beispiel: Aufruf von /api/pinnwand/root?limit=2
   * HTTP/1.1 200 OK
   * [
   *   {
   *     "id": 1,
   *     "message": "Hallo, Welt!",
   *     "user": { "id": 1, "username": "root", "hobbies": "Netzwerken", "about": "I am root!" },
   *     "wall": { "id": 1, "username": "root", "hobbies": "Netzwerken", "about": "I am root!" },
   *     "publishingDate": "2019-03-17 19:54:48",
   *     "likedBy": []
   *   },
   *   {
   *     "id": 3,
   *     "message": "Hallo root, herzlich Willkommen!",
   *     "user": { "id": 2, "username": "Welcome", "hobbies": "", "about": "" },
   *     "wall": { "id": 1, "username": "root", "hobbies": "Netzwerken", "about": "I am root!" },
   *     "publishingDate": "2019-03-18 08:55:41",
   *     "likedBy": []
   *   }
   * ]
   * @apiComment </pre>
   */
  public Object getUserPosts(Request req, Response res) {
    String username = req.params("username");
    User profileUser = userService.getUserbyUsername(username);

    if (profileUser == null) {
      res.status(400);
      return new ResponseError("Der User %s existiert nicht", username);
    }

    int limit = getLimitParam(req);
    String sortby = getSortByParam(req);
    boolean asc = getAscendingParam(req);
    if (sortby.equals("likes")) {
      return postService.getUserWallPostsSortedByLikes(profileUser, asc, limit);
    } else if (sortby.equals("trending")) {
      return postService.getTrendingUserWallPosts(profileUser, asc, limit);
    } else {
      return postService.getUserWallPostsSorted(profileUser, sortby, asc, limit);
    }
  }

  /**
   * @api {post} /api/like Beitrag liken
   * @apiDescription Like einen Beitrag mit der angegebenen postid
   * @apiGroup Posts — POST
   * @apiBody (Anmeldedaten) {String} username Eigener Benutzername
   * @apiBody (Anmeldedaten) {String} password Eigenes Passwort
   * @apiBody (Post) {Number} postid Die id des Posts, der geliket werden soll.
   * @apiSampleRequest /api/like
   */
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

  /**
   * @api {post} /api/unlike Beitrag entliken
   * @apiDescription Entferne den Like eines Beitrag mit der angegebenen postid
   * @apiGroup Posts — POST
   * @apiBody (Anmeldedaten) {String} username Eigener Benutzername
   * @apiBody (Anmeldedaten) {String} password Eigenes Passwort
   * @apiBody (Post) {Number} postid Die id des Posts, dessen Like entfernt werden soll.
   * @apiSampleRequest /api/unlike
   */
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

  /**
   * @api {post} /api/post Beitrag schreiben
   * @apiDescription Poste einen Beitrag mit der angegebenen Nachricht (auf deiner eigenen Pinnwand)
   * @apiGroup Posts — POST
   * @apiBody (Anmeldedaten) {String} username Eigener Benutzername
   * @apiBody (Anmeldedaten) {String} password Eigenes Passwort
   * @apiBody (Post) {String} message Die Nachricht, die gepostet werden soll.
   * @apiSampleRequest /api/post
   */

  /**
   * @api {post} /api/post/:username Beitrag an bestimmten User schreiben
   * @apiDescription Poste einen Beitrag mit der angegebenen Nachricht an die Pinnwand eines
   *     bestimmten Users.
   * @apiGroup Posts — POST
   * @apiParam (URL Parameter) {String} username Der Benutzername, an den die Nachricht geschrieben
   *     wird.
   * @apiBody (Anmeldedaten) {String} username Eigener Benutzername
   * @apiBody (Anmeldedaten) {String} password Eigenes Passwort
   * @apiBody (Post) {String} message Die Nachricht, die gepostet werden soll.
   * @apiSampleRequest /api/post/:username
   */
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

  private int getLimitParam(Request req) {
    return Integer.parseInt(req.queryParamOrDefault("limit", "50"));
  }

  private String getSortByParam(Request req) {
    return req.queryParamOrDefault("sortby", "id").toLowerCase();
  }

  private boolean getAscendingParam(Request req) {
    return req.queryParamOrDefault("order", "desc").equalsIgnoreCase("asc");
  }
}
