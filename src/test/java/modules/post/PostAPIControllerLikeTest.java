package modules.post;

import static org.assertj.core.api.Assertions.assertThat;

import base.IntegrationTest;
import io.javalin.http.HttpStatus;
import io.javalin.testtools.HttpClient;
import io.javalin.testtools.JavalinTest;
import java.io.IOException;
import modules.helpers.TestHelpers;
import modules.post.model.Post;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

class PostAPIControllerLikeTest extends IntegrationTest {
  private static String AUTH_PARAMS = "username=test&password=test";

  @Test
  void unAuthorizedLike() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = postWithUrlEncodedBody(client, "/api/like", "postid=3");
          assertThat(response.code())
              .as("Unauthorized request")
              .isEqualTo(HttpStatus.UNAUTHORIZED.getCode());
          Post post = TestHelpers.toPost(client.get("/api/post/3"));
          assertThat(post.getLikesCount()).isZero();
        });
  }

  @Test
  void missingPostidParameter() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = postWithUrlEncodedBody(client, "/api/like", AUTH_PARAMS);
          assertThat(response.code())
              .as("Missing postid parameter")
              .isEqualTo(HttpStatus.BAD_REQUEST.getCode());
          assertThat(TestHelpers.toError(response).getError()).contains("Parameter postid fehlt");
          Post post = TestHelpers.toPost(client.get("/api/post/3"));
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

          Response response =
              postWithUrlEncodedBody(client, "/api/like", AUTH_PARAMS + "&postid=3");
          assertThat(response.code()).as("Authorized request for liking postid 3").isEqualTo(200);
          post = TestHelpers.toPost(response);
          assertThat(post.getLikesCount()).as("number of likes updated in answer").isOne();
          assertThat(post.getRecentLikes().get(0).getUsername()).isEqualTo("test");
          post = requestPostById(client, 3);
          assertThat(post.getLikesCount())
              .as("number of likes persistet in subsequent requests")
              .isOne();
        });
  }

  @Test
  void likeTwice() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Post post = requestPostById(client, 3);
          assertThat(post.getLikesCount()).as("number of likes before").isZero();

          Response response =
              postWithUrlEncodedBody(client, "/api/like", AUTH_PARAMS + "&postid=3");
          assertThat(response.code()).as("Authorized request for liking postid 3").isEqualTo(200);

          response = postWithUrlEncodedBody(client, "/api/like", AUTH_PARAMS + "&postid=3");
          assertThat(response.code()).as("Second request should get ignored").isEqualTo(200);
          post = TestHelpers.toPost(response);
          assertThat(post.getLikesCount()).as("second request doesn't change likes").isOne();
        });
  }

  @Test
  void likeNonexistentPost() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response =
              postWithUrlEncodedBody(client, "/api/like", AUTH_PARAMS + "&postid=999");
          assertThat(response.code())
              .as("nonexistent post")
              .isEqualTo(HttpStatus.NOT_FOUND.getCode());
        });
  }

  @Test
  void unlikePost() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Post post = requestPostById(client, 1);
          assertThat(post.getLikesCount()).as("number of likes before").isEqualTo(2);
          assertThat(post.getRecentLikes()).anyMatch(user -> user.getUsername().equals("test"));

          Response response =
              postWithUrlEncodedBody(client, "/api/unlike", AUTH_PARAMS + "&postid=1");
          assertThat(response.code()).as("Authorized request for liking postid 1").isEqualTo(200);
          post = TestHelpers.toPost(response);
          assertThat(post.getLikesCount()).as("number of likes updated in answer").isOne();
          assertThat(post.getRecentLikes()).noneMatch(user -> user.getUsername().equals("test"));

          post = requestPostById(client, 1);
          assertThat(post.getLikesCount())
              .as("number of likes persistet in subsequent requests")
              .isOne();
          assertThat(post.getRecentLikes()).noneMatch(user -> user.getUsername().equals("test"));
        });
  }

  @Test
  void unlikeTwice() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Post post = requestPostById(client, 1);
          assertThat(post.getLikesCount()).as("number of likes before").isEqualTo(2);
          assertThat(post.getRecentLikes()).anyMatch(user -> user.getUsername().equals("test"));

          Response response =
              postWithUrlEncodedBody(client, "/api/unlike", AUTH_PARAMS + "&postid=1");
          assertThat(response.code()).as("Authorized request for liking postid 1").isEqualTo(200);

          response = postWithUrlEncodedBody(client, "/api/unlike", AUTH_PARAMS + "&postid=1");
          assertThat(response.code()).as("Second request should get ignored").isEqualTo(200);
          post = TestHelpers.toPost(response);
          assertThat(post.getLikesCount()).as("second request doesn't change likes").isOne();
          assertThat(post.getRecentLikes()).noneMatch(user -> user.getUsername().equals("test"));
        });
  }

  private Post requestPostById(HttpClient client, int id) throws IOException {
    return TestHelpers.toPost(client.get("/api/post/" + id));
  }
}
