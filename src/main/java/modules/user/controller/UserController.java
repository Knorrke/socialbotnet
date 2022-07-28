package modules.user.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modules.error.InputTooLongException;
import modules.post.controller.PostController;
import modules.post.model.Post;
import modules.post.service.PostService;
import modules.user.model.User;
import modules.user.service.UserService;
import modules.util.EncodingUtil;
import org.eclipse.jetty.util.MultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserController {

  static final Logger logger = LoggerFactory.getLogger(UserController.class);
  private UserService userService;
  private PostService postService;

  public UserController(UserService userService, PostService postService) {
    this.userService = userService;
    this.postService = postService;
  }

  public void login(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    if (ctx.method().equals("POST")) {
      User user = new User();

      MultiMap<String> params = EncodingUtil.decode(ctx);
      user.setUsername(params.getString("username"));
      user.setPassword(params.getString("password"));

      User authenticated = userService.checkUser(user);
      if (authenticated != null) {
        userService.addAuthenticatedUser(ctx, authenticated);
        ctx.redirect("/");
        model.put("username", user.getUsername());
        model.put("success", "Login erfolgreich. Herzlich Willkommen! :-)");
        return;
      } else {
        model.put("error", "Login fehlgeschlagen.");
      }
    }

    ctx.render("user/login.ftl", model);
  }

  public void logout(Context ctx) {
    userService.removeAuthenticatedUser(ctx);
    ctx.redirect("/");
  }

  public void register(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    if (ctx.method().equals("POST")) {
      User user = new User();
      try { // populate user attributes by registration params
        MultiMap<String> params = EncodingUtil.decode(ctx);
        if (params.get("password").equals(params.get("password2"))) {
          logger.debug("registration passwords match");
          user.setPassword(params.getString("password"));
          user.setUsername(params.getString("username"));
          userService.registerUser(user);
          logger.debug("registration succeeded");
          userService.addAuthenticatedUser(ctx, user);
          ctx.redirect("/");
          model.put("username", user.getUsername());
          model.put("success", "Registrierung erfolgreich");
          return;
        } else {
          model.put("error", "Passwörter stimmen nicht überein");
        }
      } catch (InputTooLongException e) {
        model.put("error", e.getMessage());
      } catch (Exception e) {
        ctx.status(HttpCode.INTERNAL_SERVER_ERROR)
            .result("Bei der Registrierung ist ein Fehler aufgetreten");
        logger.error("Internal server error on registration");
        return;
      }
    }

    ctx.render("user/register.ftl", model);
  }

  public void showProfile(Context ctx) {
    Map<String, Object> model = new HashMap<>();

    String username = ctx.pathParam("username");
    User profileUser = userService.getUserbyUsername(username);
    if (profileUser == null) {
      ctx.status(HttpCode.BAD_REQUEST).result("User unbekannt");
      return;
    }
    model.put("user", profileUser);

    User authenticatedUser = userService.getAuthenticatedUser(ctx);
    if (authenticatedUser != null) {
      model.put("authenticatedUser", authenticatedUser);
      model.put("postsLikedByUser", postService.getPostsLikedByUser(authenticatedUser));
    }

    String sortBy = ctx.queryParam("sortby");
    Map<String, String> acceptedSorts = PostController.getAcceptedSorts();

    List<Post> posts = postService.getUserWallPostsSorted(profileUser, sortBy, false, 50);
    model.put(acceptedSorts.getOrDefault(sortBy, "recent"), posts);
    model.put("sortby", sortBy);

    ctx.render("user/profile.ftl", model);
  }

  public void updateProfile(Context ctx) {
    User authenticatedUser = userService.getAuthenticatedUser(ctx);
    if (authenticatedUser == null) {
      ctx.status(HttpCode.UNAUTHORIZED).result("Du bist nicht angemeldet!");
      return;
    }
    Map<String, Object> model = new HashMap<>();
    if (ctx.method().equals("POST")) {
      User user = new User();
      MultiMap<String> params = EncodingUtil.decode(ctx);
      user.setUsername(params.getString("username"));
      user.setAbout(params.getString("about"));
      user.setHobbies(params.getString("hobbies"));
      try {
        userService.updateUser(authenticatedUser, user);
        userService.addAuthenticatedUser(ctx, user);
        model.put("success", "Profil erfolgreich aktualisiert");
        ctx.redirect("/user/profile/" + EncodingUtil.uriEncode(user.getUsername()));
        return;
      } catch (InputTooLongException e) {
        model.put("error", e.getMessage());
      }
    }

    model.put("authenticatedUser", authenticatedUser);
    ctx.render("user/updateProfile.ftl", model);
  }
}
