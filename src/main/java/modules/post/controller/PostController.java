package modules.post.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modules.post.model.Post;
import modules.post.service.PostService;
import modules.user.model.User;
import modules.user.service.UserService;
import modules.util.EncodingUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;
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
      ctx.status(HttpCode.UNAUTHORIZED).result("Du bist nicht angemeldet!");
      return;
    }

    MultiMap<String> params = EncodingUtil.decode(ctx);
    Post post = postService.getPostById(Integer.parseInt(params.getString("post")));
    if (post == null) {
      ctx.status(HttpCode.UNAUTHORIZED).result("Post existiert nicht");
      return;
    }
    if (liked) {
      postService.likePost(post, authenticatedUser);
    } else {
      postService.unlikePost(post, authenticatedUser);
    }

    String defaultRedirectPath = "/pinnwand/" + EncodingUtil.uriEncode(post.getUsername());
    try {
      String referer = ctx.header("referer");
      if (referer != null && new URI(referer).getPath() != null) {
        String redirectPath = new URI(referer).getPath();
        if (redirectPath.equals("/") || redirectPath.equals(defaultRedirectPath)) {
          ctx.redirect(
              String.format("%s#post-%d", redirectPath, post.getId()),
              HttpCode.SEE_OTHER.getStatus());
          return;
        }
      }
    } catch (URISyntaxException e) {
    }

    ctx.redirect(
        String.format("%s#post-%d", defaultRedirectPath, post.getId()),
        HttpCode.SEE_OTHER.getStatus());
  }

  public void createPost(Context ctx) {
    User authenticatedUser = userService.getAuthenticatedUser(ctx);
    if (authenticatedUser == null) {
      ctx.status(HttpCode.BAD_REQUEST).result("Du bist nicht angemeldet!");
      return;
    }

    Post post = new Post();
    post.setUser(authenticatedUser);
    post.setPublishingDate(new Timestamp(System.currentTimeMillis()));
    try { // populate post attributes by params
      BeanUtils.populate(post, EncodingUtil.decode(ctx));
      String username =
          ctx.pathParamMap().containsKey("username")
              ? ctx.pathParam("username")
              : authenticatedUser.getUsername();
      post.setWall(userService.getUserbyUsername(username));
      ctx.redirect("/pinnwand/" + EncodingUtil.uriEncode(username));

      postService.addPost(post);
    } catch (Exception e) {
      logger.error(e.getMessage());
      ctx.status(HttpCode.INTERNAL_SERVER_ERROR).result(HttpCode.BAD_REQUEST.getMessage());
      return;
    }
  }

  /** @return the acceptedsorts */
  public static Map<String, String> getAcceptedSorts() {
    return acceptedSorts;
  }
}
