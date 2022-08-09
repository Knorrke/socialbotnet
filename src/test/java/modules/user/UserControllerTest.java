package modules.user;

import static org.assertj.core.api.Assertions.assertThat;

import base.IntegrationTest;
import io.javalin.http.HttpCode;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

class UserControllerTest extends IntegrationTest {
  private static final String FAILED_LOGIN = "Login fehlgeschlagen";
  private static final String FAILED_REGISTRATION = "Passwörter stimmen nicht überein";

  @Test
  void loginNonexistentUsername() {
    JavalinTest.test(
        app,
        (server, client) -> {
          assertThat(useLogin(client, "nonexistent").body().string())
              .as("nonexistent user")
              .contains(FAILED_LOGIN);
        });
  }

  @Test
  void loginWrongPassword() {
    JavalinTest.test(
        app,
        (server, client) -> {
          assertThat(
                  postWithUrlEncodedBody(client, "/login", "username=test&password=wrong")
                      .body()
                      .string())
              .as("Wrong password")
              .contains(FAILED_LOGIN);
        });
  }

  @Test
  void login() {
    JavalinTest.test(
        app,
        (server, client) -> {
          assertThat(useLogin(client, "test").body().string())
              .as("Successfull")
              .doesNotContain(FAILED_LOGIN);
        });
  }

  @Test
  void register() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response =
              postWithUrlEncodedBody(
                  client, "/registrieren", "username=new&password=test&password2=test");
          assertThat(response.code()).as("Registration successfull").isEqualTo(200);
          String html = response.body().string();
          assertThat(html).doesNotContain(FAILED_REGISTRATION);

          assertThat(useLogin(client, "new").body().string())
              .as("Successfull")
              .doesNotContain(FAILED_LOGIN);
        });
  }

  @Test
  void registerNotMatchingPasswords() {
    JavalinTest.test(
        app,
        (server, client) -> {
          assertThat(
                  postWithUrlEncodedBody(
                          client, "/registrieren", "username=new&password=test&password2=wrong")
                      .body()
                      .string())
              .as("Registration failed")
              .contains(FAILED_REGISTRATION);
          assertThat(useLogin(client, "new").body().string())
              .as("User not created")
              .contains(FAILED_LOGIN);
        });
  }

  @Test
  void registerExistingUsernamePasswords() {
    JavalinTest.test(
        app,
        (server, client) -> {
          assertThat(
                  postWithUrlEncodedBody(
                          client, "/registrieren", "username=test&password=new&password2=new")
                      .code())
              .as("Registration failed")
              .isEqualTo(HttpCode.INTERNAL_SERVER_ERROR.getStatus());
          assertThat(
                  postWithUrlEncodedBody(client, "/login", "username=test&password=new")
                      .body()
                      .string())
              .as("User password not overridden")
              .contains(FAILED_LOGIN);
        });
  }
}
