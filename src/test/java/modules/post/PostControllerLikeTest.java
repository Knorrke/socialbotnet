package modules.post;

import static org.assertj.core.api.Assertions.assertThat;

import base.IntegrationTest;
import io.javalin.http.HttpStatus;
import io.javalin.testtools.HttpClient;
import io.javalin.testtools.JavalinTest;
import io.javalin.testtools.Response;
import java.io.IOException;
import java.util.stream.Stream;
import modules.helpers.TestHelpers;
import modules.post.model.Post;
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
              .isEqualTo(HttpStatus.UNAUTHORIZED.getCode());
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
          assertThat(login(client, "test").code()).as("Login successfull").isEqualTo(302);

          Response response = postWithUrlEncodedBody(client, "/like", "post=3");
          assertThat(response.code()).as("Authorized request for liking postid 3").isEqualTo(302);

          post = requestPostById(client, 3);
          assertThat(post.getLikesCount()).as("number of likes afterwards").isOne();
        });
  }

  @Test
  void likeNonexistentPost() {
    JavalinTest.test(
        app,
        (server, client) -> {
          assertThat(login(client, "test").code()).as("Login successfull").isEqualTo(302);
          Response response = postWithUrlEncodedBody(client, "/like", "post=999");
          assertThat(response.code())
              .as("nonexistent post")
              .isEqualTo(HttpStatus.NOT_FOUND.getCode());
        });
  }

  @ParameterizedTest(name = "#{index}- Test redirect with referer {arguments}")
  @MethodSource("provideRefererParameters")
  void redirectBackToKnownRefererOnLike(String referer, String expectedPath) {
    JavalinTest.test(
        app,
        (server, client) -> {
          assertThat(login(client, "test").code()).as("Login successfull").isEqualTo(302);
          Response response =
              postWithUrlEncodedBody(
                  client,
                  "/like",
                  "post=3",
                  referer == null ? null : req -> req.header("referer", referer));
          assertThat(response.code()).isEqualTo(302);
          String redirectLocation = response.headers().get("Location").get(0);
          assertThat(redirectLocation.split("#")[1]).as("jump to post").isEqualTo("post-3");
          assertThat(redirectLocation.split("#")[0])
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
          assertThat(login(client, "test").code()).as("Login successfull").isEqualTo(302);

          Response response = postWithUrlEncodedBody(client, "/unlike", "post=1");
          assertThat(response.code())
              .as("Redirect authorized request for liking postid 1")
              .isEqualTo(302);

          post = requestPostById(client, 1);
          assertThat(post.getLikesCount()).as("number of likes afterwards").isOne();
          assertThat(post.getRecentLikes()).noneMatch(user -> user.getUsername().equals("test"));
        });
  }

  private Post requestPostById(HttpClient client, int id) throws IOException {
    return TestHelpers.toPost(client.get("/api/post/" + id));
  }
}
