package base;

import static org.assertj.core.api.Assertions.assertThat;

import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

public class SetupTest extends IntegrationTest {
  @Test
  public void app_is_up() {
    JavalinTest.test(
        app,
        (server, client) -> {
          assertThat(client.get("/").code()).isEqualTo(200);
        });
  }

  @Test
  public void test_user_exists() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = client.get("/user/profile/test");
          assertThat(response.code()).isEqualTo(200);
          assertThat(response.body().string()).contains("<div>tests Profil</div>");
        });
  }
}
