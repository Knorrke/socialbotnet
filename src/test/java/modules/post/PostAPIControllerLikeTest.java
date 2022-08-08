package modules.post;

import static org.assertj.core.api.Assertions.assertThat;

import base.IntegrationTest;
import io.javalin.http.HttpCode;
import io.javalin.testtools.HttpClient;
import io.javalin.testtools.JavalinTest;
import java.io.IOException;
import modules.helpers.PostTestHelpers;
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
              .isEqualTo(HttpCode.UNAUTHORIZED.getStatus());
          Post post = PostTestHelpers.toPost(client.get("/api/post/3"));
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
              .isEqualTo(HttpCode.BAD_REQUEST.getStatus());
          assertThat(PostTestHelpers.toError(response).getError())
              .contains("Parameter postid fehlt");
          Post post = PostTestHelpers.toPost(client.get("/api/post/3"));
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
          post = PostTestHelpers.toPost(response);
          assertThat(post.getLikesCount()).as("number of likes updated in answer").isOne();
          assertThat(post.getRecentLikes().get(0).getUsername()).isEqualTo("test");
          post = requestPostById(client, 3);
          assertThat(post.getLikesCount())
              .as("number of likes persistet in subsequent requests")
              .isOne();
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
              .isEqualTo(HttpCode.NOT_FOUND.getStatus());
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
          post = PostTestHelpers.toPost(response);
          assertThat(post.getLikesCount()).as("number of likes updated in answer").isOne();
          assertThat(post.getRecentLikes()).noneMatch(user -> user.getUsername().equals("test"));

          post = requestPostById(client, 1);
          assertThat(post.getLikesCount())
              .as("number of likes persistet in subsequent requests")
              .isOne();
          assertThat(post.getRecentLikes()).noneMatch(user -> user.getUsername().equals("test"));
        });
  }

  private Post requestPostById(HttpClient client, int id) throws IOException {
    return PostTestHelpers.toPost(client.get("/api/post/" + id));
  }
}
