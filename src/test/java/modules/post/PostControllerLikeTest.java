package modules.post;

import static org.assertj.core.api.Assertions.assertThat;

import base.IntegrationTest;
import io.javalin.http.HttpCode;
import io.javalin.testtools.HttpClient;
import io.javalin.testtools.JavalinTest;
import java.io.IOException;
import java.util.stream.Stream;
import modules.helpers.PostTestHelpers;
import modules.post.model.Post;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PostControllerLikeTest extends IntegrationTest {
  @Test
  void unAuthorizedLike() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = postWithUrlEncodedBody(client, "/like", "post=3");
          assertThat(response.code())
              .as("Unauthorized request")
              .isEqualTo(HttpCode.UNAUTHORIZED.getStatus());
          Post post = requestPostById(client, 3);
          assertThat(post.getLikesCount()).isZero();
        });
  }

  @Test
  void likePost() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Post post = requestPostById(client, 3);

          assertThat(post.getLikesCount()).as("number of likes before").isZero();
          assertThat(useLogin(client, "test").code()).as("Login successfull").isEqualTo(200);

          Response response = postWithUrlEncodedBody(client, "/like", "post=3");
          assertThat(response.code()).as("Authorized request for liking postid 3").isEqualTo(200);

          post = requestPostById(client, 3);
          assertThat(post.getLikesCount()).as("number of likes afterwards").isOne();
        });
  }

  @Test
  void likeNonexistentPost() {
    JavalinTest.test(
        app,
        (server, client) -> {
          assertThat(useLogin(client, "test").code()).as("Login successfull").isEqualTo(200);
          Response response = postWithUrlEncodedBody(client, "/like", "post=999");
          assertThat(response.code())
              .as("nonexistent post")
              .isEqualTo(HttpCode.NOT_FOUND.getStatus());
        });
  }

  @ParameterizedTest(name = "#{index}- Test redirect with referer {arguments}")
  @MethodSource("provideRefererParameters")
  void redirectBackToKnownRefererOnLike(String referer, String expectedPath) {
    JavalinTest.test(
        app,
        (server, client) -> {
          assertThat(useLogin(client, "test").code()).as("Login successfull").isEqualTo(200);
          Response response =
              postWithUrlEncodedBody(
                  client,
                  "/like",
                  "post=3",
                  referer == null ? null : req -> req.addHeader("referer", referer));
          assertThat(response.code()).isEqualTo(200);
          assertThat(response.request().url().fragment()).as("jump to post").isEqualTo("post-3");
          assertThat(response.request().url().encodedPath())
              .as("validated path from referer")
              .isEqualTo(expectedPath);
        });
  }

  private static Stream<Arguments> provideRefererParameters() {
    String defaultPath = "/pinnwand/test2";
    return Stream.of(
        Arguments.of("https://domain.tld/pinnwand/test2", defaultPath),
        Arguments.of("https://domain.tld/", "/"),
        Arguments.of("https://domain.tld", "/"),
        Arguments.of("https://domain.tld/unexpected/path", defaultPath),
        Arguments.of("mailto:bad@uri.com", defaultPath),
        Arguments.of("unexpected referer format", defaultPath),
        Arguments.of(null, defaultPath));
  }

  @Test
  void unlikePost() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Post post = requestPostById(client, 1);
          assertThat(post.getLikesCount()).as("number of likes before").isEqualTo(2);
          assertThat(post.getRecentLikes()).anyMatch(user -> user.getUsername().equals("test"));
          assertThat(useLogin(client, "test").code()).as("Login successfull").isEqualTo(200);

          Response response = postWithUrlEncodedBody(client, "/unlike", "post=1");
          assertThat(response.code())
              .as("Redirect authorized request for liking postid 1")
              .isEqualTo(200);

          post = requestPostById(client, 1);
          assertThat(post.getLikesCount()).as("number of likes afterwards").isOne();
          assertThat(post.getRecentLikes()).noneMatch(user -> user.getUsername().equals("test"));
        });
  }

  private Post requestPostById(HttpClient client, int id) throws IOException {
    return PostTestHelpers.toPost(client.get("/api/post/" + id));
  }
}
