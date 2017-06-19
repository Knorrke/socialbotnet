package app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import config.WebConfig;
import modules.post.service.PostService;
import modules.user.service.UserService;

@Configuration
@ComponentScan({ "config", "modules", "service" })
public class Main {
    public static void main(String[] args) {
    	AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Main.class);
    	new WebConfig(ctx.getBean(PostService.class), ctx.getBean(UserService.class));
    	ctx.registerShutdownHook();
    }
}
