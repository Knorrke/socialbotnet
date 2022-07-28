package base;

import config.FreeMarkerEngineConfig;
import config.Router;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.template.JavalinFreemarker;
import io.zonky.test.db.postgres.embedded.FlywayPreparer;
import io.zonky.test.db.postgres.junit5.EmbeddedPostgresExtension;
import io.zonky.test.db.postgres.junit5.PreparedDbExtension;
import javax.sql.DataSource;
import modules.post.service.PostService;
import modules.user.service.UserService;
import modules.util.JSONUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan({"modules", "base"})
public abstract class IntegrationTest {
  protected static final Logger logger = LoggerFactory.getLogger(IntegrationTest.class);
  protected Javalin app;

  @RegisterExtension
  public static PreparedDbExtension pg =
      EmbeddedPostgresExtension.preparedDatabase(
          FlywayPreparer.forClasspathLocation("db/migration", "db/test-migration"));

  @Bean
  @Primary
  public DataSource dataSource() throws Exception {
    return pg.getTestDatabase();
  }

  @BeforeEach
  public void setup() {
    try (AnnotationConfigApplicationContext ctx =
        new AnnotationConfigApplicationContext(IntegrationTest.class)) {
      app =
          Javalin.create(
              config -> {
                config.addStaticFiles("/public", Location.CLASSPATH);
                config.jsonMapper(JSONUtil.create());
              });

      JavalinFreemarker.configure(FreeMarkerEngineConfig.getConfig());
      new Router(app, ctx.getBean(PostService.class), ctx.getBean(UserService.class)).setupRoutes();
      ctx.registerShutdownHook();
    } catch (BeansException e) {
      logger.error("Annotation context couldn't be created. {}", e);
    }
  }
}
