package modules.user;

import static org.assertj.core.api.Assertions.assertThat;

import base.IntegrationTest;
import io.javalin.http.HttpStatus;
import io.javalin.testtools.JavalinTest;
import modules.helpers.TestHelpers;
import modules.user.model.User;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class UserControllerTest extends IntegrationTest {
  private static final String FAILED_LOGIN = "Login fehlgeschlagen";
  private static final String FAILED_REGISTRATION = "Passwörter stimmen nicht überein";

  @Test
  void loginNonexistentUsername() {
    JavalinTest.test(
        app,
        withCookies(),
        (server, client) -> {
          assertThat(login(client, "nonexistent").body().string())
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
        withCookies(),
        (server, client) -> {
          assertThat(login(client, "test").body().string())
              .as("Successfull")
              .doesNotContain(FAILED_LOGIN);
        });
  }

  @Test
  void register() {
    JavalinTest.test(
        app,
        withCookies(),
        (server, client) -> {
          Response response =
              postWithUrlEncodedBody(
                  client, "/registrieren", "username=new&password=test&password2=test");
          assertThat(response.code()).as("Registration successfull").isEqualTo(200);
          String html = response.body().string();
          assertThat(html).doesNotContain(FAILED_REGISTRATION);

          assertThat(login(client, "new").body().string())
              .as("Successfull")
              .doesNotContain(FAILED_LOGIN);
        });
  }

  @Test
  void registerNotMatchingPasswords() {
    JavalinTest.test(
        app,
        withCookies(),
        (server, client) -> {
          assertThat(
                  postWithUrlEncodedBody(
                          client, "/registrieren", "username=new&password=test&password2=wrong")
                      .body()
                      .string())
              .as("Registration failed")
              .contains(FAILED_REGISTRATION);
          assertThat(login(client, "new").body().string())
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
              .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getCode());
          assertThat(
                  postWithUrlEncodedBody(client, "/login", "username=test&password=new")
                      .body()
                      .string())
              .as("User password not overridden")
              .contains(FAILED_LOGIN);
        });
  }

  @Test
  void unauthorizedProfileUpdate() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response =
              postWithUrlEncodedBody(
                  client, "/user/update", "username=changed&about=changed2&hobbies=changed3");
          assertThat(response.code())
              .as("Unauthorized request")
              .isEqualTo(HttpStatus.UNAUTHORIZED.getCode());
        });
  }

  @Test
  void updateProfile() {
    JavalinTest.test(
        app,
        withCookies(),
        (server, client) -> {
          assertThat(login(client, "test").code()).isEqualTo(200);
          Response response =
              postWithUrlEncodedBody(
                  client, "/user/update", "username=changed&about=changed2&hobbies=changed3");
          assertThat(response.code()).as("Change username").isEqualTo(200);
          assertThat(response.body().string())
              .as("Updated username")
              .contains("changed")
              .as("Updated about")
              .contains("changed2")
              .as("Updated hobbies")
              .contains("changed3");

          User user = TestHelpers.toUser(client.get("/api/user/1"));
          assertThat(user.getUsername()).isEqualTo("changed");
          assertThat(user.getAbout()).isEqualTo("changed2");
          assertThat(user.getHobbies()).isEqualTo("changed3");
        });
  }

  @Test
  void inputTooLong() {
    JavalinTest.test(
        app,
        withCookies(),
        (server, client) -> {
          assertThat(login(client, "test").code()).isEqualTo(200);

          assertThat(
                  postWithUrlEncodedBody(
                          client,
                          "/user/update",
                          "about=x&hobbies=x&username=" + StringUtils.repeat("a", 100))
                      .body()
                      .string())
              .as("username too long")
              .contains("Benutzername zu lang");

          assertThat(
                  postWithUrlEncodedBody(
                          client,
                          "/user/update",
                          "username=x&hobbies=x&about=" + StringUtils.repeat("a", 300))
                      .body()
                      .string())
              .as("about too long")
              .contains("Ueber mich zu lang");
          assertThat(
                  postWithUrlEncodedBody(
                          client,
                          "/user/update",
                          "username=x&about=x&hobbies=" + StringUtils.repeat("a", 300))
                      .body()
                      .string())
              .as("hobbies too long")
              .contains("Hobbies zu lang");
          User user = TestHelpers.toUser(client.get("/api/user/1"));
          assertThat(user.getUsername()).isEqualTo("test");
          assertThat(user.getAbout()).isEqualTo("test about");
          assertThat(user.getHobbies()).isEqualTo("test hobbies");
        });
  }
}
