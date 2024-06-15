package app;

import config.WebConfig;
import io.javalin.Javalin;
import modules.post.service.PostService;
import modules.user.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"config", "modules", "service"})
public class Main {
  public static void main(String[] args) {
    @SuppressWarnings("resource")
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Main.class);
    Javalin app =
        WebConfig.configure(ctx.getBean(PostService.class), ctx.getBean(UserService.class));
    ctx.registerShutdownHook();
    app.start(getAssignedPort());
  }

  static int getAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 30003; // return default port if heroku-port isn't set (i.e. on localhost)
  }
}
