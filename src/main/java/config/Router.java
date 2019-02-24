package config;

import static spark.Spark.*;

import modules.post.controller.PostApiController;
import modules.post.controller.PostController;
import modules.post.service.PostService;
import modules.user.controller.UserApiController;
import modules.user.controller.UserController;
import modules.user.model.User;
import modules.user.service.UserService;
import modules.util.JSONUtil;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Spark;

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

  public void setupRoutes() {
    get("/registrieren", userController::register);
    post("/registrieren", userController::register);

    get("/login", userController::login);
    post("/login", userController::login);

    get("/logout", userController::logout);
    post("/logout", userController::logout);

    get("/user/profile/:username", userController::showProfile);
    get("/user/update", userController::updateProfile);
    post("/user/update", userController::updateProfile);

    get("/", postController::getPosts);
    get("/pinnwand/:username", userController::showProfile);

    post("/post", postController::createPost);
    post("/post/:username", postController::createPost);

    get("like/:post", postController::likePost);
    post("like/:post", postController::likePost);
    get("unlike/:post", postController::unlikePost);
    post("unlike/:post", postController::unlikePost);

    path(
        "/api",
        () -> {
          before("/*", (req, res) -> logger.info("Received api call to " + req.pathInfo()));
          before("/*", (req, res) -> checkCorrectRequestType(req));
          before(
              "/*",
              (req, res) -> {
                if (req.requestMethod().equals("POST")) {
                  MultiMap<String> params = new MultiMap<String>();
                  UrlEncoded.decodeTo(req.body(), params, "UTF-8");
                  String username = params.getString("username");
                  String password = params.getString("password");

                  if (password == null || username == null) {
                    halt(
                        401,
                        "Du bist nicht authentifiziert! Bitte schicke deinen Nutzernamen (username) und dein Passwort (password) mit.");
                  }

                  User authenticated = userApiController.login(req, res);

                  if (authenticated == null) {
                    Spark.halt(
                        401, "Login fehlgeschlagen. Falscher Nutzername oder falsches Passwort.");
                  }

                  logger.info("Authentication successfull");
                }
              });

          get("/users", userApiController::getUsers, JSONUtil::jsonify);
          get("/posts", postApiController::getPosts, JSONUtil::jsonify);
          get("/pinnwand/:username", postApiController::getUserPosts, JSONUtil::jsonify);

          post("/user/update", userApiController::updateProfile, JSONUtil::jsonify);

          post("/post", postApiController::createPost, JSONUtil::jsonify);
          post("/post/:username", postApiController::createPost, JSONUtil::jsonify);
          post("/like", postApiController::likePost, JSONUtil::jsonify);
          post("/unlike", postApiController::unlikePost, JSONUtil::jsonify);

          after(
              (req, res) -> {
                res.type("application/json");
              });
        });
  }

  private boolean checkCorrectRequestType(Request req) {
    String requestMethod = req.requestMethod();
    String path = req.pathInfo();

    String[] routesGETRegex = {"/api/users", "/api/posts", "/api/pinnwand/.*"};
    String[] routesPOSTRegex = {"/api/post", "/api/post/.*", "/api/like", "/api/unlike"};

    if (!requestMethod.equals("POST")) {
      for (String routePOST : routesPOSTRegex) {
        if (path.matches(routePOST)) {
          halt(400, "Falscher Anfragemodus. Erwartet wurde POST, erhalten wurde " + requestMethod);
          return false;
        }
      }
    }

    if (!requestMethod.equals("GET")) {
      for (String routeGET : routesGETRegex) {
        if (path.matches(routeGET)) {
          halt(400, "Falscher Anfragemodus. Erwartet wurde GET, erhalten wurde " + requestMethod);
          return false;
        }
      }
    }

    return true;
  }
}
