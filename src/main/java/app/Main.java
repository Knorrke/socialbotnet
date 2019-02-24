package app;

import config.WebConfig;
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
    new WebConfig(ctx.getBean(PostService.class), ctx.getBean(UserService.class));
    ctx.registerShutdownHook();
  }
}
