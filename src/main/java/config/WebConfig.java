package config;

import io.javalin.Javalin;
import io.javalin.http.Header;
import io.javalin.http.HttpResponseException;
import io.javalin.http.HttpStatus;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinFreemarker;
import java.util.HashMap;
import java.util.Map;
import modules.error.ResponseError;
import modules.post.service.PostService;
import modules.user.service.UserService;
import modules.util.EncodingUtil;
import modules.util.JSONUtil;

public class WebConfig {
  public static Javalin configure(PostService postService, UserService userService) {
    Javalin app =
        Javalin.create(
            config -> {
              new Router(postService, userService).setupRoutes(config);
              config.staticFiles.add(
                  staticFiles -> {
                    staticFiles.directory = "/public";
                    staticFiles.location = Location.CLASSPATH;
                    staticFiles.headers =
                        Map.of(
                            Header.CACHE_CONTROL,
                            "max-age=31622400",
                            Header.CONTENT_ENCODING,
                            EncodingUtil.ENCODING);
                  });
              config.jsonMapper(JSONUtil.create());
              config.fileRenderer(new JavalinFreemarker(FreeMarkerEngineConfig.getConfig()));
            });
    // Activate CORS
    app.options(
        "/*",
        ctx -> {
          String accessControlRequestHeaders = ctx.header("Access-Control-Request-Headers");
          if (accessControlRequestHeaders != null) {
            ctx.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
          }

          String accessControlRequestMethod = ctx.header("Access-Control-Request-Method");
          if (accessControlRequestMethod != null) {
            ctx.header("Access-Control-Allow-Methods", accessControlRequestMethod);
          }
        });
    app.before(
        "/*",
        ctx -> {
          ctx.header("Access-Control-Allow-Origin", "*");
          ctx.header("Access-Control-Allow-Headers", "*");
        });

    app.exception(
        HttpResponseException.class,
        (e, ctx) -> {
          if (ctx.path().startsWith("/api/")) {
            ctx.status(e.getStatus()).json(new ResponseError(e.getMessage()));
          } else {
            Map<String, Object> model = new HashMap<>();
            model.put("error", e);
            model.put("defaultMessage", HttpStatus.forStatus(e.getStatus()).getMessage());
            ctx.status(e.getStatus()).render("error/error.ftl", model);
          }
        });

    return app;
  }
}
