package base;

import static org.assertj.core.api.Assertions.assertThat;

import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

class SetupTest extends IntegrationTest {
  @Test
  void appIsUp() {
    JavalinTest.test(
        app,
        (server, client) -> {
          assertThat(client.get("/").code()).isEqualTo(200);
        });
  }

  @Test
  void testUserExists() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = client.get("/user/profile/test");
          assertThat(response.code()).isEqualTo(200);
          assertThat(response.body().string()).contains("<div>tests Profil</div>");
        });
  }
}
