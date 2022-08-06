package modules.post.controller;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import java.sql.Timestamp;
import modules.error.InputTooLongException;
import modules.post.model.Post;
import modules.post.service.PostService;
import modules.user.model.User;
import modules.user.service.UserService;
import modules.util.EncodingUtil;
import org.eclipse.jetty.util.MultiMap;

public class PostApiController {
  private PostService postService;
  private UserService userService;

  public PostApiController(PostService postService, UserService userService) {
    this.postService = postService;
    this.userService = userService;
  }

  public void getPostById(Context ctx) {
    ctx.json(postService.getPostById(ctx.pathParamAsClass("postid", Integer.class).get()));
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
  public void getPosts(Context ctx) {
    int limit = getLimitParam(ctx);
    String sortby = getSortByParam(ctx);
    boolean asc = getAscendingParam(ctx);
    ctx.json(postService.getWallPostsSorted(sortby, asc, limit));
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
  public void getUserPosts(Context ctx) {
    String username = ctx.pathParam("username");
    User profileUser = userService.getUserbyUsername(username);

    if (profileUser == null) {
      throw new NotFoundResponse(String.format("Der User %s existiert nicht", username));
    }

    int limit = getLimitParam(ctx);
    String sortby = getSortByParam(ctx);
    boolean asc = getAscendingParam(ctx);
    ctx.json(postService.getUserWallPostsSorted(profileUser, sortby, asc, limit));
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
  public void likePost(Context ctx) {
    handleLikeAndUnlike(true, ctx);
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
  public void unlikePost(Context ctx) {
    handleLikeAndUnlike(false, ctx);
  }

  private void handleLikeAndUnlike(boolean liked, Context ctx) {
    MultiMap<String> params = EncodingUtil.decode(ctx);

    User authenticatedUser = userService.getUserbyUsername(params.getString("username"));
    if (!params.containsKey("postid")) {
      throw new BadRequestResponse("Parameter postid fehlt in der Anfrage.");
    }
    int id = Integer.parseInt(params.getString("postid"));
    Post post = postService.getPostById(id);
    if (post == null) {
      throw new NotFoundResponse(String.format("Der Post mit id %d existiert nicht", id));
    }

    if (liked) {
      postService.likePost(post, authenticatedUser);
    } else {
      postService.unlikePost(post, authenticatedUser);
    }

    ctx.json(post);
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
  public void createPost(Context ctx) {
    Post post = new Post();
    post.setPublishingDate(new Timestamp(System.currentTimeMillis()));
    MultiMap<String> params = EncodingUtil.decode(ctx);
    if (!params.containsKey("message")) {
      throw new BadRequestResponse("Parameter message fehlt in der Anfrage.");
    }
    post.setMessage(params.getString("message"));

    User authenticatedUser = userService.getUserbyUsername(params.getString("username"));
    post.setUser(authenticatedUser);

    String username =
        ctx.pathParamMap().containsKey("username")
            ? ctx.pathParam("username")
            : authenticatedUser.getUsername();

    post.setWall(userService.getUserbyUsername(username));

    try {
      postService.addPost(post);
    } catch (InputTooLongException e) {
      throw new BadRequestResponse(e.getMessage());
    }
    ctx.json(post);
  }

  private int getLimitParam(Context ctx) {
    return ctx.queryParamAsClass("limit", Integer.class).getOrDefault(50);
  }

  private String getSortByParam(Context ctx) {
    return ctx.queryParamAsClass("sortby", String.class).getOrDefault("id").toLowerCase();
  }

  private boolean getAscendingParam(Context ctx) {
    String order = ctx.queryParamAsClass("order", String.class).getOrDefault("desc");
    return order.equalsIgnoreCase("asc");
  }
}
