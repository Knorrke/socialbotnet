package modules.post.controller;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.UnauthorizedResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modules.error.InputTooLongException;
import modules.post.model.Post;
import modules.post.service.PostService;
import modules.user.model.User;
import modules.user.service.UserService;
import modules.util.EncodingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostController {
  static final Logger logger = LoggerFactory.getLogger(PostController.class);
  private static final Map<String, String> acceptedSorts = new HashMap<>();

  static {
    acceptedSorts.put("likes", "mostliked");
    acceptedSorts.put("trending", "trending");
    acceptedSorts.put("time", "recent");
  }

  private PostService postService;
  private UserService userService;

  public PostController(PostService postService, UserService userService) {
    this.postService = postService;
    this.userService = userService;
  }

  public void getPosts(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    User user = userService.getAuthenticatedUser(ctx);
    if (user != null) {
      model.put("authenticatedUser", user);
      model.put("postsLikedByUser", postService.getPostsLikedByUser(user));
    }

    String sortBy = ctx.queryParam("sortby");

    if (acceptedSorts.containsKey(sortBy)) {

      // explicit sort param
      List<Post> posts = postService.getWallPostsSorted(sortBy, false, 50);

      model.put(acceptedSorts.get(sortBy), posts);
      model.put("sortby", sortBy);
    } else {

      // fallback: 3 trending posts, then newest
      List<Post> trending = postService.getWallPostsSorted("trending", false, 3);
      List<Post> posts = postService.getWallPostsSorted(null, false, 50);

      model.put("trending", trending);
      model.put("recent", posts);
    }

    ctx.render("posts/wall.page.ftl", model);
  }

  public void likePost(Context ctx) {
    handleLikeAndUnlike(true, ctx);
  }

  public void unlikePost(Context ctx) {
    handleLikeAndUnlike(false, ctx);
  }

  private void handleLikeAndUnlike(boolean liked, Context ctx) {
    User authenticatedUser = userService.getAuthenticatedUser(ctx);
    if (authenticatedUser == null) {
      throw new UnauthorizedResponse("Du bist nicht angemeldet!");
    }

    Post post = postService.getPostById(ctx.formParamAsClass("post", Integer.class).get());
    if (post == null) {
      throw new NotFoundResponse("Post existiert nicht");
    }
    if (liked) {
      postService.likePost(post, authenticatedUser);
    } else {
      postService.unlikePost(post, authenticatedUser);
    }

    try {
      String referer = ctx.header("referer");
      if (referer != null) {
        String redirectPath = new URI(referer).getPath();
        if (redirectPath != null && (redirectPath.equals("") || redirectPath.equals("/"))) {
          ctx.redirect(String.format("/#post-%d", post.getId()));
          return;
        }
      }
    } catch (URISyntaxException e) {
      logger.warn("Invalid referer uri", e);
    }

    ctx.redirect(
        String.format(
            "/pinnwand/%s#post-%d",
            EncodingUtil.uriEncode(post.getWall().getUsername()), post.getId()));
  }

  public void createPost(Context ctx) {
    User authenticatedUser = userService.getAuthenticatedUser(ctx);
    if (authenticatedUser == null) {
      throw new UnauthorizedResponse("Du bist nicht angemeldet!");
    }

    Post post = new Post();
    post.setUser(authenticatedUser);
    post.setPublishingDate(new Timestamp(System.currentTimeMillis()));
    post.setMessage(ctx.formParam("message"));
    String username =
        ctx.pathParamMap().containsKey("username")
            ? ctx.pathParam("username")
            : authenticatedUser.getUsername();
    User wall = userService.getUserbyUsername(username);
    if (wall == null) {
      ctx.status(HttpCode.NOT_FOUND).result("User existiert nicht");
    } else {
      post.setWall(wall);
      ctx.redirect("/pinnwand/" + EncodingUtil.uriEncode(username));

      try {
        postService.addPost(post);
      } catch (InputTooLongException e) {
        logger.error(e.getMessage());
        throw new BadRequestResponse(e.getMessage());
      }
    }
  }

  /**
   * @return the acceptedsorts
   */
  public static Map<String, String> getAcceptedSorts() {
    return acceptedSorts;
  }
}
