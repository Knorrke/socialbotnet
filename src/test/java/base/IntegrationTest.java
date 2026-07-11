package base;

import config.WebConfig;
import io.javalin.Javalin;
import io.javalin.testtools.HttpClient;
import io.javalin.testtools.Request;
import io.javalin.testtools.Response;
import io.zonky.test.db.postgres.embedded.FlywayPreparer;
import io.zonky.test.db.postgres.junit5.EmbeddedPostgresExtension;
import io.zonky.test.db.postgres.junit5.PreparedDbExtension;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.function.Consumer;
import javax.sql.DataSource;
import modules.post.service.PostService;
import modules.user.service.UserService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
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
      app = WebConfig.configure(ctx.getBean(PostService.class), ctx.getBean(UserService.class));
      ctx.registerShutdownHook();
    } catch (BeansException e) {
      logger.error("Annotation context couldn't be created. {}", e);
    }
  }

  @AfterEach
  public void cleanDB() throws Exception {
    Flyway flyway =
        Flyway.configure()
            .cleanDisabled(false)
            .dataSource(dataSource())
            .locations("db/migration", "db/test-migration")
            .load();
    flyway.clean();
    flyway.migrate();
  }

  protected Response login(HttpClient client, String username) {
    return postWithUrlEncodedBody(
        client, "/login", String.format("username=%s&password=test", username));
  }

  protected Response login(HttpClient client, String username, String password) {
    return postWithUrlEncodedBody(
        client, "/login", String.format("username=%s&password=%s", username, password));
  }

  protected Response postWithUrlEncodedBody(HttpClient client, String path, String body) {
    return postWithUrlEncodedBody(client, path, body, null);
  }

  protected Response postWithUrlEncodedBody(
      HttpClient client, String path, String body, Consumer<Request.Builder> consumer) {
    return client.request(
        path,
        req -> {
          req.post(BodyPublishers.ofString(body));
          if (consumer != null) consumer.accept(req);
        });
  }
}
