package config;

import static io.javalin.apibuilder.ApiBuilder.before;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

import io.javalin.config.JavalinConfig;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpResponseException;
import io.javalin.http.HttpStatus;
import io.javalin.http.UnauthorizedResponse;
import modules.post.controller.PostApiController;
import modules.post.controller.PostController;
import modules.post.service.PostService;
import modules.user.controller.UserApiController;
import modules.user.controller.UserController;
import modules.user.model.User;
import modules.user.service.UserService;
import modules.util.EncodingUtil;
import org.eclipse.jetty.util.MultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Router {
  static final Logger logger = LoggerFactory.getLogger(Router.class);
  private UserController userController;
  private UserApiController userApiController;
  private PostController postController;
  private PostApiController postApiController;

  public Router(PostService postService, UserService userService) {
    this.userController = new UserController(userService, postService);
    this.postController = new PostController(postService, userService);
    this.userApiController = new UserApiController(userService);
    this.postApiController = new PostApiController(postService, userService);
  }

  public void setupRoutes(JavalinConfig config) {
    config.router.apiBuilder(
        () -> {
          get("/registrieren", userController::register);
          post("/registrieren", userController::register);

          get("/login", userController::login);
          post("/login", userController::login);

          get("/logout", userController::logout);
          post("/logout", userController::logout);

          get("/user/profile/{username}", userController::showProfile);
          get("/user/update", userController::updateProfile);
          post("/user/update", userController::updateProfile);
          get("/user/change-password", userController::changePassword);
          post("/user/change-password", userController::changePassword);

          get("/", postController::getPosts);
          get("/pinnwand/{username}", userController::showProfile);

          post("/post", postController::createPost);
          post("/post/{username}", postController::createPost);

          post("/like", postController::likePost);
          post("/unlike", postController::unlikePost);

          get("/material", ctx -> ctx.render("meta/material.ftl"));
          get("/didaktik", ctx -> ctx.render("meta/didactic.ftl"));
          get("/impressum", ctx -> ctx.render("meta/impress.ftl"));

          path(
              "/api",
              () -> {
                get("", ctx -> ctx.redirect("/docs/index.html"));
                before("/*", ctx -> logger.debug("Received api call to path {}", ctx.path()));

                before("/*", this::checkCorrectRequestType);
                before(
                    "/*",
                    ctx -> {
                      if (ctx.method() == HandlerType.POST) {
                        MultiMap<String> params = EncodingUtil.decode(ctx);
                        String username = params.getString("username");
                        String password = params.getString("password");

                        if (password == null || username == null) {
                          ctx.status(HttpStatus.UNAUTHORIZED);
                          throw new UnauthorizedResponse(
                              "Du bist nicht authentifiziert! Bitte schicke deinen Nutzernamen (username) und dein Passwort (password) mit.");
                        }

                        User authenticated = userApiController.login(ctx);

                        if (authenticated == null) {
                          ctx.status(HttpStatus.UNAUTHORIZED);
                          throw new UnauthorizedResponse(
                              "Login fehlgeschlagen. Falscher Nutzername oder falsches Passwort.");
                        }

                        logger.debug("Authentication successfull");
                      }
                    });

                get("/users", userApiController::getUsers);
                get("/user/{userid}", userApiController::getUserById);
                get("/posts", postApiController::getPosts);
                get("/post/{postid}", postApiController::getPostById);
                get("/pinnwand/{username}", postApiController::getUserPosts);

                post("/user/update", userApiController::updateProfile);

                post("/post", postApiController::createPost);
                post("/post/{username}", postApiController::createPost);
                post("/like", postApiController::likePost);
                post("/unlike", postApiController::unlikePost);
              });
        });
  }

  private boolean checkCorrectRequestType(Context ctx) throws HttpResponseException {
    HandlerType requestMethod = ctx.method();
    String path = ctx.path();

    String[] routesGETRegex = {
      "^/api/users$",
      "^/api/user/[1-9]\\d*$",
      "^/api/posts$",
      "^/api/pinnwand/.*$",
      "^/api/post/[1-9]\\d*$"
    };
    String[] routesPOSTRegex = {
      "^/api/post$", "^/api/post/.*[^0-9].*$", "^/api/like$", "^/api/unlike$", "^/api/user/update$",
    };

    if (requestMethod != HandlerType.POST) {
      for (String routePOST : routesPOSTRegex) {
        if (path.matches(routePOST)) {
          ctx.status(HttpStatus.BAD_REQUEST);
          throw new BadRequestResponse(
              String.format(
                  "Falscher Anfragemodus. Erwartet wurde %s, erhalten wurde %s",
                  "POST", requestMethod));
        }
      }
    }

    if (requestMethod != HandlerType.GET) {
      for (String routeGET : routesGETRegex) {
        if (path.matches(routeGET)) {
          ctx.status(HttpStatus.BAD_REQUEST);
          throw new BadRequestResponse(
              String.format(
                  "Falscher Anfragemodus. Erwartet wurde %s, erhalten wurde %s",
                  "GET", requestMethod));
        }
      }
    }

    return true;
  }
}
